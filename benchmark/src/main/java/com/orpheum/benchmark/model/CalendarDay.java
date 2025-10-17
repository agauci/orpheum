package com.orpheum.benchmark.model;

import java.math.BigDecimal;
import java.time.DayOfWeek;

public record CalendarDay(Integer dayOfMonth, DayOfWeek dayOfWeek, BigDecimal price) {
}
