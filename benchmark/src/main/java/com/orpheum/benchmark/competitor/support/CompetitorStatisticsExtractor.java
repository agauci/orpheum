package com.orpheum.benchmark.competitor.support;

import com.orpheum.benchmark.competitor.model.CompetitorStatistics;
import com.orpheum.benchmark.model.PriceSpan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class CompetitorStatisticsExtractor {

    public static CompetitorStatistics compute(List<PriceSpan> spans) {
        if (spans == null || spans.isEmpty()) {
            return emptyStats();
        }

        // Sort by start date
        spans = spans.stream()
                .sorted(Comparator.comparing(CompetitorStatisticsExtractor::toLocalDate))
                .toList();

        LocalDate minDate = LocalDate.now();

        // Build helper predicates
        Predicate<PriceSpan> next7 = ps -> isWithinDays(ps, minDate, 0, 7);
        Predicate<PriceSpan> days7to14 = ps -> isWithinDays(ps, minDate, 7, 7);
        Predicate<PriceSpan> thisMonth = ps -> isWithinMonth(ps, minDate, 0);
        Predicate<PriceSpan> nextMonth = ps -> isWithinMonth(ps, minDate, 1);
        Predicate<PriceSpan> in2Months = ps -> isWithinMonth(ps, minDate, 2);

        // --- NEXT 7 DAYS ---
        BigDecimal avg7 = average(spans, next7);
        BigDecimal avg7NonWeekend = average(spans, next7.and(ps -> !ps.includesWeekend()));
        BigDecimal avg7InclWeekend = average(spans, next7.and(PriceSpan::includesWeekend));
        BigDecimal std7 = standardDeviation(spans, next7);
        BigDecimal median7 = median(spans, next7);
        BigDecimal weekendPremium = (avg7InclWeekend.compareTo(BigDecimal.ZERO) != 0)  ? ratio(avg7InclWeekend, avg7NonWeekend) : BigDecimal.ZERO;

        // --- 7 TO 14 DAYS ---
        BigDecimal avg7to14 = average(spans, days7to14);
        BigDecimal avg7to14NonWeekend = average(spans, days7to14.and(ps -> !ps.includesWeekend()));
        BigDecimal avg7to14InclWeekend = average(spans, days7to14.and(PriceSpan::includesWeekend));
        BigDecimal pctChange7vs14 = percentageChange(avg7, avg7to14);

        // --- MONTHLY ---
        BigDecimal avgThisMonth = average(spans, thisMonth);
        BigDecimal avgNextMonth = average(spans, nextMonth);
        BigDecimal avgIn2Months = average(spans, in2Months);

        return new CompetitorStatistics(
                spans,
                avg7,
                avg7NonWeekend,
                avg7InclWeekend,
                std7,
                median7,
                weekendPremium,
                avg7to14,
                avg7to14NonWeekend,
                avg7to14InclWeekend,
                pctChange7vs14,
                avgThisMonth,
                avgNextMonth,
                avgIn2Months
        );
    }

    // ---------- Helper methods ----------

    private static LocalDate toLocalDate(PriceSpan ps) {
        return LocalDate.of(ps.startCalendarYear(), ps.startCalendarMonth(), ps.startDay());
    }

    private static boolean isWithinDays(PriceSpan ps, LocalDate base, int offset, int windowDays) {
        LocalDate start = toLocalDate(ps);
        LocalDate lower = base.plusDays(offset);
        LocalDate upper = lower.plusDays(windowDays);
        return !start.isBefore(lower) && start.isBefore(upper);
    }

    private static boolean isWithinMonth(PriceSpan ps, LocalDate base, Integer monthOffset) {
        LocalDate targetMonth = (monthOffset != 0) ? base.plusMonths(monthOffset) : base;

        return ps.startCalendarYear().equals(targetMonth.getYear())
                && ps.startCalendarMonth().equals(targetMonth.getMonthValue());
    }

    private static BigDecimal average(List<PriceSpan> spans, Predicate<PriceSpan> filter) {
        List<BigDecimal> values = spans.stream()
                .filter(filter)
                .map(PriceSpan::pricePerDay)
                .filter(Objects::nonNull)
                .toList();
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
    }

    private static BigDecimal standardDeviation(List<PriceSpan> spans, Predicate<PriceSpan> filter) {
        List<BigDecimal> values = spans.stream()
                .filter(filter)
                .map(PriceSpan::pricePerDay)
                .filter(Objects::nonNull)
                .toList();
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal mean = average(spans, filter);
        BigDecimal variance = values.stream()
                .map(v -> v.subtract(mean).pow(2))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), 6, RoundingMode.HALF_UP);

        double std = Math.sqrt(variance.doubleValue());
        return BigDecimal.valueOf(std).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal median(List<PriceSpan> spans, Predicate<PriceSpan> filter) {
        List<BigDecimal> values = spans.stream()
                .filter(filter)
                .map(PriceSpan::pricePerDay)
                .filter(Objects::nonNull)
                .sorted()
                .toList();
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int n = values.size();
        if (n % 2 == 1) return values.get(n / 2);
        BigDecimal sumMid = values.get(n / 2 - 1).add(values.get(n / 2));
        return sumMid.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
    }

    private static BigDecimal ratio(BigDecimal a, BigDecimal b) {
        if (b == null || b.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return a.divide(b, 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100)) // convert to %
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal percentageChange(BigDecimal a, BigDecimal b) {
        if (a == null || a.compareTo(BigDecimal.ZERO) == 0 || b == null) {
            return BigDecimal.ZERO;
        }

        return b.subtract(a)
                .divide(a, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static CompetitorStatistics emptyStats() {
        BigDecimal zero = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        return new CompetitorStatistics(Collections.EMPTY_LIST, zero, zero, zero, zero, zero, zero,
                zero, zero, zero, zero, zero, zero, zero);
    }

}
