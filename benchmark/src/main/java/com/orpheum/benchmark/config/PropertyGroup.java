package com.orpheum.benchmark.config;

import com.orpheum.benchmark.model.AmenitiesLevel;
import com.orpheum.benchmark.model.FinishingLevel;
import lombok.Data;

import java.util.Map;

/**
 * Configuration class representing a property group with title, description, and competitors.
 */
@Data
public class PropertyGroup {
    private String key;
    private String title;
    private String description;
    private String location;
    private Integer bedroomCount;
    private FinishingLevel finishingLevel;
    private AmenitiesLevel amenitiesLevel;
    private boolean hasView;
    private boolean hasTerrace;
    private Map<String, CompetitorConfig> competitors;
}