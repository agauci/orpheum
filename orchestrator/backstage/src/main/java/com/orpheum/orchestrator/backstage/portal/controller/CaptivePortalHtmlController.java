package com.orpheum.orchestrator.backstage.portal.controller;

import com.orpheum.orchestrator.backstage.portal.model.auth.GatewayAuthorisationOutcome;
import com.orpheum.orchestrator.backstage.portal.service.AuthService;
import com.orpheum.orchestrator.backstage.portal.support.PortalConfig;
import com.orpheum.orchestrator.backstage.portal.support.PortalConfig.SiteConfigDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.orpheum.orchestrator.backstage.portal.model.auth.GatewayAuthorisationOutcomeStatus.FAILED;
import static com.orpheum.orchestrator.backstage.portal.model.auth.GatewayAuthorisationOutcomeStatus.SUCCESS;

/**
 * A controller for HTML pages constituting the backstage captive portal. The two core parts of functionality found here
 * involve (i) capturing a device's call on the captive portal - in turn forwarding IP or MAC address information to
 * identify the said device with the gateway, and (ii) triggering an authorisation from the captive portal, which in
 * turn exposes a pending backstage authorisation for the respective agent to consume and execute. The latter endpoint
 * also captures any identifiable information exposed by the user (such as names and emails), thus completing the intent
 * of the captive portal.
 */
@Controller
@Slf4j
public class CaptivePortalHtmlController {

    @Autowired
    AuthService authService;

    @Autowired
    PortalConfig portalConfig;

    @Value("${backstage.portal.auth-timeout-ms}")
    private Long authorisationRequestTimeoutMs;

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

        if (macAddress != null || ip != null) {
            model.addAttribute("id", macAddress);
            model.addAttribute("ap", accessPointMacAddress);
            model.addAttribute("ip", ip);
            model.addAttribute("ssid", siteIdentifier);
            model.addAttribute("t", timestamp);
            model.addAttribute("consentText", portalConfig.getConsentText());
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

        if (macAddress != null || ip != null) {
            model.addAttribute("id", macAddress);
            model.addAttribute("ap", accessPointMacAddress);
            model.addAttribute("ip", ip);
            model.addAttribute("ssid", siteIdentifier);
            model.addAttribute("t", timestamp);
            model.addAttribute("consentText", portalConfig.getConsentText());
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
                            @RequestParam(name="ssid") String siteIdentifier,
                            @RequestParam(name="t") Long timestamp) throws ExecutionException, InterruptedException {
        log.debug("Received portal authorise request. [MAC Address: {}, Access Point MAC Address: {}, IP: {}, Site Identifier: {}, First name: {}, last name: {}, email: {}]",
                macAddress, accessPointMacAddress, ip, siteIdentifier, firstName, lastName, email);

        Optional<GatewayAuthorisationOutcome> validationOutcome = validate(macAddress, accessPointMacAddress, ip);
        if (validationOutcome.isPresent()) {
            return processFailureOutcome(model, validationOutcome.get());
        }
        Optional<SiteConfigDetails> siteConfigDetails = portalConfig.getSiteConfigBySiteIdentifier(siteIdentifier);
        if (siteConfigDetails.isEmpty()) {
            return processFailureOutcome(model, new GatewayAuthorisationOutcome(null, FAILED, "Missing site configuration for " + siteIdentifier));
        }

        final GatewayAuthorisationOutcome authOutcome = authService.startAuthorisation(macAddress,
                                                                                       accessPointMacAddress,
                                                                                       timestamp,
                                                                                       ip,
                                                                                       siteIdentifier,
                                                                                       firstName,
                                                                                       lastName,
                                                                                       email,
                                                                                       siteConfigDetails.get())
                .completeOnTimeout(new GatewayAuthorisationOutcome(null, FAILED, "Authentication request timed out. Please try again later."), authorisationRequestTimeoutMs, TimeUnit.MILLISECONDS)
                .get();

        if (SUCCESS.equals(authOutcome.outcome())) {
            log.info("Successfully completed gateway authentication request with outcome {}", authOutcome);
            model.addAttribute("redirectUrl", siteConfigDetails.get().getRedirectUrl());
            model.addAttribute("siteIdentifier", siteConfigDetails.get().getSiteIdentifier());

            return "success";
        } else {
            return processFailureOutcome(model, authOutcome);
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

    @PostMapping(value = "/error")
    public String error(Model model) {
        model.addAttribute("backupWifiSsid", portalConfig.getBackupWifiSsid());
        model.addAttribute("backupWifiPassword", portalConfig.getBackupWifiPassword());

        return "error";
    }

    private String processFailureOutcome(Model model, GatewayAuthorisationOutcome authOutcome) {
        model.addAttribute("errorMessage", authOutcome.message());
        model.addAttribute("backupWifiSsid", portalConfig.getBackupWifiSsid());
        model.addAttribute("backupWifiPassword", portalConfig.getBackupWifiPassword());

        log.warn("Failed to process authorisation request. [Outcome: {}]", authOutcome);
        return "error";
    }

    private Optional<GatewayAuthorisationOutcome> validate(final String macAddress, final String apMacAddress, final String ip) {
        if (isNullOrEmpty(macAddress) && isNullOrEmpty(apMacAddress) && isNullOrEmpty(ip))  {
            return Optional.of(new GatewayAuthorisationOutcome(null, FAILED, "The MAC address, access point MAC address and IP address are all missing"));
        }

        if (isNullOrEmpty(ip) && (isNullOrEmpty(macAddress) || isNullOrEmpty(apMacAddress)))  {
            return Optional.of(new GatewayAuthorisationOutcome(null, FAILED, "If either MAC address is provided, the other MAC address is also necessary."));
        }

        return Optional.empty();
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
