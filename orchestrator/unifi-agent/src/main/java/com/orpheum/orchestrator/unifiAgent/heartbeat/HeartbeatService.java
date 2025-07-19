package com.orpheum.orchestrator.unifiAgent.heartbeat;

import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import com.orpheum.orchestrator.unifiAgent.support.BackstageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatService.class);

    private static final String HEARTBEAT_TYPE = "GATEWAY";
    private static final String SITE_FRIENDLY_NAME = ApplicationProperties.getString("site_friendly_name");

    public static void start(final ScheduledExecutorService service) {
        service.scheduleAtFixedRate(
                () -> {
                    try {
                        BackstageClient.sendHeartbeat(HEARTBEAT_TYPE, SITE_FRIENDLY_NAME);
                    } catch (Exception e) {
                        LOGGER.error("Heartbeat service encountered exception. Skipping run.", e);
                    }
                },
                30000L, // Wait 30 seconds before the first sync request
                ApplicationProperties.getInteger("gateway_heartbeat_delay_ms"),
                TimeUnit.MILLISECONDS
        );
    }

}
