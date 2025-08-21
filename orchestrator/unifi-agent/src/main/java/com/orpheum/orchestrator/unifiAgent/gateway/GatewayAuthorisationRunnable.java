package com.orpheum.orchestrator.unifiAgent.gateway;

import com.orpheum.orchestrator.unifiAgent.auth.GatewayAuthConnectionManager;
import com.orpheum.orchestrator.unifiAgent.model.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.unifiAgent.model.GatewayAuthConnection;
import com.orpheum.orchestrator.unifiAgent.model.GatewayAuthorisationOutcome;
import com.orpheum.orchestrator.unifiAgent.model.UnifiGatewayActiveDevice;
import com.orpheum.orchestrator.unifiAgent.support.BackstageClient;
import com.orpheum.orchestrator.unifiAgent.support.UnifiGatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static com.orpheum.orchestrator.unifiAgent.model.GatewayAuthenticationOutcomeStatus.FAILED;
import static com.orpheum.orchestrator.unifiAgent.model.GatewayAuthenticationOutcomeStatus.SUCCESS;

/**
 * A Runnable implementation which, given a pending backstage device authentication request, (i) performs the
 * gateway authorisation request, (ii) notifies backstage of the authorisation outcome, and if successful (iii) caches
 * the device within the agent's cache manager. Note that
 *
 * <ul>
 *     <li>If the authorisation request is by IP, this runnable will first attempt to resolve the device's full
 *     details via the cache manager, i.e. first within the local cache, and if not available by polling the
 *     gateway with a timoeut. The resolved MAC address and access point MAC address will then be used to
 *     authorise the device.</li>
 *     <li>If the authorisation request is by MAC addresses (i.e. device and access point), authorisation on
 *     the gateway is done immediately. However, the full device details are then resolved post backstage
 *     notification in order to hydrate the cache with the device's full details.</li>
 * </ul>
 */
public class GatewayAuthorisationRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayAuthorisationRunnable.class);
    private final BackstageAuthorisationRequest pendingAuthorisationRequest;

    public GatewayAuthorisationRunnable(final BackstageAuthorisationRequest authorisationRequest) {
        this.pendingAuthorisationRequest = authorisationRequest;
    }

    @Override
    public void run() {
        GatewayAuthConnection gatewayAuthConnection = null;
        try {
            LOGGER.debug("Starting gateway device authorization process. [Request:{}]", pendingAuthorisationRequest);

            gatewayAuthConnection = GatewayAuthConnectionManager.borrowConnection();

            String macAddress = pendingAuthorisationRequest.macAddress();
            String apMacAddress = pendingAuthorisationRequest.accessPointMacAddress();
            Optional<UnifiGatewayActiveDevice> unifiGatewayActiveDevice = Optional.empty();
            if (isEmptyOrNull(macAddress) || isEmptyOrNull(apMacAddress)) {
                LOGGER.debug("Missing MAC address or access point MAC address. Attempting to resolve device by ip. [Request:{}]", pendingAuthorisationRequest);

                if (isEmptyOrNull(pendingAuthorisationRequest.ip())) {
                    BackstageClient.notifyAuthorisationOutcome(new GatewayAuthorisationOutcome(pendingAuthorisationRequest, FAILED, "Missing IP in authorization request"));
                    return;
                }

                unifiGatewayActiveDevice = GatewayAuthorisationService.resolveDeviceByIp(pendingAuthorisationRequest.ip());

                if (unifiGatewayActiveDevice.isEmpty()) {
                    BackstageClient.notifyAuthorisationOutcome(new GatewayAuthorisationOutcome(pendingAuthorisationRequest, FAILED, "Unable to resolve device by IP"));
                    return;
                }

                macAddress = unifiGatewayActiveDevice.get().id();
                apMacAddress = unifiGatewayActiveDevice.get().ap_mac();
            }

            UnifiGatewayClient.authorizeDevice(
                    gatewayAuthConnection,
                    macAddress,
                    apMacAddress
            );

            BackstageClient.notifyAuthorisationOutcome(new GatewayAuthorisationOutcome(pendingAuthorisationRequest, SUCCESS));

            updateCache(unifiGatewayActiveDevice);

            LOGGER.debug("Gateway authentication complete. [Request:{}]", pendingAuthorisationRequest);
        } catch (Exception e) {
            LOGGER.error("Failed to authenticate request on the UniFi Gateway. Notifying backstage of outcome. [Request:{}]", pendingAuthorisationRequest, e);
            try {
                BackstageClient.notifyAuthorisationOutcome(new GatewayAuthorisationOutcome(pendingAuthorisationRequest, FAILED, "Http request failure"));
                LOGGER.error("Notified backstage of failed outcome. [Request:{}]", pendingAuthorisationRequest);
            } catch (IOException | InterruptedException ex) {
                LOGGER.error("Failed to notify backstage of outcome. [Request:{}]", pendingAuthorisationRequest, ex);
            }
        } finally {
            if (gatewayAuthConnection != null) {
                GatewayAuthConnectionManager.returnConnection(gatewayAuthConnection);
            }
            BackstageAuthRepository.onGatewayAuthorisationCompleted(pendingAuthorisationRequest);
        }
    }

    private void updateCache(Optional<UnifiGatewayActiveDevice> resolvedDevice) {
        UnifiGatewayActiveDevice device;
        if (resolvedDevice.isPresent()) {
            device = resolvedDevice.get();
            LOGGER.debug("Device already resolved by IP from gateway. [Device:{}]", device);
        } else {
            LOGGER.debug("Unable to readily cache device since IP is not known from request. Attempting to resolve via manager. [Request:{}]", pendingAuthorisationRequest);
            Optional<UnifiGatewayActiveDevice> resolvedDeviceByMacs = GatewayAuthorisationService.resolveDeviceByMacs(pendingAuthorisationRequest.macAddress(), pendingAuthorisationRequest.accessPointMacAddress());

            if (resolvedDeviceByMacs.isPresent()) {
                device = resolvedDeviceByMacs.get();
                LOGGER.debug("Resolved device from gateway by MAC & AP mac. [Device:{}, Request:{}]", device, pendingAuthorisationRequest);
            } else {
                LOGGER.warn("Unable to resolve device from gateway by MAC & AP mac in time. Skipping addition of device to cache. [Request:{}]", pendingAuthorisationRequest);
                return;
            }
        }

        GatewayAuthorisationService.addAuthorisedDeviceToCache(device);
    }

    private boolean isEmptyOrNull(final String str) {
        return str == null || str.isEmpty();
    }

}