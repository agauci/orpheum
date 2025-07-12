package com.orpheum.orchestrator.backstage.heartbeat.service;

import com.orpheum.orchestrator.backstage.heartbeat.model.Heartbeat;
import com.orpheum.orchestrator.backstage.heartbeat.repository.HeartbeatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * A simple heartbeat service meant to both keep the free Supabase project alive (deleted after 1 week of inactivity),
 * as well as a simple external heartbeat evidence of the backstage portal.
 */
@Component
@Slf4j
public class HeartbeatService {

    @Autowired
    private HeartbeatRepository heartbeatRepository;

    @Scheduled(fixedRateString = "${backstage.heartbeat.period-ms}")
    @Transactional
    public void heartbeat() {
        log.trace("Heartbeat scheduled.");
        heartbeatRepository.deleteAll();
        heartbeatRepository.save(new Heartbeat());
    }

}
