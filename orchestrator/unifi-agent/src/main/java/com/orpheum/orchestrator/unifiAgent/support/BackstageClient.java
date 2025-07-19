package com.orpheum.orchestrator.unifiAgent.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orpheum.orchestrator.unifiAgent.model.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.unifiAgent.model.GatewayAuthorisationOutcome;
import com.orpheum.orchestrator.unifiAgent.model.BackstageLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Encapsulates API calls to the backstage server
 */
public class BackstageClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackstageClient.class);

    private static final String GET_URL;
    private static final String CONFIRM_URL;
    private static final String LOGS_URL;
    private static final String HEARTBEAT_URL;

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        try {
            GET_URL = "https://backstage.orpheum.cloud/portal?site_identifier=" + URLEncoder.encode(ApplicationProperties.getString("site_identifier"), "UTF-8");
            CONFIRM_URL = "https://backstage.orpheum.cloud/portal";
            LOGS_URL = "https://backstage.orpheum.cloud/gateway/logs";
            HEARTBEAT_URL = "https://backstage.orpheum.cloud/heartbeat/refresh";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<BackstageAuthorisationRequest> getPendingAuthorisationRequests() throws IOException, InterruptedException {
        LOGGER.trace("Attempting request to retrieve pending authorisation requests from backstage.");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GET_URL))
                .header("X-Auth-Token", ApplicationProperties.getString("backstage_api_auth_token"))
                .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                .GET()
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(String.format("Backstage GET request failed! Status code: %s, Headers: %s, Body: %s", response.statusCode(), response.headers(), response.body()));
        }

        final List<BackstageAuthorisationRequest> retrievedRequests = MAPPER.readValue(response.body(), new TypeReference<>() {});

        LOGGER.trace("Successfully completed GET backstage request. [Retrieved requests = {}]", retrievedRequests);

        return retrievedRequests;
    }

    public static void notifyAuthorisationOutcome(final GatewayAuthorisationOutcome outcome) throws IOException, InterruptedException {
        LOGGER.debug("Attempting request to notify authorisation outcome. [Outcome={}]", outcome);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CONFIRM_URL))
                .header("X-Auth-Token", ApplicationProperties.getString("backstage_api_auth_token"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                .POST(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(outcome)))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(String.format("Backstage POST confirmation request failed! Status code: %s, Headers: %s, Body: %s", response.statusCode(), response.headers(), response.body()));
        }

        LOGGER.debug("Successfully notified authentication outcome. [Outcome={}]", outcome);
    }

    /**
     * Sends a batch of log entries to the backstage server.
     *
     * @param logEntries The list of log entries to send
     * @throws IOException If an I/O error occurs during the request
     * @throws InterruptedException If the operation is interrupted
     * @throws IllegalStateException If the request fails (non-200 status code)
     */
    public static void sendLogs(final List<BackstageLogEntry> logEntries) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGS_URL))
                .header("X-Auth-Token", ApplicationProperties.getString("backstage_api_auth_token"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                .POST(HttpRequest.BodyPublishers.ofString(MAPPER.writeValueAsString(logEntries)))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(String.format("Backstage POST logs request failed! Status code: %s, Headers: %s, Body: %s", response.statusCode(), response.headers(), response.body()));
        }
    }

    /**
     * Sends a heartbeat signal to the backstage server.
     *
     * @param heartbeatType The type of heartbeat. Valid values are "GATEWAY" or "DATABASE".
     * @param identifier The identifier for the heartbeat
     * @throws IOException If an I/O error occurs during the request
     * @throws InterruptedException If the operation is interrupted
     * @throws IllegalStateException If the request fails (non-200 status code)
     */
    public static void sendHeartbeat(final String heartbeatType, final String identifier) throws IOException, InterruptedException {
        LOGGER.debug("Sending heartbeat to backstage. [Type={}, Identifier={}]", heartbeatType, identifier);

        String url = HEARTBEAT_URL + "?type=" + URLEncoder.encode(heartbeatType, "UTF-8") + "&identifier=" + URLEncoder.encode(identifier, "UTF-8");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Auth-Token", ApplicationProperties.getString("backstage_api_auth_token"))
                .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(String.format("Backstage POST heartbeat request failed! Status code: %s, Headers: %s, Body: %s", response.statusCode(), response.headers(), response.body()));
        }

        LOGGER.debug("Successfully sent heartbeat to backstage. [Type={}, Identifier={}]", heartbeatType, identifier);
    }

}
