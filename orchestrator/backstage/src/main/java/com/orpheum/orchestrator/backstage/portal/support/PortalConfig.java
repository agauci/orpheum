package com.orpheum.orchestrator.backstage.portal.support;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@ConfigurationProperties(prefix = "orpheum")
@NoArgsConstructor
@Data
public class PortalConfig {

    private Map<String, SiteConfigDetails> siteConfig;
    private String backupWifiSsid;
    private String backupWifiPassword;

    public Optional<SiteConfigDetails> getSiteConfigBySiteIdentifier(final String siteIdentifier) {
        return siteConfig.entrySet().stream()
                .filter(entry -> entry.getValue().getSiteIdentifier().equals(siteIdentifier))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @NoArgsConstructor
    @Data
    public static class SiteConfigDetails {
        private String siteIdentifier;
        private String redirectUrl;
    }
}