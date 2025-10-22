package com.orpheum.benchmark.competitor.repository;

import com.orpheum.benchmark.competitor.model.CompetitorReport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for managing CompetitorReport entities.
 */
public interface CompetitorReportRepository extends CrudRepository<CompetitorReport, Long> {

    List<CompetitorReport> findByCompetitorGroupReportId(Long competitorGroupReportId);

}
