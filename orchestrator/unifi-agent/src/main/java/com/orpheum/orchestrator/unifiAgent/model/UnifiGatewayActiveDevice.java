package com.orpheum.orchestrator.unifiAgent.model;

/**
 * A record containing the relevant active device details, as returned by the UniFi gateway
 *
 * @param id            the device id, i.e. its MAC address
 * @param ap_mac        the access point mac address which this device is connected to
 * @param ip            the device's assigned IP address
 * @param fixed_ip      the device's fixed IP, should it have been assigned a fixed ip via the portal
 * @param essid         the site's SSID which this device is connected to
 * @param authorized    true if the device has been authorised, false otherwise
 */
public record UnifiGatewayActiveDevice(String id, String ap_mac, String ip, String fixed_ip, String essid, boolean authorized) {

    public boolean matches(final String ip, final String siteIdentifier) {
        return (ip.equals(this.ip) || ip.equals(this.fixed_ip)) && siteIdentifier.equals(essid);
    }

    public boolean matches(final String id, final String ap_mac, final String siteIdentifier) {
        return id.equals(this.id) && ap_mac.equals(this.ap_mac) && siteIdentifier.equals(essid);
    }

    public String resolveIp() {
        return (fixed_ip != null) ? fixed_ip : ip;
    }

    public String resolveMacsKey() {
        return resolveMacsKey(id, ap_mac);
    }

    public static String resolveMacsKey(String mac, String ap_mac) {
        return mac + ":" + ap_mac;
    }

}
