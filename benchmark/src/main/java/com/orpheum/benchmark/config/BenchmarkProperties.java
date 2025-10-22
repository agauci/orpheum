package com.orpheum.benchmark.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Configuration properties for the benchmark application.
 * Maps to the "orpheum.benchmark" prefix in application.yaml.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "orpheum.benchmark")
public class BenchmarkProperties {
    private Map<String, CompetitorGroup> competitorGroups;
}
