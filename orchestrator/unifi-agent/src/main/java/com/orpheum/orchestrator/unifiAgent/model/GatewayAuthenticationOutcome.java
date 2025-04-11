package com.orpheum.orchestrator.unifiAgent.model;

public record GatewayAuthenticationOutcome(BackstageAuthenticationRequest request, GatewayAuthenticationOutcomeStatus outcome, String message) {

    public GatewayAuthenticationOutcome(BackstageAuthenticationRequest request, GatewayAuthenticationOutcomeStatus outcome) {
        this(request, outcome, null);
    }

}
