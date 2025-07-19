package com.orpheum.orchestrator.backstage.heartbeat.service;

import com.orpheum.orchestrator.backstage.heartbeat.model.Heartbeat;
import com.orpheum.orchestrator.backstage.heartbeat.model.HeartbeatType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class DatabaseHeartbeatService {

    @Autowired
    private HeartbeatService heartbeatService;

    @Scheduled(fixedRateString = "${backstage.heartbeat.db-period-ms}")
    @Transactional
    public void heartbeat() {
        log.trace("Heartbeat scheduled.");
        heartbeatService.refresh(HeartbeatType.DATABASE, HeartbeatType.DATABASE.name().toLowerCase());
    }
}
