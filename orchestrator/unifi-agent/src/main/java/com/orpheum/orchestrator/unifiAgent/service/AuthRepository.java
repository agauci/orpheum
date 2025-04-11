package com.orpheum.orchestrator.unifiAgent.service;

import com.orpheum.orchestrator.unifiAgent.model.BackstageAuthenticationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public final class AuthRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRepository.class);

    private static final Set<BackstageAuthenticationRequest> ongoingAuthentications = Collections.synchronizedSet(new HashSet<>());

    public static List<BackstageAuthenticationRequest> merge(List<BackstageAuthenticationRequest> backstageAuthenticationRequests) {
        LOGGER.debug("Received pending gateway authenticated requests {}. Current set: {}", backstageAuthenticationRequests, ongoingAuthentications);

        Map<Boolean, List<BackstageAuthenticationRequest>> duplicateEntriesPartition = backstageAuthenticationRequests.stream()
                .collect(Collectors.partitioningBy(ongoingAuthentications::contains));
        final List<BackstageAuthenticationRequest> duplicateEntries = duplicateEntriesPartition.get(true);
        if (!duplicateEntries.isEmpty()) {
            LOGGER.trace("Resolved duplicate entries {}", duplicateEntries);
        }

        List<BackstageAuthenticationRequest> newEntries = duplicateEntriesPartition.get(false);
        if (newEntries.size() != 0) {
            LOGGER.debug("Adding new entries {}", newEntries);
            ongoingAuthentications.addAll(newEntries);
        }

        return newEntries;
    }

    public static void onGatewayAuthenticationCompleted(BackstageAuthenticationRequest backstageAuthenticationRequest) {
        LOGGER.debug("Backstage authentication request {} completed and removed from repository.", backstageAuthenticationRequest);

        ongoingAuthentications.remove(backstageAuthenticationRequest);
    }

}
