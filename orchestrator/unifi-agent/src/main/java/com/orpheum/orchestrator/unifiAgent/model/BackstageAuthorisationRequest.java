package com.orpheum.orchestrator.unifiAgent.model;

/**
 * Represents a request which has been authorised by backstage for authorisation on the gateway. Note that this request
 * uniquely identifies the device to be authorised either (i) by the device's assigned IP address, or (ii) by the
 * device's MAC address and acccess point MAC address, as forwarded by the UniFi gateway.
 *
 * @param macAddress            the device's MAC address
 * @param accessPointMacAddress the access point's MAC address
 * @param siteIdentifier        the unique site identifier, i.e. the site's SSID
 * @param ip                    the IP address assigned to the device.
 * @param timestamp             the authorisation request's timestamp.
 */
public record BackstageAuthorisationRequest(
        String macAddress,
        String accessPointMacAddress,
        String siteIdentifier,
        String ip,
        Long timestamp) {

}
