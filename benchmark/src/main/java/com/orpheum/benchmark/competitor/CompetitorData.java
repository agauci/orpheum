package com.orpheum.benchmark.competitor;

import com.orpheum.benchmark.model.CalendarDay;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;

public record CompetitorData(
        Map<Month, List<CalendarDay>> upcomingMonthPrices,
        BigDecimal averagePriceNext3Days,
        BigDecimal averagePriceNext5Days,
        BigDecimal averagePriceNext7Days,
        BigDecimal averagePriceNext14Days,
        BigDecimal averagePriceNextWeekend,
        BigDecimal averagePriceWeekendAfterNext,
        BigDecimal averagePriceNext7DaysAfter15Days
) {
}
