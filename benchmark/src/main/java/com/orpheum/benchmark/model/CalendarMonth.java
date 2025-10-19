package com.orpheum.benchmark.model;

import java.math.BigDecimal;
import java.util.List;

public record CalendarMonth(List<CalendarDay> calendarDays, BigDecimal occupancyRate) {
}
