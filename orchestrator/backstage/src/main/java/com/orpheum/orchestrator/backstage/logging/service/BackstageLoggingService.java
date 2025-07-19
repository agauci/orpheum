package com.orpheum.orchestrator.backstage.logging.service;

import com.orpheum.orchestrator.backstage.logging.model.LogEntry;
import com.orpheum.orchestrator.backstage.portal.exception.AuthTokenNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackstageLoggingService {

    private static final String SITE_FRIENDLY_NAME_MDC_KEY = "siteFriendlyName";
    private static final String UNRECOGNISED_LEVEL_MDC_KEY = "unrecognisedLevel";

    @Value("${backstage.portal.api-tokens}")
    private List<String> apiTokens;

    /**
     * Processes a single log entry by adding the site-friendly name to MDC
     * and logging the message with the appropriate log level.
     *
     * @param apiToken The received API token
     * @param logEntry The log entry to process
     */
    public void processLogEntry(String apiToken, LogEntry logEntry) {
        validateAuthToken(apiToken);

        try {
            // Get a logger with the name from the log entry
            Logger logger = LoggerFactory.getLogger(logEntry.getLoggerName());

            // Add site-friendly name to MDC
            String siteFriendlyName = logEntry.getSiteFriendlyName();
            MDC.put(SITE_FRIENDLY_NAME_MDC_KEY, siteFriendlyName);

            // Log the message with the appropriate log level
            switch (logEntry.getLogLevel().toUpperCase()) {
                case "TRACE":
                    if (logger.isTraceEnabled()) {
                        logger.trace(enrichMessage(siteFriendlyName, logEntry.getMessage(), logEntry.getStacktrace()));
                    }
                    break;
                case "DEBUG":
                    if (logger.isDebugEnabled()) {
                        logger.debug(enrichMessage(siteFriendlyName, logEntry.getMessage(), logEntry.getStacktrace()));
                    }
                    break;
                case "INFO":
                    if (logger.isInfoEnabled()) {
                        logger.info(enrichMessage(siteFriendlyName, logEntry.getMessage(), logEntry.getStacktrace()));
                    }
                    break;
                case "WARN":
                    if (logger.isWarnEnabled()) {
                        logger.warn(enrichMessage(siteFriendlyName, logEntry.getMessage(), logEntry.getStacktrace()));
                    }
                    break;
                case "ERROR":
                    if (logger.isErrorEnabled()) {
                        logger.error(enrichMessage(siteFriendlyName, logEntry.getMessage(), logEntry.getStacktrace()));
                    }
                    break;
                default:
                    // Default to WARN level if the level is not recognized
                    if (logger.isWarnEnabled()) {
                        MDC.put(UNRECOGNISED_LEVEL_MDC_KEY, logEntry.getLogLevel().toUpperCase());
                        logger.warn(enrichMessage(siteFriendlyName, logEntry.getMessage(), logEntry.getStacktrace()));
                    }
                    break;
            }
        } finally {
            // Always clear the MDC to prevent leaking context
            MDC.clear();
        }
    }

    private String enrichMessage(final String siteFriendlyName, final String message, final String stacktrace) {
        String enrichedMessage = "[" + siteFriendlyName + "] " + message;

        if (stacktrace != null && !stacktrace.isEmpty()) {
            enrichedMessage = enrichedMessage + "\n" + stacktrace;
        }

        return enrichedMessage;
    }

    private void validateAuthToken(String authToken) {
        if (!apiTokens.contains(authToken)) {
            throw new AuthTokenNotFoundException(authToken);
        }
    }
}
