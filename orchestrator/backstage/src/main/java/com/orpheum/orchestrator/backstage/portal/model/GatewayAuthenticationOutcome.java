package com.orpheum.orchestrator.backstage.portal.model;

public record GatewayAuthenticationOutcome(BackstageAuthenticationRequest request, GatewayAuthenticationOutcomeStatus outcome, String message) {

    public GatewayAuthenticationOutcome(BackstageAuthenticationRequest request, GatewayAuthenticationOutcomeStatus outcome) {
        this(request, outcome, null);
    }

}
