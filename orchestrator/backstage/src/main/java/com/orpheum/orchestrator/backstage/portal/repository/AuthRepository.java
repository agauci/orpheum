package com.orpheum.orchestrator.backstage.portal.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.orpheum.orchestrator.backstage.portal.model.auth.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.backstage.portal.model.auth.GatewayAuthorisationOutcome;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@Component
@Slf4j
public final class AuthRepository {

    @Value("${backstage.portal.auth-thread-pool}")
    private Integer authThreadPoolSize;

    private final Cache<String, PendingAuthorisationData> ongoingAuthorisations = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();
    private ExecutorService threadPool;

    public List<BackstageAuthorisationRequest> getPendingAuthorisations(String siteIdentifier) {
        final List<BackstageAuthorisationRequest> authorisedRequests = ongoingAuthorisations.asMap().values().stream()
                .map(PendingAuthorisationData::pendingRequest)
                .filter(request -> siteIdentifier.equals(request.siteIdentifier()))
                .toList();

        log.trace("Resolved the following authorised requests for gateway authentication. [Requests: {}, Site Identifier: {}]", authorisedRequests, siteIdentifier);

        return authorisedRequests;
    }

    public synchronized CompletableFuture<GatewayAuthorisationOutcome> startAuthorisation(String macAddress, String accessPointMacAddress, String siteIdentifier, String ip, Long timestamp) {
        BackstageAuthorisationRequest pendingAuthRequest = new BackstageAuthorisationRequest(macAddress, accessPointMacAddress, siteIdentifier, ip, timestamp);

        CompletableFuture<GatewayAuthorisationOutcome> pendingRequestOutcome = new CompletableFuture<>();
        ongoingAuthorisations.put(pendingAuthRequest.id(), new PendingAuthorisationData(pendingAuthRequest, pendingRequestOutcome));

        return pendingRequestOutcome;
    }

    public synchronized void onAuthorizationOutcome(GatewayAuthorisationOutcome outcome) {
        PendingAuthorisationData request = ongoingAuthorisations.getIfPresent(outcome.request().id());

        if (request != null) {
            CompletableFuture<GatewayAuthorisationOutcome> pendingCompletableFuture = request.pendingCompletableFuture;

            if (pendingCompletableFuture != null) {
                log.debug("Resolved outcome notification. Pending HTML request to be notified of outcome. [Outcome: {}]", outcome);

                threadPool.execute(() -> {
                    // Signal to any pending HTTP request that the outcome has been received
                    pendingCompletableFuture.complete(outcome);
                    // Remove the ongoing authorisation from the store
                    ongoingAuthorisations.invalidate(request.pendingRequest.id());
                    log.debug("Removed authorisation request. [Request = {}, Set = {}]", request, ongoingAuthorisations);
                });
            } else {
                log.debug("Unable to resolve corresponding completable future for outcome. Any associated pending authorization request will time out. [Outcome: {}]", outcome);
            }
        } else {
            log.warn("Unable to resolve corresponding backstage authorisation request. Any associated pending authorization request will time out. [Outcome: {}]", outcome);
        }
    }

    @PostConstruct
    public void postConstruct() {
        threadPool = Executors.newFixedThreadPool(authThreadPoolSize);
    }

    private record PendingAuthorisationData(BackstageAuthorisationRequest pendingRequest, CompletableFuture<GatewayAuthorisationOutcome> pendingCompletableFuture) {}

}
