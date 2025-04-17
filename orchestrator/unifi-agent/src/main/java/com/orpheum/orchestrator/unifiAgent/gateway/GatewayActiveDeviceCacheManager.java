package com.orpheum.orchestrator.unifiAgent.gateway;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.orpheum.orchestrator.unifiAgent.auth.GatewayAuthConnectionManager;
import com.orpheum.orchestrator.unifiAgent.model.GatewayAuthConnection;
import com.orpheum.orchestrator.unifiAgent.model.UnifiGatewayActiveDevice;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import com.orpheum.orchestrator.unifiAgent.support.UnifiGatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * A class which manages the agent's internal cache around authenticated devices by
 *
 * <ul>
 *     <li>Periodically syncing with the gateway to ensure that any authenticated devices are reflected in the cache;</li>
 *     <li>Offering a resolve method which (i) first attempts to resolve the device by IP/MAC addresses from the cache,
 *     and if not found (ii) polls the gateway to resolve the requested device, up to a configurable timeout. If
 *     successfully resolved, (iii) it will add the active device in the cache.</li>
 * </ul>
 *
 * Note that the authorised device shall be kept in the cache for a time matching that configured in the UniFi gateway
 * via the hotspot portal. Given that the item is usually added to the cache almost instantly after successful gateway
 * authorisation, this should result in the agent and gateway being in sync. Restarting the agent however breaks this
 * assumption, since the authorised devices will be added via polling at a later time than the actual authorisation. A
 * solution to this could be to set the cache TTL equal to the polling rate, and refresh the cache on each poll.
 */
public class GatewayActiveDeviceCacheManager implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayActiveDeviceCacheManager.class);

    private static final String SITE_IDENTIFIER = ApplicationProperties.getString("site_identifier");

    private static final Integer AUTHORIZED_DEVICE_CACHE_EXPIRY_MS = ApplicationProperties.getInteger("authorized_device_cache_expiry_ms");
    private static final Long DEVICE_RESOLUTION_TIMEOUT_MS = ApplicationProperties.getLong("authorized_device_resolution_timeout_ms");

    private final Cache<String, UnifiGatewayActiveDevice> authorisedDeviceCacheByIp = Caffeine.newBuilder()
            .expireAfterWrite(AUTHORIZED_DEVICE_CACHE_EXPIRY_MS, TimeUnit.MILLISECONDS)
            .build();

    private final Cache<String, UnifiGatewayActiveDevice> authorisedDeviceCacheByMacs = Caffeine.newBuilder()
            .expireAfterWrite(AUTHORIZED_DEVICE_CACHE_EXPIRY_MS, TimeUnit.MILLISECONDS)
            .build();

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    @Override
    public void run() {
        if (isRunning.get()) {
            LOGGER.debug("Ongoing run detected. Skipping run.");
            return;
        }

        isRunning.set(true);
        GatewayAuthConnection connection = null;
        try {
            connection = GatewayAuthConnectionManager.borrowConnection();

            cacheAuthorizedDevices(UnifiGatewayClient.getActiveDevices(connection));
        } catch (Exception e) {
            LOGGER.error("Unexpected exception encountered. Skipping run.", e);
        } finally {
            if (connection != null) {
                GatewayAuthConnectionManager.returnConnection(connection);
            }
            isRunning.set(false);
        }
    }

    public Optional<UnifiGatewayActiveDevice> resolveAuthorisedCachedDeviceByIp(String ip) {
        return Optional.ofNullable(authorisedDeviceCacheByIp.getIfPresent(ip));
    }

    public Optional<UnifiGatewayActiveDevice> resolveAuthorisedCachedDeviceByMacs(String mac, String ap_mac) {
        return Optional.ofNullable(authorisedDeviceCacheByIp.getIfPresent(buildMacsKey(mac, ap_mac)));
    }

    public void addAuthorisedCachedDevice(UnifiGatewayActiveDevice device) {
        if (authorisedDeviceCacheByIp.getIfPresent(device.resolveIp()) == null) {
            LOGGER.debug("Added authorised device to cache by IP. [Device:{}]", device);
            authorisedDeviceCacheByIp.put(device.resolveIp(), device);
        }
        if (authorisedDeviceCacheByMacs.getIfPresent(buildMacsKey(device)) == null) {
            LOGGER.debug("Added authorised device to cache by MACs. [Device:{}]", device);
            authorisedDeviceCacheByMacs.put(buildMacsKey(device), device);
        }
    }

    private String buildMacsKey(UnifiGatewayActiveDevice device) {
        return buildMacsKey(device.id(), device.ap_mac());
    }

    private String buildMacsKey(String mac, String ap_mac) {
        return mac + ":" + ap_mac;
    }

    public Optional<UnifiGatewayActiveDevice> resolveDeviceByIp(String ip) {
        return resolve(
                () -> resolveAuthorisedCachedDeviceByIp(ip),
                devices -> resolveMatchingDevice(devices, ip)
        );
    }

    public Optional<UnifiGatewayActiveDevice> resolveDeviceByMacs(String mac, String ap_mac) {
        return resolve(
                () -> resolveAuthorisedCachedDeviceByMacs(mac, ap_mac),
                devices -> resolveMatchingDevice(devices, mac, ap_mac)
        );
    }

    private Optional<UnifiGatewayActiveDevice> resolve(Callable<Optional<UnifiGatewayActiveDevice>> cacheResolver,
                                                       Function<List<UnifiGatewayActiveDevice>, Optional<UnifiGatewayActiveDevice>> listResolver) {
        try {
            Optional<UnifiGatewayActiveDevice>  cachedDevice = cacheResolver.call();
            if (cachedDevice.isPresent()) {
                return cachedDevice;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long initialTimestamp = System.currentTimeMillis();

        while ((System.currentTimeMillis() - initialTimestamp) < DEVICE_RESOLUTION_TIMEOUT_MS) {
            GatewayAuthConnection connection = null;
            try {
                connection = GatewayAuthConnectionManager.borrowConnection();

                List<UnifiGatewayActiveDevice> activeDevices = UnifiGatewayClient.getActiveDevices(connection);
                // Only cache if authorised
                cacheAuthorizedDevices(activeDevices);

                Optional<UnifiGatewayActiveDevice> unifiGatewayActiveDevice = listResolver.apply(activeDevices);

                if (unifiGatewayActiveDevice.isPresent()) {
                    return unifiGatewayActiveDevice;
                }
            } catch (Exception e) {
                LOGGER.error("Unexpected exception encountered. Skipping resolution.", e);
            } finally {
                if (connection != null) {
                    GatewayAuthConnectionManager.returnConnection(connection);
                }
                isRunning.set(false);
            }
        }

        return Optional.empty();
    }

    public void clearCache() {
        authorisedDeviceCacheByIp.invalidateAll();
        authorisedDeviceCacheByMacs.invalidateAll();
        LOGGER.debug("Emptied authorised device cache");
    }

    private Optional<UnifiGatewayActiveDevice> resolveMatchingDevice(List<UnifiGatewayActiveDevice> activeDevices, final String ip) {
        return activeDevices.stream()
                .filter(device -> device.matches(ip, SITE_IDENTIFIER))
                .findFirst();
    }

    private Optional<UnifiGatewayActiveDevice> resolveMatchingDevice(List<UnifiGatewayActiveDevice> activeDevices, final String mac, final String ap_mac) {
        return activeDevices.stream()
                .filter(device -> device.matches(mac, ap_mac, SITE_IDENTIFIER))
                .findFirst();
    }

    private void cacheAuthorizedDevices(final List<UnifiGatewayActiveDevice> devices) {
        devices.stream()
               .filter(device -> device.authorized() && SITE_IDENTIFIER.equals(device.essid()))
               .forEach(this::addAuthorisedCachedDevice);
    }

    public List<UnifiGatewayActiveDevice> getAuthorisedDevicesView() {
        return new ArrayList<>(authorisedDeviceCacheByIp.asMap().values());
    }

}
