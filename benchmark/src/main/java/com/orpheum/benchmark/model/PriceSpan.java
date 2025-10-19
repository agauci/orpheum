package com.orpheum.benchmark.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.DayOfWeek;

@Builder(toBuilder = true)
public record PriceSpan(
    Integer startCalendarMonth,
    Integer startCalendarYear,
    Integer startDay,
    DayOfWeek startDayOfWeek,
    Integer endCalendarMonth,
    Integer endCalendarYear,
    Integer endDay,
    DayOfWeek endDayOfWeek,
    boolean includesWeekend,
    Integer length,
    BigDecimal price,
    BigDecimal pricePerDay
) {
}
