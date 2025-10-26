package com.orpheum.benchmark.model;

public enum PricingStrategyMode {

    CONSERVATIVE("A strategy which prioritises occupancy, even at the cost of suppressed rates"),
    BALANCED("A strategy focused on maximising ADR, attempting to find an optimal balance between occupancy and daily rates"),
    AGGRESSIVE("A strategy which focuses on getting the highest daily rates, at the risk of supressing occupancy");

    String description;

    PricingStrategyMode(String description) {
        this.description = description;
    }

    public String getFullDescription() {
        return this.name() + " - " + this.description;
    }

}
