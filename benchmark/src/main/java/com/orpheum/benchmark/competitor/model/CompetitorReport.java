package com.orpheum.benchmark.competitor.model;

import com.orpheum.benchmark.model.AbstractEntity;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Model class representing the competitor_report table.
 */
@Value
@With
public class CompetitorReport extends AbstractEntity<Long, CompetitorReport> {
    @Id
    Long id;
    Long competitorGroupReportId;
    String competitorId;
    String competitorTitle;
    String competitorReport;

    @CreatedDate
    LocalDateTime timestampGenerated;

    @PersistenceCreator
    public static CompetitorReport create(Long id, Long competitorGroupReportId, String competitorId, String competitorTitle, String competitorReport) {
        return new CompetitorReport(id, competitorGroupReportId, competitorId, competitorTitle, competitorReport, null);
    }

}
