package com.orpheum.orchestrator.unifiAgent.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * A UniFi gateway admin authenticated connection used to perform actions on the gateway's API.
 *
 * @param cookie The authentication cookie string
 * @param csrfToken The CSRF token for this connection
 * @param lastAuthenticatedTime The timestamp when this connection was last authenticated
 * @param username The username used for authentication
 * @param password The password used for authentication
 */
public record GatewayAuthConnection(
        String cookie,
        String csrfToken,
        Instant lastAuthenticatedTime,
        String username,
        String password,
        Integer failureCount
) {
    /**
     * Constructs a new GatewayAuthConnection with the provided authentication details.
     *
     * @param username The username used for authentication
     * @param password The password used for authentication
     */
    public GatewayAuthConnection(String username, String password) {
        this(null, null, Instant.MIN, username, password, 0);
    }

    public boolean isNew() {
        return cookie == null;
    }

    /**
     * Checks if this connection has expired based on the expiry threshold.
     *
     * @return true if the connection has expired, false otherwise
     */
    public boolean isExpired(Long expiryMilliseconds) {
        return lastAuthenticatedTime.plus(expiryMilliseconds, ChronoUnit.MILLIS)
                .isBefore(Instant.now());
    }

    /**
     * Creates a new AuthConnection with updated authentication details.
     *
     * @param cookie The new cookie string
     * @param csrfToken The new CSRF token
     * @return A new AuthConnection with updated authentication details
     */
    public GatewayAuthConnection refresh(String cookie, String csrfToken) {
        return new GatewayAuthConnection(cookie, csrfToken, Instant.now(), username, password, 0);
    }

    public GatewayAuthConnection failed(GatewayAuthConnection connection) {
        return new GatewayAuthConnection(null, null, Instant.MIN, connection.username, connection.password, connection.failureCount + 1);
    }
}
