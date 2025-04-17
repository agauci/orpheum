package com.orpheum.orchestrator.unifiAgent.auth;

import com.orpheum.orchestrator.unifiAgent.auth.GatewayAuthConnectionManager.AuthCredentials;
import com.orpheum.orchestrator.unifiAgent.model.GatewayAuthConnection;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import com.orpheum.orchestrator.unifiAgent.support.UnifiGatewayClient;
import com.orpheum.orchestrator.unifiAgent.support.UnifiGatewayClient.UnifiGatewayLoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Periodically refreshes expired gateway authentication connections.
 */
class GatewayAuthConnectionRefresherRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayAuthConnectionRefresherRunnable.class);

    private static final Integer FAILURE_COUNT_LIMIT = 3;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private final Random random = new Random();
    private final Long gatewayMinAuthExpiry;
    private final Long gatewayMaxAuthExpiry;

    GatewayAuthConnectionRefresherRunnable() {
        gatewayMinAuthExpiry = ApplicationProperties.getLong("gateway_auth_connection_timeout_min_ms");
        gatewayMaxAuthExpiry = ApplicationProperties.getLong("gateway_auth_connection_timeout_max_ms");
    }

    @Override
    public void run() {
        if (isRunning.get()) {
            LOGGER.debug("Gateway auth connection refresher is already running. Skipping run.");
            return;
        }

        try {
            isRunning.set(true);
            refreshExpiredConnections();
        } catch (Exception e) {
            LOGGER.error("Error encountered while refreshing connections. Skipping run.", e);
        } finally {
            isRunning.set(false);
        }
    }

    void initialiseConnections(List<AuthCredentials> authCredentials) {
        executeSingleRun(
            authCredentials.stream()
                    .map(cred -> new GatewayAuthConnection(cred.username(), cred.password()))
                    .collect(Collectors.toList()),
            GatewayAuthConnectionManager.getConnectionPool()
        );
    }

    /**
     * Goes through the connection pool and refreshes any expired connections.
     */
    private void refreshExpiredConnections() {
        List<GatewayAuthConnection> connections = new ArrayList<>();
        BlockingQueue<GatewayAuthConnection> connectionPool = GatewayAuthConnectionManager.getConnectionPool();
        connectionPool.drainTo(connections);

        executeSingleRun(connections, connectionPool);
    }

    private void executeSingleRun(final List<GatewayAuthConnection> connections, final BlockingQueue<GatewayAuthConnection> connectionPool) {
        for (GatewayAuthConnection connection : connections) {
            // An uninitialised connection will always be expired
            if (connection.isExpired(getExpiryTimeout())) {
                try {
                    // We can't log out a session during initial creation
                    if (!connection.isNew()) {
                        LOGGER.info("Refreshing expired connection for username {}", connection.username());

                        // Perform logout request
                        try {
                            UnifiGatewayClient.logout(connection);
                        } catch (IllegalStateException e) {
                            LOGGER.debug("Ignoring logout failure; it's possible that we can still log in again.", e);
                        }
                    }

                    // Perform login request
                    UnifiGatewayLoginResponse loginResponse = UnifiGatewayClient.login(connection);

                    // Create a new connection with fresh details
                    GatewayAuthConnection refreshedConnection = connection.refresh(loginResponse.cookie(), loginResponse.csrfToken());
                    connectionPool.add(refreshedConnection);

                    LOGGER.info("Successfully created/refreshed connection for username {}", connection.username());
                } catch (Exception e) {
                    // Return the original connection to the pool if an error occurred, incrementing the failure count

                    if (connection.failureCount() < FAILURE_COUNT_LIMIT) {
                        connectionPool.add(connection.failed(connection));
                        LOGGER.warn("Error refreshing connection for connection {}. Incrementing failure count, will retry later.", connection, e);
                    } else {
                        LOGGER.error("Error refreshing connection for connection {}. Failure count limit exceeded, discarding connection.", connection, e);
                    }
                }
            } else {
                // Add non-expired connections back to the pool
                connectionPool.add(connection);
            }
        }
    }

    private Long getExpiryTimeout() {
        // A random element is added between a min and a max range to avoid making multiple calls at the same time.
        // Given that login requests can take some time, this ensures that other connections are still available
        // while an individual connection is being refreshed.
        return gatewayMinAuthExpiry + random.nextLong(gatewayMaxAuthExpiry - gatewayMinAuthExpiry);
    }
}
