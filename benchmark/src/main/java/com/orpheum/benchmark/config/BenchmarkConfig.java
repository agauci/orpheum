package com.orpheum.benchmark.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.util.Map;

/**
 * Configuration class for the benchmark application.
 * Processes the properties and sets the keys for competitors.
 */
@Configuration
public class BenchmarkConfig {

    private final BenchmarkProperties benchmarkProperties;

    public BenchmarkConfig(BenchmarkProperties benchmarkProperties) {
        this.benchmarkProperties = benchmarkProperties;
    }

    @PostConstruct
    public void init() {
        // Set the keys for property groups and competitors
        if (benchmarkProperties.getCompetitorGroups() != null) {
            for (Map.Entry<String, CompetitorGroup> groupEntry : benchmarkProperties.getCompetitorGroups().entrySet()) {
                String groupKey = groupEntry.getKey();
                CompetitorGroup group = groupEntry.getValue();
                group.setKey(groupKey);

                if (group.getCompetitors() != null) {
                    for (Map.Entry<String, CompetitorConfig> competitorEntry : group.getCompetitors().entrySet()) {
                        String competitorKey = competitorEntry.getKey();
                        CompetitorConfig competitor = competitorEntry.getValue();
                        competitor.setKey(competitorKey);
                    }
                }
            }
        }
    }
}
