package com.orpheum.benchmark.competitor.repository;

import com.orpheum.benchmark.competitor.model.CompetitorGroupReport;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing CompetitorGroupReport entities.
 */
public interface CompetitorGroupReportRepository extends CrudRepository<CompetitorGroupReport, Long> {

    Optional<CompetitorGroupReport> findFirstByGroupIdOrderByIdDesc(String groupId);

    @Query("""
        SELECT * FROM competitor_group_report
        WHERE group_id = :groupId
          AND timestamp_generated >= :startOfDay
          AND timestamp_generated < :endOfDay
        ORDER BY id DESC
        LIMIT 1
    """)
    Optional<CompetitorGroupReport> findFirstByGroupIdAndDay(
            @Param("groupId") String groupId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    List<CompetitorGroupReport> findAllByGroupId(String groupId);

}
