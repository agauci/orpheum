package com.orpheum.orchestrator.backstage.logging.model;

import lombok.Data;

import java.time.Instant;

/**
 * Represents a log entry received from a gateway.
 * Contains the message, timestamp, log level, logger name, site friendly name, and stacktrace if available.
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

    /**
     * The stacktrace as a string, if a throwable was included in the log event.
     * Will be null if no throwable was present.
     */
    private String stacktrace;
}
