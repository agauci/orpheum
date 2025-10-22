package com.orpheum.benchmark.competitor.model;

import java.util.List;

public record AggregateCompetitorGroupReport(CompetitorGroupReport competitorGroupReport, List<CompetitorReport> competitorReports) {
}
