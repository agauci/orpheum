package com.orpheum.orchestrator.backstage.portal.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.orpheum.orchestrator.backstage.portal.model.auth.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.backstage.portal.model.auth.GatewayAuthenticationOutcome;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

import static com.orpheum.orchestrator.backstage.portal.model.auth.BackstageAuthenticationRequestStatus.AUTHENTICATED;
import static com.orpheum.orchestrator.backstage.portal.model.auth.BackstageAuthenticationRequestStatus.PRE_AUTH;
import static com.orpheum.orchestrator.backstage.portal.model.auth.GatewayAuthenticationOutcomeStatus.FAILED;

@Component
@Slf4j
public final class AuthRepository {

    @Value("${backstage.portal.auth-thread-pool}")
    private Integer authThreadPoolSize;

    private final Cache<String, BackstageAuthorisationRequest> ongoingAuthorisations = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
    private final Map<String, CompletableFuture<GatewayAuthenticationOutcome>> pendingAuthenticationRequests = new ConcurrentHashMap<>();
    private ExecutorService threadPool;

    public synchronized void add(BackstageAuthorisationRequest request) {
        if (Optional.ofNullable(ongoingAuthorisations.getIfPresent(request.id()))
                .map(req -> PRE_AUTH.equals(req.status())).orElse(false)) {
            log.debug("Ignoring request since matching entry already found. [Request = {}, Ongoing authentications = {}]", request, ongoingAuthorisations);
        } else {
            log.debug("Adding new authentication request {}. Current set: {}", request, ongoingAuthorisations);

            ongoingAuthorisations.put(request.id(), request);
        }
    }

    public List<BackstageAuthorisationRequest> getPendingAuthentications(String siteIdentifier) {
        final List<BackstageAuthorisationRequest> authenticatedRequests = ongoingAuthorisations.asMap().values().stream()
                .filter(request -> AUTHENTICATED.equals(request.status()) && siteIdentifier.equals(request.siteIdentifier()))
                .toList();

        log.trace("Resolved the following authenticated requests for gateway authentication. [Requests: {}, Site Identifier: {}]", authenticatedRequests, siteIdentifier);

        return authenticatedRequests;
    }

    public CompletableFuture<GatewayAuthenticationOutcome> authoriseRequest(String macAddress, String accessPointMacAddress, String siteIdentifier, String ip) {
        BackstageAuthorisationRequest resolvedRequest = ongoingAuthorisations.getIfPresent(BackstageAuthorisationRequest.id(macAddress, accessPointMacAddress, ip, siteIdentifier));

        if (resolvedRequest == null || !PRE_AUTH.equals(resolvedRequest.status())) {
            log.warn("Unable to find pre authenticated backstage request, or found request not in expected state. [Request: {}]", resolvedRequest);

            return CompletableFuture.completedFuture(new GatewayAuthenticationOutcome(resolvedRequest, FAILED, "Missing pending backstage request"));
        }

        log.debug("Resolved the following pending authentication request. [Request: {}]", resolvedRequest);

        final BackstageAuthorisationRequest updatedRequest = new BackstageAuthorisationRequest(
                resolvedRequest.macAddress(),
                resolvedRequest.accessPointMacAddress(),
                resolvedRequest.siteIdentifier(),
                resolvedRequest.ip(),
                resolvedRequest.timestamp(),
                AUTHENTICATED
        );
        ongoingAuthorisations.put(resolvedRequest.id(), updatedRequest);
        log.debug("Marked authentication request for gateway authentication. [Request: {}, Ongoing Authentications: {}]", updatedRequest, ongoingAuthorisations);

        CompletableFuture<GatewayAuthenticationOutcome> pendingAuthRequest = new CompletableFuture<>();
        pendingAuthenticationRequests.put(resolvedRequest.id(), pendingAuthRequest);

        return pendingAuthRequest;
    }

    public void onAuthorizationOutcome(GatewayAuthenticationOutcome outcome) {
        BackstageAuthorisationRequest request = ongoingAuthorisations.getIfPresent(outcome.request().id());

        if (request != null) {
            CompletableFuture<GatewayAuthenticationOutcome> pendingCompletableFuture = pendingAuthenticationRequests.get(request.id());

            if (pendingCompletableFuture != null) {
                log.debug("Resolved outcome notification. Pending HTML request to be notified of outcome. [Outcome: {}]", outcome);

                threadPool.execute(() -> {
                    // Signal to any pending HTTP request that the outcome has been received
                    pendingCompletableFuture.complete(outcome);
                    // Remove the ongoing authentication from the store
                    ongoingAuthorisations.invalidate(request.id());
                    log.debug("Removed authentication request. [Request = {}, Set = {}]", request, ongoingAuthorisations);
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
