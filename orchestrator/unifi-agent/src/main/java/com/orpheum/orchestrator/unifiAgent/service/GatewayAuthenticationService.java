package com.orpheum.orchestrator.unifiAgent.service;

import com.orpheum.orchestrator.unifiAgent.model.BackstageAuthenticationRequest;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import com.orpheum.orchestrator.unifiAgent.support.BackstageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GatewayAuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayAuthenticationService.class);
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(ApplicationProperties.getInteger("thread_pool_size"));

    public static void start() {
        LOGGER.info("Started polling of backstage authenticated requests.");
        while (true) {
            try {
                Thread.sleep(ApplicationProperties.getLong("poll_delay_ms"));

                List<BackstageAuthenticationRequest> retrievedPendingAuths = BackstageClient.getPendingAuthenticatedRequests();

                if (retrievedPendingAuths.size() != 0) {
                    // Check if any of the retrieved pending authorisation requests have already been actioned on. Those which
                    // are currently being worked upon should be ignored.
                    final List<BackstageAuthenticationRequest> newPendingAuths = AuthRepository.merge(retrievedPendingAuths);

                    if (newPendingAuths.size() != 0) {
                        newPendingAuths.forEach(pendingAuth -> THREAD_POOL.execute(new GatewayAuthenticationRunnable(pendingAuth)));
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Gateway authentication service encountered exception. Skipping run.", e);
            }
        }
    }

}
