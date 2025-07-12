package com.orpheum.orchestrator.backstage.portal.model.auth;

public record GatewayAuthorisationOutcome(BackstageAuthorisationRequest request, GatewayAuthorisationOutcomeStatus outcome, String message) {

    public GatewayAuthorisationOutcome(BackstageAuthorisationRequest request, GatewayAuthorisationOutcomeStatus outcome) {
        this(request, outcome, null);
    }

}
