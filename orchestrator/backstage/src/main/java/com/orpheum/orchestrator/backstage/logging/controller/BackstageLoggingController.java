package com.orpheum.orchestrator.backstage.logging.controller;

import com.orpheum.orchestrator.backstage.logging.model.LogEntry;
import com.orpheum.orchestrator.backstage.logging.service.BackstageLoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for receiving and processing log entries from third parties, namely gateways.
 * It receives a list of log entries and triggers logback logs that replicate the received parameters.
 */
@RestController
@Slf4j
public class BackstageLoggingController {

    @Autowired
    private BackstageLoggingService backstageLoggingService;

    /**
     * Receives a list of log entries from a gateway and logs them using logback.
     *
     * @param logEntries The list of log entries to process
     * @return A response entity indicating success
     */
    @PostMapping("/gateway/logs")
    public ResponseEntity<String> receiveLogs(@RequestHeader("X-Auth-Token") String authToken,
                                              @RequestBody List<LogEntry> logEntries) {
        for (LogEntry logEntry : logEntries) {
            backstageLoggingService.processLogEntry(authToken, logEntry);
        }

        return ResponseEntity.ok("");
    }

}