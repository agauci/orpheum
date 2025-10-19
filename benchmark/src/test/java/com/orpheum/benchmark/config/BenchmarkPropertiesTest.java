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
    public void testPropertyGroupsLoaded() {
        assertNotNull(benchmarkProperties);
        assertNotNull(benchmarkProperties.getPropertyGroups());
        assertFalse(benchmarkProperties.getPropertyGroups().isEmpty());
        
        // Check if the property group from application.yaml exists
        assertTrue(benchmarkProperties.getPropertyGroups().containsKey("2bed-equal-quality-gzira"));
        
        // Get the property group
        PropertyGroup propertyGroup = benchmarkProperties.getPropertyGroups().get("2bed-equal-quality-gzira");
        assertEquals("2 Bed Properties In Gzira Of Comparable Quality", propertyGroup.getTitle());
        assertEquals("A group of competitors in the Gzira area having 2 beds and of comparable quality/amenities", 
                propertyGroup.getDescription());
        
        // Check competitors
        assertNotNull(propertyGroup.getCompetitors());
        assertFalse(propertyGroup.getCompetitors().isEmpty());
        
        // Check a specific competitor
        assertTrue(propertyGroup.getCompetitors().containsKey("refined-elegance"));
        CompetitorConfig competitorConfig = propertyGroup.getCompetitors().get("refined-elegance");
        assertEquals("refined-elegance", competitorConfig.getKey());
        assertEquals("Refined Elegance By The Sea", competitorConfig.getTitle());
        assertEquals("https://www.airbnb.com.mt/rooms/1405209871580505002", competitorConfig.getUrl());
    }
}