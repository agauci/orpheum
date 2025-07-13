package com.orpheum.orchestrator.unifiAgent.gateway;

import com.orpheum.orchestrator.unifiAgent.model.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.unifiAgent.model.UnifiGatewayActiveDevice;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import com.orpheum.orchestrator.unifiAgent.support.BackstageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The "main" service which (i) continuously polls the backstage server, (ii) identifies which pending backstage
 * authorisation requests have not been seen yet by the agent, and (iii) asynchronously triggers a fresh
 * {@link GatewayAuthorisationRunnable} to perform the UniFi gateway authorisation accordingly.
 */
public class GatewayAuthorisationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayAuthorisationService.class);

    private static final AtomicBoolean IS_RUNNING = new AtomicBoolean(true);

    private static final GatewayActiveDeviceCacheManager CACHE_MANAGER = new GatewayActiveDeviceCacheManager();

    public static void start(final ScheduledExecutorService service) {
        service.scheduleAtFixedRate(
                CACHE_MANAGER,
                30000L, // Wait 30 seconds before the first sync request
                ApplicationProperties.getInteger("gateway_active_device_cache_ping_delay_ms"),
                TimeUnit.MILLISECONDS
        );

        LOGGER.info("Started polling of backstage pending authorisation requests.");
        while (IS_RUNNING.get()) {
            try {
                Thread.sleep(ApplicationProperties.getLong("backstage_server_poll_delay_ms"));

                List<BackstageAuthorisationRequest> retrievedPendingAuths = BackstageClient.getPendingAuthorisationRequests();

                if (retrievedPendingAuths.size() != 0) {
                    // Check if any of the retrieved pending authorisation requests have already been actioned on. Those which
                    // are currently being worked upon should be ignored.
                    final List<BackstageAuthorisationRequest> newPendingAuths = BackstageAuthRepository.merge(retrievedPendingAuths);

                    if (newPendingAuths.size() != 0) {
                        newPendingAuths.forEach(pendingAuth -> service.execute(new GatewayAuthorisationRunnable(pendingAuth)));
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Gateway authentication service encountered exception. Skipping run.", e);
            }
        }
    }

    public static void shutdown() {
        IS_RUNNING.set(false);
        LOGGER.debug("Stopped GatewayAuthenticationService");
    }

    public static void clearCache() {
        CACHE_MANAGER.clearCache();
    }

    public static Optional<UnifiGatewayActiveDevice> resolveAuthorisedCachedDevice(final String ip) {
        return CACHE_MANAGER.resolveAuthorisedCachedDeviceByIp(ip);
    }

    public static Optional<UnifiGatewayActiveDevice> resolveDeviceByIp(final String ip) {
        return CACHE_MANAGER.resolveDeviceByIp(ip);
    }

    public static Optional<UnifiGatewayActiveDevice> resolveDeviceByMacs(final String mac, final String ap_mac) {
        return CACHE_MANAGER.resolveDeviceByMacs(mac, ap_mac);
    }

    public static void addAuthorisedDeviceToCache(UnifiGatewayActiveDevice device) {
        CACHE_MANAGER.addAuthorisedDeviceToCache(device);
    }

    public static List<UnifiGatewayActiveDevice> getAuthorisedDevicesView() {
        return CACHE_MANAGER.getAuthorisedDevicesView();
    }

}
