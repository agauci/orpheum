package com.orpheum.benchmark.airgpt.service.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.uuid.exception.InvalidUuidException;
import com.orpheum.benchmark.airgpt.service.AirGptChatMemory;
import com.orpheum.benchmark.competitor.model.AggregateCompetitorGroupReport;
import com.orpheum.benchmark.competitor.service.CompetitorAnalysisService;
import com.orpheum.benchmark.competitor.support.HtmlContentExtractor;
import com.orpheum.benchmark.competitor.support.UUIDs;
import com.orpheum.benchmark.config.BenchmarkConfig;
import com.orpheum.benchmark.config.BenchmarkProperties;
import com.orpheum.benchmark.config.CompetitorGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Component
@Slf4j
@RequiredArgsConstructor
public class AirGptTools {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final AirGptChatMemory chatMemory;
    private final BenchmarkProperties benchmarkProperties;
    private final CompetitorAnalysisService competitorAnalysisService;
    private final ObjectMapper objectMapper;

    @Tool(description = "Get the current date and time in UTC")
    String getCurrentDateTime(@ToolParam(description = "The conversation ID in UUID format") String conversationId) {
        return processToolCall(
                () -> LocalDateTime.now(ZoneOffset.UTC).toString(),
                conversationId,
                Map.of(param("conversationId"), conversationId),
                "getCurrentDateTime",
                true
        );
    }

    @Tool(description = "Retrieves the contents of any website")
    String getWebsite(@ToolParam(description = "The conversation ID in UUID format") String conversationId,
                      @ToolParam(description = "The website URL") String url) {
        return processToolCall(
                () -> HtmlContentExtractor.fetchAndClean(url),
                conversationId,
                Map.of(
                        param("conversationId"), conversationId,
                        param("url"), url
                ),
                "getWebsite",
                false
        );
    }

    @Tool(description = "Provides a summary of the available competitor groups, mapping a competitor group ID to its metadata. For each group, provides metadata including the level of finish, amenities view etc, and also gives a list of all competitor properties in each group, together with a title and URL for each,")
    Map<String, CompetitorGroup> getCompetitorMetadata(@ToolParam(description = "The conversation ID in UUID format") String conversationId) {
        return processToolCall(
                () -> benchmarkProperties.getCompetitorGroups(),
                conversationId,
                Map.of(
                        param("conversationId"), conversationId
                ),
                "getCompetitorMetadata",
                false
        );
    }

    @Tool(description = "Retrieves the competitor group report for a specific date, including both an aggregate report across competitor properties in the same group, as well as a more detailed report per competitor property including calendar availability")
    String getCompetitorGroupReport(@ToolParam(description = "The conversation ID in UUID format") String conversationId,
                                    @ToolParam(description = "The competitor group ID, as retrieved from getCompetitorMetadata") String competitorGroupId,
                                    @ToolParam(description = "The date when the report was generated, in the format yyyy/mm/dd") String reportGenerationDate) {
        return processToolCall(
                () -> {
                    AggregateCompetitorGroupReport aggregateCompetitorGroupReport = competitorAnalysisService.pullCompetitorGroupReport(competitorGroupId, LocalDate.parse(reportGenerationDate, DATE_TIME_FORMATTER));

                    if (aggregateCompetitorGroupReport.competitorGroupReport() == null) {
                        return "Unable to resolve a competitor group report on this day. Make sure that you pick a date with an available report as defined by tool getConsumeGroupAvailableReportDays.";
                    } else {
                        try {
                            return objectMapper.writeValueAsString(aggregateCompetitorGroupReport);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                conversationId,
                Map.of(
                        param("conversationId"), conversationId,
                        param("competitorGroupId"), competitorGroupId,
                        param("reportGenerationDate"), reportGenerationDate
                ),
                "getCompetitorGroupReport",
                false
        );
    }

    @Tool(description = "Returns the days when a report is available for the provided competitor group")
    List<LocalDate> getConsumeGroupAvailableReportDays(@ToolParam(description = "The conversation ID in UUID format") String conversationId,
                                                       @ToolParam(description = "The competitor group ID whose report day availability is required") String competitorGroupId) {
        return processToolCall(
                () -> competitorAnalysisService.getAvailableReportDays(competitorGroupId),
                conversationId,
                Map.of(
                        param("conversationId"), conversationId,
                        param("competitorGroupId"), competitorGroupId
                ),
                "getConsumeGroupAvailableReportDays",
                false
        );
    }

    private <T> T processToolCall(Supplier<T> responseValue,
                                  String conversationId,
                                  Map<String, Object> requestParams,
                                  String toolName,
                                  boolean storeOutcome) {
        try {
            log.debug("Started processing tool call for conversation {} [Name: {}, Request Parameters: {}]", conversationId, toolName, requestParams);
            long initialTime = System.currentTimeMillis();
            Optional<UUID> conversationIdUuid = processConversationId(conversationId);


            T result = responseValue.get();

            conversationIdUuid.ifPresent(uuid -> {
                chatMemory.add(conversationId, new ToolResponseMessage(
                        List.of(new ToolResponseMessage.ToolResponse("1", toolName, (storeOutcome) ? result.toString() : "")),
                        requestParams
                ));
            });

            log.debug("Completed processing tool call for conversation {} [Name: {}, Duration: {} ms]", conversationId, toolName, System.currentTimeMillis() - initialTime);

            return result;
        } catch (Exception e) {
            log.error("Error processing tool call for conversation {} [Name: {}, Request Parameters: {}]", conversationId, toolName, requestParams, e);
            throw e;
        }
    }

    private Optional<UUID> processConversationId(String conversationId) {
        try {
            return Optional.of(UUIDs.fromString(conversationId));
        } catch (InvalidUuidException e) {
            log.warn("Invalid conversation ID: {}. Unable to store tool call.", conversationId);
        }

        return Optional.empty();
    }

    private OffsetDateTime processOffsetDateTime(String dateTime) {
        try {
            TemporalAccessor parsed = DateTimeFormatter.ISO_DATE_TIME.parse(dateTime);

            return OffsetDateTime.parse((parsed.isSupported(ChronoField.OFFSET_SECONDS)) ? dateTime : dateTime + "Z");
        } catch (Exception e) {
            log.warn("Invalid date and time: {}. Unable to process tool call.", dateTime);
            throw e;
        }
    }

    private String param(String paramName) {
        return paramName + "_param";
    }

}
