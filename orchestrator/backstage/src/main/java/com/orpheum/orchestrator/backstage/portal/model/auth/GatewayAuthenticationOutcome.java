package com.orpheum.orchestrator.backstage.portal.model.auth;

public record GatewayAuthenticationOutcome(BackstageAuthorisationRequest request, GatewayAuthenticationOutcomeStatus outcome, String message) {

    public GatewayAuthenticationOutcome(BackstageAuthorisationRequest request, GatewayAuthenticationOutcomeStatus outcome) {
        this(request, outcome, null);
    }

}
