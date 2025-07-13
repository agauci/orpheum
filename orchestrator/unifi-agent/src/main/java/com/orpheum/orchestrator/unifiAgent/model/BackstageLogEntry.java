package com.orpheum.orchestrator.unifiAgent.model;

import java.time.Instant;

/**
 * Represents a log entry to be sent to the backstage server.
 * Contains the message, timestamp, log level, logger name, site friendly name, and stacktrace if available.
 */
public record BackstageLogEntry (
    /**
     * The log message.
     */
    String message,

    /**
     * The timestamp when the log was generated.
     */
    Long timestamp,

    /**
     * The log level (e.g., INFO, WARN, ERROR, DEBUG, TRACE).
     */
    String logLevel,

    /**
     * The name of the logger that generated the log.
     */
    String loggerName,

    /**
     * The friendly name of the site where the log was generated.
     */
    String siteFriendlyName,

    /**
     * The stacktrace as a string, if a throwable was included in the log event.
     * Will be null if no throwable was present.
     */
    String stacktrace
) { }
