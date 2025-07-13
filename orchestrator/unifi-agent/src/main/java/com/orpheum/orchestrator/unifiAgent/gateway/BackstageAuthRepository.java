package com.orpheum.orchestrator.unifiAgent.gateway;

import com.orpheum.orchestrator.unifiAgent.model.BackstageAuthorisationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An in-memory repository which keep strack of pending backstage authorisation requests being currently worked on. This
 * is necessary since the polling rate on backstage to pull pending authorisations is faster than the time taken to
 * authorise a single device with the gateway. This repository ensures an idempotent processing of pending backstage
 * authorisation requests, discarding polled requests which are currently being worked on.
 */
public final class BackstageAuthRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackstageAuthRepository.class);

    private static final Set<BackstageAuthorisationRequest> ONGOING_AUTHORISATIONS = Collections.synchronizedSet(new HashSet<>());

    public static List<BackstageAuthorisationRequest> merge(List<BackstageAuthorisationRequest> backstageAuthorisationRequests) {
        LOGGER.trace("Received pending gateway authorisation requests {}. Current set: {}", backstageAuthorisationRequests, ONGOING_AUTHORISATIONS);

        Map<Boolean, List<BackstageAuthorisationRequest>> duplicateEntriesPartition = backstageAuthorisationRequests.stream()
                .collect(Collectors.partitioningBy(ONGOING_AUTHORISATIONS::contains));
        final List<BackstageAuthorisationRequest> duplicateEntries = duplicateEntriesPartition.get(true);
        if (!duplicateEntries.isEmpty()) {
            LOGGER.trace("Resolved duplicate entries {}", duplicateEntries);
        }

        List<BackstageAuthorisationRequest> newEntries = duplicateEntriesPartition.get(false);
        if (newEntries.size() != 0) {
            LOGGER.debug("Adding new entries {}", newEntries);
            ONGOING_AUTHORISATIONS.addAll(newEntries);
        }

        return newEntries;
    }

    public static void onGatewayAuthorisationCompleted(BackstageAuthorisationRequest backstageAuthorisationRequest) {
        LOGGER.debug("Backstage authorisation request {} completed and removed from repository.", backstageAuthorisationRequest);

        ONGOING_AUTHORISATIONS.remove(backstageAuthorisationRequest);
    }

}
