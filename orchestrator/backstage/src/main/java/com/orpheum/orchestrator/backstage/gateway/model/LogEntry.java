package com.orpheum.orchestrator.backstage.gateway.model;

import lombok.Data;

import java.time.Instant;

/**
 * Represents a log entry received from a gateway.
 * Contains the message, timestamp, log level, logger name, and site friendly name.
 */
@Data
public class LogEntry {
    /**
     * The log message.
     */
    private String message;
    
    /**
     * The timestamp when the log was generated.
     */
    private Instant timestamp;
    
    /**
     * The log level (e.g., INFO, WARN, ERROR, DEBUG, TRACE).
     */
    private String logLevel;
    
    /**
     * The name of the logger that generated the log.
     */
    private String loggerName;
    
    /**
     * The friendly name of the site where the log was generated.
     */
    private String siteFriendlyName;
}