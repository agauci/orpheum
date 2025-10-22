package com.orpheum.benchmark.config;

import com.orpheum.benchmark.model.AmenitiesLevel;
import com.orpheum.benchmark.model.FinishesLevel;
import com.orpheum.benchmark.model.ProximityToSeafront;
import com.orpheum.benchmark.model.ViewLevel;
import lombok.Data;

import java.util.Map;

/**
 * Configuration class representing a property group with title, description, and competitors.
 */
@Data
public class CompetitorGroup {
    private String key;
    private String title;
    private Boolean isInternalGroup;
    private String description;
    private String location;
    private Integer bedroomCount;
    private FinishesLevel finishesLevel;
    private AmenitiesLevel amenitiesLevel;
    private ViewLevel viewLevel;
    private ProximityToSeafront proximityToSeafront;
    private Map<String, CompetitorConfig> competitors;
}