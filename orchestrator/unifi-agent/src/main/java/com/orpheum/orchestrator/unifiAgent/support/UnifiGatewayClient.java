package com.orpheum.orchestrator.unifiAgent.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orpheum.orchestrator.unifiAgent.model.GatewayAuthConnection;
import com.orpheum.orchestrator.unifiAgent.model.UnifiGatewayActiveDevice;
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
import java.util.List;

/**
 * Encapsulates calls to the UniFi gateway
 */
public class UnifiGatewayClient {

    private static final HttpClient CLIENT = create();
    private static final ObjectMapper MAPPER = createObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiGatewayClient.class);

    public static UnifiGatewayLoginResponse login(GatewayAuthConnection connection) throws IOException, InterruptedException {
        LOGGER.debug("Attempting UniFi gateway admin login. [Username: {}]", connection.username());

        final HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://localhost/api/auth/login"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                .POST(HttpRequest.BodyPublishers.ofString(String.format("""
                                   {
                                        "username":"%s",
                                        "password":"%s"
                                   }
                            """, connection.username(), connection.password())))
                .build();

        HttpResponse<String> loginResponse = CLIENT.send(loginRequest, HttpResponse.BodyHandlers.ofString());

        if (loginResponse.statusCode() != 200) {
            throw new IllegalStateException(String.format("Unifi gateway login request failed! [Status code: %s, Headers: %s, Body: %s]", loginResponse.statusCode(), loginResponse.headers(), loginResponse.body()));
        }

        LOGGER.debug("Login successful. [Username:{}]", connection.username());

        return new UnifiGatewayLoginResponse(
                loginResponse.headers().firstValue("Set-Cookie").orElse(""),
                loginResponse.headers().firstValue("X-Csrf-Token").orElse("")
        );
    }

    public static void authorizeDevice(GatewayAuthConnection connection, String macAddress, String accessPointMacAddress) throws IOException, InterruptedException {
        LOGGER.debug("Attempting UniFi gateway device authorization. [Auth username: {}, MAC address:{}, AP MAC address: {}]", connection.username(), macAddress, accessPointMacAddress);

        final HttpRequest authRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://localhost/proxy/network/api/s/default/cmd/stamgr"))
                .header("Content-Type", "application/json")
                .header("Cookie", connection.cookie())
                .header("X-Csrf-Token", connection.csrfToken())
                .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                .POST(HttpRequest.BodyPublishers.ofString(String.format("""
                                   {
                                        "cmd":"authorize-guest",
                                        "mac":"%s",
                                        "ap_mac": "%s"
                                   }
                            """, macAddress, accessPointMacAddress)))
                .build();

        HttpResponse<String> authResponse = CLIENT.send(authRequest, HttpResponse.BodyHandlers.ofString());

        if (authResponse.statusCode() != 200) {
            throw new IllegalStateException(String.format("Unifi gateway device authorization request failed! [Status code: %s, Headers: %s, Body: %s]", authResponse.statusCode(), authResponse.headers(), authResponse.body()));
        }

        LOGGER.debug("Device authorization successful. [Auth username: {}, MAC address:{}, AP MAC address: {}]", connection.username(), macAddress, accessPointMacAddress);
    }

    public static List<UnifiGatewayActiveDevice> getActiveDevices(GatewayAuthConnection connection) throws IOException, InterruptedException {
        LOGGER.trace("Attempting UniFi gateway get active devices. [Username: {}]", connection.username());

        final HttpRequest getDevicesRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://localhost/proxy/network/v2/api/site/default/clients/active?includeTrafficUsage=false&includeUnifiDevices=false"))
                .header("Content-Type", "application/json")
                .header("Cookie", connection.cookie())
                .header("X-Csrf-Token", connection.csrfToken())
                .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                .GET()
                .build();

        HttpResponse<String> getDevicesResponse = CLIENT.send(getDevicesRequest, HttpResponse.BodyHandlers.ofString());

        if (getDevicesResponse.statusCode() != 200) {
            throw new IllegalStateException(String.format("UniFi gateway get active devices request failed! [Status code: %s, Headers: %s, Body: %s]", getDevicesResponse.statusCode(), getDevicesResponse.headers(), getDevicesResponse.body()));
        }

        final List<UnifiGatewayActiveDevice> devices = MAPPER.readValue(getDevicesResponse.body(), new TypeReference<List<UnifiGatewayActiveDevice>>() {});

        LOGGER.trace("UniFi gateway get active devices successful. [Username:{}, Devices: {}]", connection.username(), devices);

        return devices;
    }

    public static void logout(GatewayAuthConnection connection) throws IOException, InterruptedException {
        LOGGER.debug("Attempting UniFi gateway admin logout. [Username:{}]", connection.username());

        final HttpRequest logoutRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://localhost/api/auth/logout"))
                .header("Content-Type", "application/json")
                .header("Cookie", connection.cookie())
                .header("X-Csrf-Token", connection.csrfToken())
                .timeout(Duration.ofMillis(ApplicationProperties.getInteger("request_timeout")))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> logoutResponse = CLIENT.send(logoutRequest, HttpResponse.BodyHandlers.ofString());

        if (logoutResponse.statusCode() != 200) {
            throw new IllegalStateException(String.format("UniFi gateway logout request failed! [Username: %s, Status code: %s, Headers: %s, Body: %s]", connection.username(), logoutResponse.statusCode(), logoutResponse.headers(), logoutResponse.body()));
        }
        LOGGER.debug("UniFi gateway admin logout successful. [Username:{}]", connection.username());
    }

    public record UnifiGatewayLoginResponse(String cookie, String csrfToken) { }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
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
