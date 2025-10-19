package com.orpheum.benchmark.competitor;

import com.orpheum.benchmark.model.PriceSpan;

import java.math.BigDecimal;
import java.util.List;

public record CompetitorStatistics(
        List<PriceSpan> spans,

        BigDecimal averagePriceNext7Days,
        BigDecimal averageNonWeekendPriceNext7Days,
        BigDecimal averagePriceIncludingWeekendDaysNext7Days,
        BigDecimal stdDevPriceNext7Days,
        BigDecimal medianPriceNext7Days,
        BigDecimal weekendPremiumRatioNext7Days,

        BigDecimal averagePrice7To14Days,
        BigDecimal averageNonWeekendPriceNext7To14Days,
        BigDecimal averagePriceIncludingWeekendDaysNext7To14Days,
        BigDecimal percentageChangeOverFirstAndSecond7Days,

        BigDecimal averagePriceThisMonth,
        BigDecimal averagePriceNextMonth,
        BigDecimal averagePriceIn2Months
) {
}
