package com.orpheum.orchestrator.backstage.portal.controller;

import com.orpheum.orchestrator.backstage.portal.model.auth.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.backstage.portal.model.auth.GatewayAuthorisationOutcome;
import com.orpheum.orchestrator.backstage.portal.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller implements the API integration with UniFi Orpheum agents across multiple sites. It exposes
 *
 * <ul>
 *     <li>An endpoint used by agents to retrieve any pending authorisation requests for their site.</li>
 *     <li>A callback webhook which signals the agent's outcome of a pending backstage authorisation. This webhook
 *     in turn completes a pending /authorise API call, therefore reporting the outcome back to the user.</li>
 * </ul>
 *
 */
@RestController
@Slf4j
public class CaptivePortalApiController {

    @Autowired
    AuthService authService;

    @GetMapping("/portal")
    public ResponseEntity<List<BackstageAuthorisationRequest>> list(@RequestHeader("X-Auth-Token") String authToken,
                                                                    @RequestParam(name= "site_identifier") String siteIdentifier) {
        log.trace("Received gateway list request. [Site Identifier: {}]", siteIdentifier);

        return ResponseEntity.ok(authService.getPendingAuthorisations(siteIdentifier, authToken));
    }

    @PostMapping("/portal")
    public ResponseEntity<String> postOutcome(@RequestHeader("X-Auth-Token") String authToken,
                                              @RequestBody GatewayAuthorisationOutcome outcome) {
        log.debug("Received gateway authorization outcome notification. [Outcome: {}]", outcome);

        authService.onAuthorizationOutcome(outcome, authToken);
        return ResponseEntity.ok("");
    }

}
