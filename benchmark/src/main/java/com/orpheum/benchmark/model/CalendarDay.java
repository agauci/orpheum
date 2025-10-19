package com.orpheum.benchmark.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;

public record CalendarDay(Integer dayOfMonth, Integer month, Integer year, DayOfWeek dayOfWeek, Boolean available) {
}
