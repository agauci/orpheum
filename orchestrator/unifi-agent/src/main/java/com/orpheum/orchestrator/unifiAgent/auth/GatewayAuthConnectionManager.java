package com.orpheum.orchestrator.unifiAgent.auth;

import com.orpheum.orchestrator.unifiAgent.model.GatewayAuthConnection;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import com.orpheum.orchestrator.unifiAgent.support.UnifiGatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class responsible for managing a pool of gateway authentication connections.
 * Includes automatic refreshing of stale connections.
 */
public class GatewayAuthConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayAuthConnectionManager.class);

    private static final BlockingQueue<GatewayAuthConnection> CONNECTION_POOL = new LinkedBlockingQueue<>();

    private static AtomicInteger connectionCount = new AtomicInteger();

    /**
     * Starts the connection manager with the provided executor service.
     * The executor service will be used to schedule periodic refreshing of expired connections.
     *
     * @param executorService The scheduled executor service to use
     */
    public static void start(ScheduledExecutorService executorService) {
        GatewayAuthConnectionRefresherRunnable runnable = new GatewayAuthConnectionRefresherRunnable();
        runnable.initialiseConnections(parseAuthCredentials());
        connectionCount.set(CONNECTION_POOL.size());

        executorService.scheduleAtFixedRate(
                runnable,
                0,
                ApplicationProperties.getInteger("gateway_auth_connection_manager_delay_ms"),
                TimeUnit.MILLISECONDS
        );
        LOGGER.info("Connection manager started");
    }

    public static void shutdown() throws InterruptedException, IOException {
        for (int i = 0; i < connectionCount.get(); i++) {
            GatewayAuthConnection connection = borrowConnection();

            try {
                UnifiGatewayClient.logout(connection);
            } catch (IllegalStateException e) {
                LOGGER.error("Failed to process connection logout during shutdown. Skipping. [Username: {}]", connection.username(), e);
            }
        }
        LOGGER.debug("Stopped GatewayAuthConnectionManager");
    }

    /**
     * Loans out a connection from the pool.
     * If no connections are available, this method blocks until one becomes available.
     *
     * @return An authentication connection
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public static GatewayAuthConnection borrowConnection() throws InterruptedException {
        GatewayAuthConnection gatewayAuthConnection = CONNECTION_POOL.take();// Blocks if the queue is empty
        return gatewayAuthConnection;
    }

    /**
     * Returns a previously borrowed connection to the pool.
     *
     * @param connection The connection to return to the pool
     */
    public static void returnConnection(GatewayAuthConnection connection) {
        CONNECTION_POOL.add(connection);
    }

    public static void onConnectionFailure() {
        connectionCount.set(connectionCount.get() - 1);
    }

    static BlockingQueue<GatewayAuthConnection> getConnectionPool() {
        return CONNECTION_POOL;
    }

    public static List<GatewayAuthConnection> getConnectionsView() {
        return CONNECTION_POOL.stream().toList();
    }

    public static List<AuthCredentials> parseAuthCredentials() {
        List<AuthCredentials> credentials = new ArrayList<>();

        String input = ApplicationProperties.getString("unifi_gateway_auth_credentials");
        if (input == null || input.isBlank()) {
            return credentials;
        }

        String[] entries = input.split(",");
        for (String entry : entries) {
            String[] parts = entry.trim().split(":", 2);
            if (parts.length == 2) {
                String username = parts[0].trim();
                String password = parts[1].trim();
                credentials.add(new AuthCredentials(username, password));
            }
        }

        return credentials;
    }

    public record AuthCredentials(String username, String password) { }

}
