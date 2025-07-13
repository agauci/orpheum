package com.orpheum.orchestrator.backstage.gateway.controller;

import com.orpheum.orchestrator.backstage.gateway.model.LogEntry;
import com.orpheum.orchestrator.backstage.gateway.service.GatewayLogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for receiving and processing log entries from gateways.
 * It receives a list of log entries and triggers logback logs that replicate the received parameters.
 */
@RestController
@Slf4j
public class GatewayLoggerController {

    @Autowired
    private GatewayLogService gatewayLogService;

    /**
     * Receives a list of log entries from a gateway and logs them using logback.
     *
     * @param logEntries The list of log entries to process
     * @return A response entity indicating success
     */
    @PostMapping("/gateway/logs")
    public ResponseEntity<String> receiveLogs(@RequestHeader("X-Auth-Token") String authToken,
                                              @RequestBody List<LogEntry> logEntries) {
        log.debug("Received {} log entries from gateway", logEntries.size());

        for (LogEntry logEntry : logEntries) {
            gatewayLogService.processLogEntry(authToken, logEntry);
        }

        return ResponseEntity.ok("");
    }

}