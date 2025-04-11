package com.orpheum.orchestrator.unifiAgent.model;

public record BackstageAuthenticationRequest(
        String macAddress,
        String accessPointMacAddress,
        String siteIdentifier,
        Long timestamp) {

}
