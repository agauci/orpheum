package com.orpheum.orchestrator.unifiAgent;


import com.orpheum.orchestrator.unifiAgent.auth.GatewayAuthConnectionManager;
import com.orpheum.orchestrator.unifiAgent.gateway.GatewayAuthenticationService;
import com.orpheum.orchestrator.unifiAgent.support.AgentControlManager;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import com.orpheum.orchestrator.unifiAgent.capport.CaptivePortalDeviceStateServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class UnifiAgentApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiAgentApplication.class);

    private static final ScheduledExecutorService THREAD_POOL = Executors.newScheduledThreadPool(ApplicationProperties.getInteger("thread_pool_size"));

    public static void main(String[] args) {
        System.out.println("""
                        ________               .__                            ____ ___      .__  _____.__     _____                         __  \s
                        \\_____  \\_____________ |  |__   ____  __ __  _____   |    |   \\____ |__|/ ____\\__|   /  _  \\    ____   ____   _____/  |_\s
                         /   |   \\_  __ \\____ \\|  |  \\_/ __ \\|  |  \\/     \\  |    |   /    \\|  \\   __\\|  |  /  /_\\  \\  / ___\\_/ __ \\ /    \\   __\\
                        /    |    \\  | \\/  |_> >   Y  \\  ___/|  |  /  Y Y  \\ |    |  /   |  \\  ||  |  |  | /    |    \\/ /_/  >  ___/|   |  \\  | \s
                        \\_______  /__|  |   __/|___|  /\\___  >____/|__|_|  / |______/|___|  /__||__|  |__| \\____|__  /\\___  / \\___  >___|  /__| \s
                                \\/      |__|        \\/     \\/            \\/               \\/                       \\//_____/      \\/     \\/     \s
                        """);

        LOGGER.info("Initializing Orpheum UniFi Agent");
        LOGGER.debug("Loaded properties {}", ApplicationProperties.getProperties());

        GatewayAuthConnectionManager.start(THREAD_POOL);
        CaptivePortalDeviceStateServer.start(THREAD_POOL);
        AgentControlManager.start(THREAD_POOL);
        // This should always be last since it occupies the main thread
        GatewayAuthenticationService.start(THREAD_POOL);
    }

}
