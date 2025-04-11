package com.orpheum.orchestrator.backstage.portal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

public record BackstageAuthenticationRequest(
        String macAddress,
        String accessPointMacAddress,
        String siteIdentifier,
        Long timestamp,
        @JsonIgnore
        BackstageAuthenticationRequestStatus status) {

    public String id() {
        return BackstageAuthenticationRequest.id(macAddress, accessPointMacAddress, siteIdentifier);
    }

    public static String id(String macAddress, String accessPointMacAddress, String siteIdentifier) {
        return macAddress + ":" + accessPointMacAddress + ":" + siteIdentifier;
    }

}
