package com.orpheum.orchestrator.unifiAgent.model;

/**
 * The gateway authorisation outcome as computed by the agent.
 *
 * @param request   The original authorisation request
 * @param outcome   The outcome, signalling success or failure
 * @param message   An optional message in case of failure
 */
public record GatewayAuthorisationOutcome(BackstageAuthorisationRequest request, GatewayAuthenticationOutcomeStatus outcome, String message) {

    public GatewayAuthorisationOutcome(BackstageAuthorisationRequest request, GatewayAuthenticationOutcomeStatus outcome) {
        this(request, outcome, null);
    }

}
