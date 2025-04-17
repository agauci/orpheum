package com.orpheum.orchestrator.backstage.portal;

import com.orpheum.orchestrator.backstage.portal.model.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.backstage.portal.model.GatewayAuthenticationOutcome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
 *
 */
@RestController
@Slf4j
public class CaptivePortalApiController {

    @Autowired
    AuthRepository authRepository;

    @Value("${backstage.portal.api-tokens}")
    private List<String> apiTokens;

    @GetMapping("/portal")
    public ResponseEntity<List<BackstageAuthorisationRequest>> list(@RequestHeader("X-Auth-Token") String authToken,
                                                                    @RequestParam(name= "site_identifier") String siteIdentifier) {
        log.trace("Received gateway list request. [Site Identifier: {}]", siteIdentifier);
        if (!apiTokens.contains(authToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        final List<BackstageAuthorisationRequest> resolvedAuthentications = authRepository.getPendingAuthentications(siteIdentifier);
        log.trace("Resolved {} pending authentication requests. [Requests: {}]", resolvedAuthentications.size(), resolvedAuthentications);

        return ResponseEntity.ok(resolvedAuthentications);
    }

    @PostMapping("/portal")
    public ResponseEntity<String> postOutcome(@RequestHeader("X-Auth-Token") String authToken,
                                              @RequestBody GatewayAuthenticationOutcome outcome) {
        log.debug("Received gateway authorization outcome notification. [Outcome: {}]", outcome);
        if (!apiTokens.contains(authToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        authRepository.onAuthorizationOutcome(outcome);

        return ResponseEntity.ok("");
    }

}
