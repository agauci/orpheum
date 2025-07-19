package com.orpheum.orchestrator.backstage.heartbeat.controller;

import com.orpheum.orchestrator.backstage.heartbeat.model.HeartbeatType;
import com.orpheum.orchestrator.backstage.heartbeat.service.HeartbeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for receiving and processing heartbeat signals.
 * It accepts heartbeat type and identifier and refreshes the heartbeat record.
 */
@RestController
@Slf4j
public class HeartbeatController {

    @Autowired
    private HeartbeatService heartbeatService;

    /**
     * Refreshes a heartbeat record with the given type and identifier.
     *
     * @param heartbeatType The type of heartbeat (DATABASE, GATEWAY)
     * @param identifier The identifier for the heartbeat
     * @return A response entity indicating success
     */
    @PostMapping("/heartbeat/refresh")
    public ResponseEntity<String> refreshHeartbeat(
            @RequestParam("type") HeartbeatType heartbeatType,
            @RequestParam("identifier") String identifier) {
        
        log.debug("Refreshing heartbeat. [Type: {}, Identifier: {}]", heartbeatType, identifier);
        heartbeatService.refresh(heartbeatType, identifier);
        
        return ResponseEntity.ok("Heartbeat refreshed successfully");
    }
}