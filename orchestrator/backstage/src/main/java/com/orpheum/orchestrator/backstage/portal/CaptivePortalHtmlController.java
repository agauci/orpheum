package com.orpheum.orchestrator.backstage.portal;

import com.orpheum.orchestrator.backstage.portal.model.BackstageAuthorisationRequest;
import com.orpheum.orchestrator.backstage.portal.model.GatewayAuthenticationOutcome;
import com.orpheum.orchestrator.backstage.portal.support.PortalConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.orpheum.orchestrator.backstage.portal.model.BackstageAuthenticationRequestStatus.PRE_AUTH;
import static com.orpheum.orchestrator.backstage.portal.model.GatewayAuthenticationOutcomeStatus.FAILED;
import static com.orpheum.orchestrator.backstage.portal.model.GatewayAuthenticationOutcomeStatus.SUCCESS;

/**
 * A controller for HTML pages constituting the backstage captive portal. The two core parts of functionality found here
 * involve (i) capturing a device's call on the captive portal - in turn forwarding IP or MAC address information to
 * identify the said device with the gateway, and (ii) triggering an authoriseation from the captive portal, which in
 * turn exposes a pending backstge authorisation for the respective agent to consume and execute. The latter endpoint
 * also captures any identifiable information exposed by the user (such as names and emails), thus completing the intent
 * of the captive portal.
 */
@Controller
@Slf4j
public class CaptivePortalHtmlController {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    PortalConfig portalConfig;

    @Value("${backstage.portal.auth-timeout-ms}")
    private Long authenticationRequestTimeoutMs;

    @GetMapping(value = "/")
    public String indexBasic() {
        log.trace("Access to basic portal detected.");
        return "index";
    }


    @GetMapping(value = "/guest/s/{network}/")
    public String index(Model model,
                        @RequestParam(name="id", required = false) String macAddress,
                        @RequestParam(name="ap", required = false) String accessPointMacAddress,
                        @RequestParam(name="ip", required = false) String ip,
                        @RequestParam(name="t", required = false) Long timestamp,
                        @RequestParam(name="ssid", required = false) String siteIdentifier) {
        log.debug("Received portal GET request. [MAC: {}, AP: {}, ip: {}, timestamp: {}, siteIdentifier: {}]", macAddress, accessPointMacAddress, ip, timestamp, siteIdentifier);

        if (macAddress != null) {
            authRepository.add(new BackstageAuthorisationRequest(macAddress, accessPointMacAddress, siteIdentifier, ip, timestamp, PRE_AUTH));
            model.addAttribute("id", macAddress);
            model.addAttribute("ap", accessPointMacAddress);
            model.addAttribute("ip", ip);
            model.addAttribute("ssid", siteIdentifier);
        }

        return "index";
    }

    @PostMapping(value = "/guest/s/{network}/")
    public String indexPost(Model model,
                            @RequestParam(name="id", required = false) String macAddress,
                            @RequestParam(name="ap", required = false) String accessPointMacAddress,
                            @RequestParam(name="t", required = false) Long timestamp,
                            @RequestParam(name="ip", required = false) String ip,
                            @RequestParam(name="ssid", required = false) String siteIdentifier) {
        log.debug("Received portal POST request. [MAC: {}, AP: {}, ip: {}, timestamp: {}, siteIdentifier: {}]", macAddress, accessPointMacAddress, ip, timestamp, siteIdentifier);

        if (macAddress != null) {
            authRepository.add(new BackstageAuthorisationRequest(macAddress, accessPointMacAddress, siteIdentifier, ip, timestamp, PRE_AUTH));
            model.addAttribute("id", macAddress);
            model.addAttribute("ap", accessPointMacAddress);
            model.addAttribute("ip", ip);
            model.addAttribute("ssid", siteIdentifier);
        }

        return "index";
    }

    @PostMapping(value = "/authorise")
    public String authorise(Model model,
                            @RequestParam(name="firstName") String firstName,
                            @RequestParam(name="lastName") String lastName,
                            @RequestParam(name="email") String email,
                            @RequestParam(name="id", required = false) String macAddress,
                            @RequestParam(name="ap", required = false) String accessPointMacAddress,
                            @RequestParam(name="ip", required = false) String ip,
                            @RequestParam(name="ssid") String siteIdentifier) throws ExecutionException, InterruptedException {
        log.debug("Received portal authorise request. [MAC Address: {}, Access Point MAC Address: {}, IP: {}, Site Identifier: {}]", macAddress, accessPointMacAddress, ip, siteIdentifier);
        validate(macAddress, accessPointMacAddress, ip);

        log.info("Received the following info [First name: {}, last name: {}, email: {}", firstName, lastName, email);

        final GatewayAuthenticationOutcome authOutcome = authRepository.authoriseRequest(macAddress, accessPointMacAddress, siteIdentifier, ip)
                .completeOnTimeout(new GatewayAuthenticationOutcome(null, FAILED, "Authentication request timed out. Please try again later."), authenticationRequestTimeoutMs, TimeUnit.MILLISECONDS)
                .get();

        PortalConfig.SiteConfigDetails siteConfigDetails = portalConfig.getSiteConfigBySiteIdentifier(siteIdentifier);

        if (SUCCESS.equals(authOutcome.outcome())) {
            log.info("Successfully completed gateway authentication request with outcome {}", authOutcome);
            model.addAttribute("redirectUrl", siteConfigDetails.getRedirectUrl());

            return "success";
        } else {
            model.addAttribute("errorMessage", authOutcome.message());
            model.addAttribute("backupWifiSsid", siteConfigDetails.getBackupWifiSsid());
            model.addAttribute("backupWifiPassword", siteConfigDetails.getBackupWifiPassword());

            log.warn("Failed to authenticate request, resolving outcome {}", authOutcome);
            return "error";
        }
    }

    @GetMapping(value = "/privacy-policy")
    public String privacyPolicy() {
        return "privacy-policy";
    }

    @GetMapping(value = "/terms-of-use")
    public String termsOfUse() {
        return "terms-of-use";
    }

    private void validate(final String macAddress, final String apMacAddress, final String ip) {
        if (macAddress == null && apMacAddress == null && ip == null)  {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The MAC address, access point MAC address and IP address are all missing");
        }

        if (ip == null && (macAddress == null || apMacAddress == null))  {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "If either MAC address is provided, the other MAC address is also necessary.");
        }
    }

}
