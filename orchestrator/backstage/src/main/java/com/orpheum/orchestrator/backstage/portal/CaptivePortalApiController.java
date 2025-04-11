package com.orpheum.orchestrator.backstage.portal;

import com.orpheum.orchestrator.backstage.portal.model.BackstageAuthenticationRequest;
import com.orpheum.orchestrator.backstage.portal.model.GatewayAuthenticationOutcome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CaptivePortalApiController {

    @Autowired
    AuthRepository authRepository;

    @Value("${backstage.portal.api-tokens}")
    private List<String> apiTokens;

    @GetMapping("/portal")
    public ResponseEntity<List<BackstageAuthenticationRequest>> list(@RequestHeader("X-Auth-Token") String authToken,
                                                                     @RequestParam(name= "site_identifier") String siteIdentifier) {
        log.trace("Received gateway list request. [Site Identifier: {}]", siteIdentifier);
        if (!apiTokens.contains(authToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        final List<BackstageAuthenticationRequest> resolvedAuthentications = authRepository.getPendingAuthentications(siteIdentifier);
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
