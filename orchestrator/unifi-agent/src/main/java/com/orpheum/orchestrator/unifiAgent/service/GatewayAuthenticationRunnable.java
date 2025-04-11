package com.orpheum.orchestrator.unifiAgent.service;

import com.orpheum.orchestrator.unifiAgent.model.BackstageAuthenticationRequest;
import com.orpheum.orchestrator.unifiAgent.model.GatewayAuthenticationOutcome;
import com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties;
import com.orpheum.orchestrator.unifiAgent.support.BackstageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;

import static com.orpheum.orchestrator.unifiAgent.model.GatewayAuthenticationOutcomeStatus.FAILED;
import static com.orpheum.orchestrator.unifiAgent.model.GatewayAuthenticationOutcomeStatus.SUCCESS;
import static com.orpheum.orchestrator.unifiAgent.support.ApplicationProperties.getString;

public class GatewayAuthenticationRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayAuthenticationRunnable.class);

    private final HttpClient client = create();
    private final BackstageAuthenticationRequest authenticatedRequest;

    public GatewayAuthenticationRunnable(final BackstageAuthenticationRequest authenticatedRequest) {
        this.authenticatedRequest = authenticatedRequest;
    }

    @Override
    public void run() {
        try {
            LOGGER.debug("Starting gateway request authentication process. Triggering gateway login request. [Request:{}]", authenticatedRequest);

            final HttpRequest loginRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://localhost/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("""
                                   {
                                        "username":"%s",
                                        "password":"%s"
                                   }
                            """, getString("unifi_gateway_username"), getString("unifi_gateway_password"))))
                    .build();

            HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());

            if (loginResponse.statusCode() != 200) {
                throw new IllegalStateException(String.format("Unifi gateway login request failed! [Request: %s, Status code: %s, Headers: %s, Body: %s]", authenticatedRequest, loginResponse.statusCode(), loginResponse.headers(), loginResponse.body()));
            }
            LOGGER.debug("Login successful. Proceeding to gateway authorization request. [Request:{}]", authenticatedRequest);

            final HttpRequest authRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://localhost/proxy/network/api/s/default/cmd/stamgr"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginResponse.headers().firstValue("Set-Cookie").orElse(""))
                    .header("X-Csrf-Token", loginResponse.headers().firstValue("X-Csrf-Token").orElse(""))
                    .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("""
                                   {
                                        "cmd":"authorize-guest",
                                        "mac":"%s",
                                        "ap_mac": "%s"
                                   }
                            """, authenticatedRequest.macAddress(), authenticatedRequest.accessPointMacAddress())))
                    .build();

            HttpResponse<String> authResponse = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

            if (authResponse.statusCode() != 200) {
                throw new IllegalStateException(String.format("Unifi gateway authentication request failed! [Request: %s, Status code: %s, Headers: %s, Body: %s]", authenticatedRequest, loginResponse.statusCode(), loginResponse.headers(), loginResponse.body()));
            }
            LOGGER.debug("Authorization successful. Proceeding to notify backstage of outcome. [Request:{}]", authenticatedRequest);

            // We are optimistically notifying backstage before logging out in order to shave off some waiting time on the client's end
            BackstageClient.notifyAuthenticationOutcome(new GatewayAuthenticationOutcome(authenticatedRequest, SUCCESS));

            LOGGER.debug("Backstage notified successfully. Proceeding to gateway logout request. [Request:{}]", authenticatedRequest);

            final HttpRequest logoutRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://localhost/api/auth/logout"))
                    .header("Content-Type", "application/json")
                    .header("Cookie", loginResponse.headers().firstValue("Set-Cookie").orElse(""))
                    .header("X-Csrf-Token", loginResponse.headers().firstValue("X-Csrf-Token").orElse(""))
                    .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();

            HttpResponse<String> logoutResponse = client.send(logoutRequest, HttpResponse.BodyHandlers.ofString());

            if (logoutResponse.statusCode() != 200) {
                throw new IllegalStateException(String.format("Unifi gateway logout request failed! [Request: %s, Status code: %s, Headers: %s, Body: %s]", authenticatedRequest, loginResponse.statusCode(), loginResponse.headers(), loginResponse.body()));
            }
            LOGGER.debug("Gateway logout successful. Gateway authentication complete. [Request:{}]", authenticatedRequest);
        } catch (Exception e) {
            LOGGER.error("Failed to authenticate request on the UniFi Gateway. Notifying backstage of outcome. [Request:{}]", authenticatedRequest, e);
            try {
                BackstageClient.notifyAuthenticationOutcome(new GatewayAuthenticationOutcome(authenticatedRequest, FAILED, "Http request failure"));
                LOGGER.error("Notified backstage of failed outcome. [Request:{}]", authenticatedRequest);
            } catch (IOException | InterruptedException ex) {
                LOGGER.error("Failed to notify backstage of outcome. [Request:{}]", authenticatedRequest, ex);
            }
        } finally {
            AuthRepository.onGatewayAuthenticationCompleted(authenticatedRequest);
        }
    }

    private static HttpClient create() {
        // Trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // HTTP calls are made on the same physical device to the gateway, thus not posing any security risk
            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error("Failed to create trusting HTTP client.", e);
            throw new RuntimeException(e);
        }
    }

}