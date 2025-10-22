package com.orpheum.benchmark.config;

import lombok.Data;

/**
 * Configuration class representing a competitor with key, title, and URL.
 */
@Data
public class CompetitorConfig {
    private String key;
    private String title;
    private Integer windowSize;
    private Integer maxOccupancyCount;
    private String url;
    private boolean isOpenForBookings;
    private boolean hasTerrace;
    private boolean hasPool;
    private boolean hasBarbecue;

    private Double minimumRate;
    private Double cleaningFee;
}