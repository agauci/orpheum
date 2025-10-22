package com.orpheum.benchmark.competitor.model;

import com.orpheum.benchmark.model.AbstractEntity;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

/**
 * Model class representing the competitor_group_report table.
 */
@Value
@With
public class CompetitorGroupReport extends AbstractEntity<Long, CompetitorGroupReport> {
    @Id
    Long id;
    String groupId;
    String groupTitle;
    String groupReport;

    @CreatedDate
    LocalDateTime timestampGenerated;

    @PersistenceCreator
    public static CompetitorGroupReport create(Long id, String groupId, String groupTitle, String groupReport) {
        return new CompetitorGroupReport(id, groupId, groupTitle, groupReport, null);
    }

}
