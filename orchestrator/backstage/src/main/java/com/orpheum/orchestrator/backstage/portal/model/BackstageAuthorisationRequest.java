package com.orpheum.orchestrator.backstage.portal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

public record BackstageAuthorisationRequest(
        String macAddress,
        String accessPointMacAddress,
        String siteIdentifier,
        String ip,
        Long timestamp,
        @JsonIgnore
        BackstageAuthenticationRequestStatus status) {

    public String id() {
        return BackstageAuthorisationRequest.id(macAddress, accessPointMacAddress, ip, siteIdentifier);
    }

    public static String id(String macAddress, String accessPointMacAddress, String ip, String siteIdentifier) {
        return coalesce(macAddress) + ":" + coalesce(accessPointMacAddress) + ":" + coalesce(ip) + ":" + coalesce(siteIdentifier);
    }

    private static String coalesce(String str) {
        return (str != null) ? str : "";
    }

}
