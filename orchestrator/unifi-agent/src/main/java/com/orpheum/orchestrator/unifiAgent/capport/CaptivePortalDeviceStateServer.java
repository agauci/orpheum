package com.orpheum.orchestrator.unifiAgent.capport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orpheum.orchestrator.unifiAgent.gateway.GatewayAuthenticationService;
import com.orpheum.orchestrator.unifiAgent.model.UnifiGatewayActiveDevice;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Lightweight server implementing DHCP Options 114 Captive Portal authentication state. This is achieved by exposing a
 * single URL (/.well-known/captive-portal) which (i) resolves the requester's IP address, (ii) checks with its own
 * internal cache if this device is already authorised, and (iii) sends the device's captive portal state, as well as
 * the backstage captive portal URL in case the device needs to authorise its entry on the network.
 */
public class CaptivePortalDeviceStateServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptivePortalDeviceStateServer.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String BASE_PORTAL_URL = ApplicationProperties.getString("capport_base_portal_url");
    private static final String VENUE_URL = ApplicationProperties.getString("capport_venue_url");

    private static final String SITE_IDENTIFIER = ApplicationProperties.getString("site_identifier");

    private static Undertow server;

    public static void start(ScheduledExecutorService service) {
        Integer capportServerPort = ApplicationProperties.getInteger("capport_server_port");
        Integer capportServerIoThreads = ApplicationProperties.getInteger("capport_server_io_threads");
        Integer capportServerWorkerThreads = ApplicationProperties.getInteger("capport_server_worker_threads");

        LOGGER.info("Starting captive portal device state server. [Port {}, ioThreads: {}, workerThreads: {}]", capportServerPort, capportServerIoThreads, capportServerWorkerThreads);

        server = Undertow.builder()
                .addHttpListener(capportServerPort, "0.0.0.0")
                .setHandler(getHttpHandler())
                .setWorkerThreads(capportServerWorkerThreads)
                .setIoThreads(capportServerIoThreads)
                .build();

        server.start();

        LOGGER.info("Capport server started!");
    }

    private static HttpHandler getHttpHandler() {
        return exchange -> {
            if (exchange.getRequestPath().equals("/.well-known/captive-portal") && exchange.getRequestMethod().toString().equals("GET")) {
                captivePortal(exchange);
            } else {
                exchange.setStatusCode(404);
                exchange.getResponseSender().send("Not Found");
            }
        };
    }

    public static void captivePortal(final HttpServerExchange exchange) {
        try {
            final String deviceIp = resolveIp(exchange);
            LOGGER.debug("Received Capport GET request for device with IP {}", deviceIp);

            Optional<UnifiGatewayActiveDevice> resolvedDevice = GatewayAuthenticationService.resolveAuthorisedCachedDevice(deviceIp);

            CaptivePortalResponse response;
            if (resolvedDevice.isPresent()) {
                response = new CaptivePortalResponse(!resolvedDevice.get().authorized(), constructPortalUrl(deviceIp), VENUE_URL, false);
                LOGGER.debug("Device resolved successfully. Sending response {}", response);
            } else {
                response = new CaptivePortalResponse(true, constructPortalUrl(deviceIp), VENUE_URL, false);
                LOGGER.debug("Unable to resolve device matching ip {}. Sending response {}.", deviceIp, response);
            }

            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(OBJECT_MAPPER.writeValueAsString(response));
        } catch (Exception e) {
            LOGGER.error("Unexpected exception triggered while attempting to process capport GET request.", e);
            exchange.setStatusCode(500);
            exchange.getResponseSender().send("Something went wrong");
        }
    }

    private static String resolveIp(HttpServerExchange exchange) {
        String xForwardedForIp = Optional.ofNullable(exchange.getRequestHeaders().getFirst("X-Forwarded-For")).orElse("");

        if (xForwardedForIp.startsWith("192.168")) {
            return xForwardedForIp;
        }

        String xRealIp = Optional.ofNullable(exchange.getRequestHeaders().getFirst("X-Real-IP")).orElse("");

        if (xRealIp.startsWith("192.168")) {
            return xRealIp;
        }

        return exchange.getSourceAddress().getAddress().getHostAddress();
    }

    private static String constructPortalUrl(String ip) {
        return BASE_PORTAL_URL + "?ip=" + urlEncode(ip) + "&ssid=" + urlEncode(SITE_IDENTIFIER) + "&t=" + System.currentTimeMillis();
    }

    private static String urlEncode(final String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    public static void shutdown() {
        if (server != null) {
            server.stop();
            LOGGER.info("Capport server stopped");
        }
    }

    private record CaptivePortalResponse(boolean captive, String user_portal_url, String venue_info_url, boolean can_extend_session) { }
}