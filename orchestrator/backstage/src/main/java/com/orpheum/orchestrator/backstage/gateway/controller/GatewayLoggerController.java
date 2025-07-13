package com.orpheum.orchestrator.backstage.gateway.controller;

import com.orpheum.orchestrator.backstage.gateway.model.LogEntry;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for receiving and processing log entries from gateways.
 * It receives a list of log entries and triggers logback logs that replicate the received parameters.
 * The site friendly name is added as an MDC attribute.
 */
@RestController
@RequestMapping("/gateway/logs")
@Slf4j
public class GatewayLoggerController {

    private static final String SITE_FRIENDLY_NAME_MDC_KEY = "siteFriendlyName";

    /**
     * Receives a list of log entries from a gateway and logs them using logback.
     * The site friendly name is added as an MDC attribute.
     *
     * @param logEntries The list of log entries to process
     * @return A response entity indicating success
     */
    @PostMapping
    public ResponseEntity<String> receiveLogs(@RequestBody List<LogEntry> logEntries) {
        log.debug("Received {} log entries from gateway", logEntries.size());

        for (LogEntry logEntry : logEntries) {
            processLogEntry(logEntry);
        }

        return ResponseEntity.ok("Logs processed successfully");
    }

    /**
     * Processes a single log entry by adding the site friendly name to MDC
     * and logging the message with the appropriate log level.
     *
     * @param logEntry The log entry to process
     */
    private void processLogEntry(LogEntry logEntry) {
        try {
            // Get a logger with the name from the log entry
            Logger logger = LoggerFactory.getLogger(logEntry.getLoggerName());
            
            // Add site friendly name to MDC
            MDC.put(SITE_FRIENDLY_NAME_MDC_KEY, logEntry.getSiteFriendlyName());
            
            // Log the message with the appropriate log level
            switch (logEntry.getLogLevel().toUpperCase()) {
                case "TRACE":
                    if (logger.isTraceEnabled()) {
                        logger.trace(logEntry.getMessage());
                    }
                    break;
                case "DEBUG":
                    if (logger.isDebugEnabled()) {
                        logger.debug(logEntry.getMessage());
                    }
                    break;
                case "INFO":
                    if (logger.isInfoEnabled()) {
                        logger.info(logEntry.getMessage());
                    }
                    break;
                case "WARN":
                    if (logger.isWarnEnabled()) {
                        logger.warn(logEntry.getMessage());
                    }
                    break;
                case "ERROR":
                    if (logger.isErrorEnabled()) {
                        logger.error(logEntry.getMessage());
                    }
                    break;
                default:
                    // Default to INFO level if the level is not recognized
                    if (logger.isInfoEnabled()) {
                        logger.info(logEntry.getMessage());
                    }
                    break;
            }
        } finally {
            // Always clear the MDC to prevent leaking context
            MDC.remove(SITE_FRIENDLY_NAME_MDC_KEY);
        }
    }
}