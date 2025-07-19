package com.orpheum.orchestrator.backstage.heartbeat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "backstage.heartbeat")
@NoArgsConstructor
@Data
public class HeartbeatVerifierConfig {

    private Long periodMs;
    private Long warningIntervalMs;
    private Map<HeartbeatType, HeartbeatTypeConfigDetails> verifier;

    @NoArgsConstructor
    @Data
    public static class HeartbeatTypeConfigDetails {
        private Long toleranceMs;
    }
}