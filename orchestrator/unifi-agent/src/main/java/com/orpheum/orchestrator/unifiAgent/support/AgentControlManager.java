package com.orpheum.orchestrator.unifiAgent.support;

import com.orpheum.orchestrator.unifiAgent.auth.GatewayAuthConnectionManager;
import com.orpheum.orchestrator.unifiAgent.capport.CaptivePortalDeviceStateServer;
import com.orpheum.orchestrator.unifiAgent.gateway.GatewayAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An internal administrative implementation which reads configuration found in `agent_control.properties` and executes
 * commands based on the state of the configuraiton. Possible commands include:
 *
 * <ul>
 *     <li>active - True implies that that the agent is running, false kicks off the shutdown process. Kickstarting the
 *     shutdown process implies that the agent needs to be restarted for it to work correctly again.</li>
 *     <li>printAuthConnections - True grabs the admin connections available and prints them to command line.</li>
 *     <li>clearAuthorisedDevicesCache - True clears any authorised devices in the cache</li>
 *     <li>printAuthorisedDevicesCache - True prings any authorised devices within the cache</li>
 * </ul>
 */
public class AgentControlManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgentControlManager.class);

    private static final AtomicBoolean IS_SHUTDOWN_TRIGGERED = new AtomicBoolean(false);
    private static final AtomicLong LAST_CONNECTION_VIEW = new AtomicLong(0L);
    private static final AtomicLong LAST_AUTHORISED_DEVICES_CACHE_VIEW = new AtomicLong(0L);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (IS_SHUTDOWN_TRIGGERED.get()) {
                return;
            }

            LOGGER.info("System shutdown detected. Attempting safe shutdown trigger.");
            shutdown();
            LOGGER.info("System shutdown completed successfully.");
        }));
    }

    public static void start(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(() -> {
            if (IS_SHUTDOWN_TRIGGERED.get()) {
                return;
            }

            Properties controlProperties = readControlFile();
            processShutdownFlag(controlProperties);
            processPrintConnectionsFlag(controlProperties);
            processClearAuthorisedDevicesCache(controlProperties);
            processPrintAuthorisedDevicesCache(controlProperties);
        }, 1000L, 1000L, TimeUnit.MILLISECONDS);

        LOGGER.info("Agent control manager started!");
    }

    private static void processShutdownFlag(Properties controlProperties) {
        if (!Boolean.parseBoolean(controlProperties.getProperty("active"))) {
            LOGGER.info("Identified active=false, triggering safe shutdown");
            shutdown();
            LOGGER.info("Shutdown complete, it is safe to restart the agent.");
        }
    }

    private static void processPrintConnectionsFlag(Properties controlProperties) {
        if (Boolean.parseBoolean(controlProperties.getProperty("printAuthConnections"))) {
            if ((System.currentTimeMillis() - LAST_CONNECTION_VIEW.get()) > 60000L) {
                LOGGER.info("Current auth connections: {}", GatewayAuthConnectionManager.getConnectionsView());

                LAST_CONNECTION_VIEW.set(System.currentTimeMillis());
            }
        }
    }

    private static void processClearAuthorisedDevicesCache(Properties controlProperties) {
        if (Boolean.parseBoolean(controlProperties.getProperty("clearAuthorisedDevicesCache"))) {
            GatewayAuthenticationService.clearCache();
        }
    }

    private static void processPrintAuthorisedDevicesCache(Properties controlProperties) {
        if (Boolean.parseBoolean(controlProperties.getProperty("printAuthorisedDevicesCache"))) {
            if ((System.currentTimeMillis() - LAST_AUTHORISED_DEVICES_CACHE_VIEW.get()) > 60000L) {
                LOGGER.info("Current devices in cache: {}", GatewayAuthenticationService.getAuthorisedDevicesView());

                LAST_AUTHORISED_DEVICES_CACHE_VIEW.set(System.currentTimeMillis());
            }
        }
    }

    public static void shutdown() {
        try {
            IS_SHUTDOWN_TRIGGERED.set(true);

            GatewayAuthConnectionManager.shutdown();
            GatewayAuthenticationService.shutdown();
            CaptivePortalDeviceStateServer.shutdown();
        } catch (Exception e) {
            LOGGER.error("Encountered unexpected exception during shutdown process.", e);
        }
    }

    public static Properties readControlFile() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream("agent_control.properties")) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return props;
    }

}
