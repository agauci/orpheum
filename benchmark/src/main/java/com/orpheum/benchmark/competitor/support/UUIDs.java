package com.orpheum.benchmark.competitor.support;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDs {

    public static UUID create() {
        // Generates a UUID v7
        return UuidCreator.getTimeOrderedEpoch();
    }

    public static UUID fromString(String uuid) {
        return UuidCreator.fromString(uuid);
    }

}

