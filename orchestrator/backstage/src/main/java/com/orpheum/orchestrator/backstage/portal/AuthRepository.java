package com.orpheum.orchestrator.backstage.portal;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.orpheum.orchestrator.backstage.portal.model.BackstageAuthenticationRequest;
import com.orpheum.orchestrator.backstage.portal.model.GatewayAuthenticationOutcome;
import com.orpheum.orchestrator.backstage.portal.model.GatewayAuthenticationOutcomeStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.*;

import static com.orpheum.orchestrator.backstage.portal.model.BackstageAuthenticationRequestStatus.AUTHENTICATED;
import static com.orpheum.orchestrator.backstage.portal.model.BackstageAuthenticationRequestStatus.PRE_AUTH;
import static com.orpheum.orchestrator.backstage.portal.model.GatewayAuthenticationOutcomeStatus.FAILED;

@Component
@Slf4j
public final class AuthRepository {

    @Value("${backstage.portal.auth-thread-pool}")
    private Integer authThreadPoolSize;

    private final Cache<String, BackstageAuthenticationRequest> ongoingAuthentications = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
    private final Map<String, CompletableFuture<GatewayAuthenticationOutcome>> pendingAuthenticationRequests = new ConcurrentHashMap<>();
    private ExecutorService threadPool;

    public synchronized void add(BackstageAuthenticationRequest request) {
        if (Optional.ofNullable(ongoingAuthentications.getIfPresent(request.id()))
                .map(req -> PRE_AUTH.equals(req.status())).orElse(false)) {
            log.debug("Ignoring request since matching entry already found. [Request = {}, Ongoing authentications = {}]", request, ongoingAuthentications);
        } else {
            log.debug("Adding new authentication request {}. Current set: {}", request, ongoingAuthentications);

            ongoingAuthentications.put(request.id(), request);
        }
    }

    public List<BackstageAuthenticationRequest> getPendingAuthentications(String siteIdentifier) {
        final List<BackstageAuthenticationRequest> authenticatedRequests = ongoingAuthentications.asMap().values().stream()
                .filter(request -> AUTHENTICATED.equals(request.status()) && siteIdentifier.equals(request.siteIdentifier()))
                .toList();

        log.trace("Resolved the following authenticated requests for gateway authentication. [Requests: {}, Site Identifier: {}]", authenticatedRequests, siteIdentifier);

        return authenticatedRequests;
    }

    public CompletableFuture<GatewayAuthenticationOutcome> authoriseRequest(String macAddress, String accessPointMacAddress, String siteIdentifier) {
        BackstageAuthenticationRequest resolvedRequest = ongoingAuthentications.getIfPresent(BackstageAuthenticationRequest.id(macAddress, accessPointMacAddress, siteIdentifier));

        if (resolvedRequest == null || !PRE_AUTH.equals(resolvedRequest.status())) {
            log.warn("Unable to find pre authenticated backstage request, or found request not in expected state. [Request: {}]", resolvedRequest);

            return CompletableFuture.completedFuture(new GatewayAuthenticationOutcome(resolvedRequest, FAILED, "Missing pending backstage request"));
        }

        log.debug("Resolved the following pending authentication request. [Request: {}]", resolvedRequest);

        final BackstageAuthenticationRequest updatedRequest = new BackstageAuthenticationRequest(
                resolvedRequest.macAddress(),
                resolvedRequest.accessPointMacAddress(),
                resolvedRequest.siteIdentifier(),
                resolvedRequest.timestamp(),
                AUTHENTICATED
        );
        ongoingAuthentications.put(resolvedRequest.id(), updatedRequest);
        log.debug("Marked authentication request for gateway authentication. [Request: {}, Ongoing Authentications: {}]", updatedRequest, ongoingAuthentications);

        CompletableFuture<GatewayAuthenticationOutcome> pendingAuthRequest = new CompletableFuture<>();
        pendingAuthenticationRequests.put(resolvedRequest.id(), pendingAuthRequest);

        return pendingAuthRequest;
    }

    public void onAuthorizationOutcome(GatewayAuthenticationOutcome outcome) {
        BackstageAuthenticationRequest request = ongoingAuthentications.getIfPresent(outcome.request().id());

        if (request != null) {
            CompletableFuture<GatewayAuthenticationOutcome> pendingCompletableFuture = pendingAuthenticationRequests.get(request.id());

            if (pendingCompletableFuture != null) {
                log.debug("Resolved outcome notification. Pending HTML request to be notified of outcome. [Outcome: {}]", outcome);

                threadPool.execute(() -> {
                    // Signal to any pending HTTP request that the outcome has been received
                    pendingCompletableFuture.complete(outcome);
                    // Remove the ongoing authentication from the store
                    ongoingAuthentications.invalidate(request.id());
                    log.debug("Removed authentication request. [Request = {}, Set = {}]", request, ongoingAuthentications);
                });
            } else {
                log.debug("Unable to resolve corresponding completable future for outcome. Any associated pending authorization request will time out. [Outcome: {}]", outcome);
            }
        } else {
            log.warn("Unable to resolve corresponding backstage authentication request. Any associated pending authorization request will time out. [Outcome: {}]", outcome);
        }
    }

    @PostConstruct
    public void postConstruct() {
        threadPool = Executors.newFixedThreadPool(authThreadPoolSize);
    }

}
