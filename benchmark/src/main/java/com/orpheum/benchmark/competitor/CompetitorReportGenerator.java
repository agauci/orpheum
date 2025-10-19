package com.orpheum.benchmark.competitor;

import com.orpheum.benchmark.model.CalendarDay;
import com.orpheum.benchmark.model.CalendarMonth;
import com.orpheum.benchmark.model.PriceSpan;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CompetitorReportGenerator {

    public static String generateReport(
            CompetitorStatistics stats,
            Map<Month, CalendarMonth> calendarMonths
    ) {
        List<PriceSpan> upcoming14DaysSpans = stats.spans().stream()
                .sorted(Comparator.comparing(CompetitorReportGenerator::toLocalDate))
                .filter(ps -> isWithinDays(ps, LocalDate.now(), 0, 14))
                .toList();

        StringBuilder md = new StringBuilder();

        // ===== HEADER =====
        md.append("# Competitor Pricing & Availability Report\n\n");

        // ===== IMMEDIATE TERM (0-7 days) =====
        md.append("## Immediate Term (Next 7 Days)\n");
        md.append("| Metric | Value |\n");
        md.append("|--------|-------|\n");
        md.append("| Average Price | ").append(format(stats.averagePriceNext7Days())).append(" |\n");
        md.append("| Average Non-Weekend Price | ").append(format(stats.averageNonWeekendPriceNext7Days())).append(" |\n");
        md.append("| Average Including Weekend | ").append(format(stats.averagePriceIncludingWeekendDaysNext7Days())).append(" |\n");
        md.append("| Std Dev | ").append(format(stats.stdDevPriceNext7Days())).append(" |\n");
        md.append("| Median Price | ").append(format(stats.medianPriceNext7Days())).append(" |\n");
        md.append("| Weekend Premium (%) | ").append(format(stats.weekendPremiumRatioNext7Days())).append(" |\n\n");

        // ===== SHORT TERM (7-14 days) =====
        md.append("## Short Term (Next 7-14 Days)\n");
        md.append("| Metric | Value |\n");
        md.append("|--------|-------|\n");
        md.append("| Average Price | ").append(format(stats.averagePrice7To14Days())).append(" |\n");
        md.append("| Average Non-Weekend Price | ").append(format(stats.averageNonWeekendPriceNext7To14Days())).append(" |\n");
        md.append("| Average Including Weekend | ").append(format(stats.averagePriceIncludingWeekendDaysNext7To14Days())).append(" |\n");
        md.append("| Percentage Change vs First 7 Days (%) | ").append(format(stats.percentageChangeOverFirstAndSecond7Days())).append(" |\n\n");

        // ===== MONTHLY PROJECTIONS =====
        md.append("## Monthly Projections\n");
        md.append("| Month | Average Price |\n");
        md.append("|-------|---------------|\n");
        md.append("| This Month | ").append(format(stats.averagePriceThisMonth())).append(" |\n");
        md.append("| Next Month | ").append(format(stats.averagePriceNextMonth())).append(" |\n");
        md.append("| In 2 Months | ").append(format(stats.averagePriceIn2Months())).append(" |\n\n");

        // ===== CALENDAR BY MONTH =====
        md.append("## Monthly Availability\n");
        for (Month month : Month.values()) {
            CalendarMonth cm = calendarMonths.get(month);
            if (cm == null) continue;

            md.append("### ").append(month).append(" ").append(cm.calendarDays().isEmpty() ? "" : cm.calendarDays().get(0).year()).append("\n");
            md.append("Occupancy Rate: ").append(cm.occupancyRate().multiply(BigDecimal.valueOf(100)).setScale(1)).append("%\n\n");

            md.append("| ");
            for (DayOfWeek dow : DayOfWeek.values()) {
                md.append(dow.toString().substring(0, 3)).append(" | ");
            }
            md.append("\n|");
            for (int i = 0; i < 7; i++) md.append("----|");
            md.append("\n");

            int currentWeekDay = cm.calendarDays().get(0).dayOfWeek().getValue() - 1; // Monday=1, Sunday=7
            md.append("| ");
            for (int i = 0; i < currentWeekDay; i++) md.append("    | ");

            for (int i = 0; i < cm.calendarDays().size(); i++) {
                CalendarDay day = cm.calendarDays().get(i);

                String avail = day.available() ? String.valueOf(day.dayOfMonth()) : "X";
                md.append(avail).append(" | ");
                if (day.dayOfWeek() == DayOfWeek.SUNDAY && i < cm.calendarDays().size() - 1) {
                    md.append("\n| ");
                }
            }
            md.append("\n\n");
        }

        // ===== UPCOMING 14-DAY PRICE SPANS =====
        md.append("## Upcoming 14-Day Price Spans\n");
        md.append("| From | To | Price/Day | Includes Weekend |\n");
        md.append("|------|----|-----------|-----------------|\n");

        for (PriceSpan ps : upcoming14DaysSpans) {
            String from = String.format("%d-%02d-%02d", ps.startCalendarYear(), ps.startCalendarMonth(), ps.startDay());
            String to = String.format("%d-%02d-%02d", ps.endCalendarYear(), ps.endCalendarMonth(), ps.endDay());
            md.append("| ").append(from)
                    .append(" | ").append(to)
                    .append(" | ").append(format(ps.pricePerDay()))
                    .append(" | ").append(ps.includesWeekend() ? "Yes" : "No")
                    .append(" |\n");
        }

        return md.toString();
    }

    private static String format(BigDecimal value) {
        if (value == null) return "N/A";
        return "€" + value.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    private static LocalDate toLocalDate(PriceSpan ps) {
        return LocalDate.of(ps.startCalendarYear(), ps.startCalendarMonth(), ps.startDay());
    }

    private static boolean isWithinDays(PriceSpan ps, LocalDate base, int offset, int windowDays) {
        LocalDate start = toLocalDate(ps);
        LocalDate lower = base.plusDays(offset);
        LocalDate upper = lower.plusDays(windowDays);
        return !start.isBefore(lower) && start.isBefore(upper);
    }

}
