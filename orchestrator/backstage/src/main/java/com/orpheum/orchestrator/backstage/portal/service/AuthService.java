package com.orpheum.orchestrator.backstage.portal.service;

import com.orpheum.orchestrator.backstage.portal.exception.AuthTokenNotFoundException;
import com.orpheum.orchestrator.backstage.portal.model.auth.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.backstage.portal.model.auth.GatewayAuthenticationOutcome;
import com.orpheum.orchestrator.backstage.portal.repository.AuthRepository;
import com.orpheum.orchestrator.backstage.portal.repository.UserDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.orpheum.orchestrator.backstage.portal.model.auth.BackstageAuthenticationRequestStatus.PRE_AUTH;

@Slf4j
@Component
public class AuthService {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    UserDataRepository userDataRepository;

    @Value("${backstage.portal.api-tokens}")
    private List<String> apiTokens;

    public void startAuthorisation(String macAddress, String accessPointMacAddress, Long timestamp, String ip, String siteIdentifier) {
        authRepository.add(new BackstageAuthorisationRequest(macAddress, accessPointMacAddress, siteIdentifier, ip, timestamp, PRE_AUTH));
    }

    public List<BackstageAuthorisationRequest> getPendingAuthorisations(String siteIdentifier, String authToken) {
        validateAuthToken(authToken);

        final List<BackstageAuthorisationRequest> resolvedAuthentications = authRepository.getPendingAuthentications(siteIdentifier);
        log.trace("Resolved {} pending authentication requests. [Requests: {}]", resolvedAuthentications.size(), resolvedAuthentications);

        return resolvedAuthentications;
    }

    public void onAuthorizationOutcome(GatewayAuthenticationOutcome outcome, String authToken) {
        validateAuthToken(authToken);
        authRepository.onAuthorizationOutcome(outcome);
    }


    private void validateAuthToken(String authToken) {
        if (!apiTokens.contains(authToken)) {
            throw new AuthTokenNotFoundException(authToken);
        }
    }

}
