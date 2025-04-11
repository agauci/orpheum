package com.orpheum.orchestrator.unifiAgent;


import com.orpheum.orchestrator.unifiAgent.service.GatewayAuthenticationService;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnifiAgentApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiAgentApplication.class);

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

        GatewayAuthenticationService.start();
    }

}
