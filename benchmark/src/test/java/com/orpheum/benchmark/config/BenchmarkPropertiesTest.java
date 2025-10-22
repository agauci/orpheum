package com.orpheum.benchmark.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BenchmarkPropertiesTest {

    @Autowired
    private BenchmarkProperties benchmarkProperties;

    @Test
    public void testCompetitorGroupsLoaded() {
        assertNotNull(benchmarkProperties);
        assertNotNull(benchmarkProperties.getCompetitorGroups());
        assertFalse(benchmarkProperties.getCompetitorGroups().isEmpty());
        
        // Check if the property group from application.yaml exists
        assertTrue(benchmarkProperties.getCompetitorGroups().containsKey("2bed-equal-quality-gzira"));
        
        // Get the property group
        CompetitorGroup competitorGroup = benchmarkProperties.getCompetitorGroups().get("2bed-equal-quality-gzira");
        assertEquals("2 Bed Properties In Gzira Of Comparable Quality", competitorGroup.getTitle());
        assertEquals("A group of competitors in the Gzira area having 2 beds and of comparable quality/amenities", 
                competitorGroup.getDescription());
        
        // Check competitors
        assertNotNull(competitorGroup.getCompetitors());
        assertFalse(competitorGroup.getCompetitors().isEmpty());
        
        // Check a specific competitor
        assertTrue(competitorGroup.getCompetitors().containsKey("refined-elegance"));
        CompetitorConfig competitorConfig = competitorGroup.getCompetitors().get("refined-elegance");
        assertEquals("refined-elegance", competitorConfig.getKey());
        assertEquals("Refined Elegance By The Sea", competitorConfig.getTitle());
        assertEquals("https://www.airbnb.com.mt/rooms/1405209871580505002", competitorConfig.getUrl());
    }
}