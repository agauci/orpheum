package com.orpheum.orchestrator.backstage.heartbeat.service;

import com.orpheum.orchestrator.backstage.heartbeat.model.Heartbeat;
import com.orpheum.orchestrator.backstage.heartbeat.model.HeartbeatType;
import com.orpheum.orchestrator.backstage.heartbeat.model.HeartbeatVerifierConfig;
import com.orpheum.orchestrator.backstage.heartbeat.repository.HeartbeatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple heartbeat service meant to both keep the free Supabase project alive (deleted after 1 week of inactivity),
 * as well as a simple external heartbeat evidence of the backstage portal.
 */
@Component
@Slf4j
public class HeartbeatService {

    // Map to track the last warning time for each heartbeat (type + identifier)
    private final Map<String, Long> lastWarningTimeMap = new ConcurrentHashMap<>();

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Autowired
    private HeartbeatVerifierConfig heartbeatVerifierConfig;

    @Transactional
    public void refresh(HeartbeatType heartbeatType, String identifier) {
        heartbeatRepository.deleteHeartbeatByTypeAndIdentifier(heartbeatType, identifier);
        heartbeatRepository.save(new Heartbeat(null, heartbeatType, identifier, LocalDateTime.now(), true));
    }

    @Scheduled(fixedRateString = "#{heartbeatVerifierConfig.periodMs}")
    public void verifyHeartbeats() {
        log.trace("Verifying heartbeat records.");
        long currentTimeMs = System.currentTimeMillis();

        for (Heartbeat heartbeat : heartbeatRepository.findByActive(true)) {
            if ((currentTimeMs - extractTimestampMs(heartbeat)) > heartbeatVerifierConfig.getVerifier().get(heartbeat.getType()).getToleranceMs() ) {
                String heartbeatKey = getHeartbeatKey(heartbeat);
                Long lastWarningTime = lastWarningTimeMap.get(heartbeatKey);

                // Log warning if this is the first time or if WARNING_INTERVAL_MS has passed since the last warning
                if (lastWarningTime == null || (currentTimeMs - lastWarningTime) >= heartbeatVerifierConfig.getWarningIntervalMs()) {
                    log.warn("Heartbeat record expired. [Heartbeat: {}]", heartbeat);
                    lastWarningTimeMap.put(heartbeatKey, currentTimeMs);
                }
            } else {
                log.trace("Heartbeat record is still valid. [Heartbeat: {}]", heartbeat);
            }
        }
    }

    /**
     * Creates a unique key for a heartbeat based on its type and identifier.
     */
    private String getHeartbeatKey(Heartbeat heartbeat) {
        return heartbeat.getType() + ":" + heartbeat.getIdentifier();
    }

    private Long extractTimestampMs(Heartbeat heartbeat) {
        return heartbeat.getTimestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
