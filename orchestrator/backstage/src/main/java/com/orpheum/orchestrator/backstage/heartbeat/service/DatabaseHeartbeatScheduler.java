package com.orpheum.orchestrator.backstage.heartbeat.service;

import com.orpheum.orchestrator.backstage.heartbeat.model.HeartbeatType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DatabaseHeartbeatScheduler {

    @Autowired
    private HeartbeatService heartbeatService;

    @Scheduled(fixedRateString = "${backstage.heartbeat.database.period-ms}", initialDelay = 10000)
    public void heartbeat() {
        heartbeatService.refresh(HeartbeatType.DATABASE, HeartbeatType.DATABASE.name().toLowerCase());
        log.debug("Database heartbeat triggered.");
    }
}
