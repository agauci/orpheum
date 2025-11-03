package com.orpheum.benchmark.competitor.service;

import com.google.common.base.Strings;
import com.orpheum.benchmark.competitor.model.AggregateCompetitorGroupReport;
import com.orpheum.benchmark.competitor.model.CompetitorGroupReport;
import com.orpheum.benchmark.competitor.model.CompetitorReport;
import com.orpheum.benchmark.competitor.repository.CompetitorGroupReportRepository;
import com.orpheum.benchmark.competitor.repository.CompetitorReportRepository;
import com.orpheum.benchmark.competitor.support.CompetitorReportGenerator;
import com.orpheum.benchmark.competitor.model.CompetitorStatistics;
import com.orpheum.benchmark.competitor.support.CompetitorStatisticsExtractor;
import com.orpheum.benchmark.config.BenchmarkProperties;
import com.orpheum.benchmark.config.CompetitorConfig;
import com.orpheum.benchmark.config.CompetitorGroup;
import com.orpheum.benchmark.model.CalendarDay;
import com.orpheum.benchmark.model.CalendarMonth;
import com.orpheum.benchmark.model.PriceSpan;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.awt.event.InputEvent;

import static java.util.Collections.EMPTY_MAP;
import static org.awaitility.Awaitility.await;

/**
 * Service for analyzing competitor data from Airbnb.
 * 
 * Required dependencies:
 * - org.seleniumhq.selenium:selenium-java
 * - io.github.bonigarcia:webdrivermanager
 */
@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class CompetitorAnalysisService {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    BenchmarkProperties benchmarkProperties;
    CompetitorGroupReportRepository competitorGroupReportRepository;
    CompetitorReportRepository competitorReportRepository;

    @Scheduled(fixedRateString = "1",
                initialDelayString = "0",
                timeUnit = TimeUnit.MINUTES)
    public void processReports() {
        List<PriceSpan> groupPriceSpans = new ArrayList<>();
        List<CompetitorData> competitorAnalysisData = new ArrayList<>();

        Map<String, CompetitorGroup> finalCompetitorGroups;
        if (!Strings.isNullOrEmpty(benchmarkProperties.getRunOnly())) {
            log.info("Running only competitor group {}", benchmarkProperties.getRunOnly());
            finalCompetitorGroups = Map.of(benchmarkProperties.getRunOnly(), benchmarkProperties.getCompetitorGroups().get(benchmarkProperties.getRunOnly()));
        } else {
            finalCompetitorGroups = benchmarkProperties.getCompetitorGroups();
        }

        finalCompetitorGroups.forEach((groupKey, group) -> {
            Optional<CompetitorGroupReport> latestCompetitorGroupReport = competitorGroupReportRepository.findFirstByGroupIdOrderByIdDesc(groupKey);
            if (latestCompetitorGroupReport.isPresent() && Duration.between(latestCompetitorGroupReport.get().getTimestampGenerated(), LocalDateTime.now()).toMinutes() < 1440) {
                log.trace("Skipping group {} as it has been last processed at {}", groupKey, latestCompetitorGroupReport.get().getTimestampGenerated());
            } else {
                log.debug("Starting processing of group {}", groupKey);

                if (group.getCompetitors() != null && !group.getCompetitors().isEmpty()) {
                    group.getCompetitors().forEach((competitorKey, competitorConfig) -> {
                        Pair<CompetitorStatistics, Map<Month, CalendarMonth>> competitorData = extractCompetitorData(competitorConfig);
                        
                        if (competitorData.getLeft() != null) {
                            groupPriceSpans.addAll(competitorData.getLeft().spans());    
                        }
                        
                        competitorAnalysisData.add(new CompetitorData(competitorData != null ? competitorData.getLeft() : null, competitorData.getRight(), competitorConfig));
                    });
                }

                CompetitorGroupReport groupReport = competitorGroupReportRepository.save(
                    CompetitorGroupReport.create(
                        null,
                        groupKey,
                        group.getTitle(),
                        CompetitorReportGenerator.generateGroupReport(group, CompetitorStatisticsExtractor.compute(groupPriceSpans))
                    ).markAsNew()
                );

                competitorAnalysisData.forEach(data ->
                    competitorReportRepository.save(
                        CompetitorReport.create(
                            null,
                            groupReport.getId(),
                            data.competitorConfig.getKey(),
                            data.competitorConfig.getTitle(),
                            CompetitorReportGenerator.generateCompetitorReport(data.competitorConfig, data.competitorStatistics, data.calendarMonths)
                        ).markAsNew()
                    )
                );

                log.debug("Completed processing of group {}", groupKey);
            }
            groupPriceSpans.clear();
            competitorAnalysisData.clear();
        });
    }

    public AggregateCompetitorGroupReport pullCompetitorGroupReport(String competitorGroupId, LocalDate reportDay) {
        LocalDateTime startOfDay = reportDay.atStartOfDay();
        Optional<CompetitorGroupReport> competitorGroupReport = competitorGroupReportRepository.findFirstByGroupIdAndDay(competitorGroupId, startOfDay, startOfDay.plusDays(1));

        if (competitorGroupReport.isPresent()) {
            CompetitorGroupReport groupReport = competitorGroupReport.get();
            return new AggregateCompetitorGroupReport(groupReport, competitorReportRepository.findByCompetitorGroupReportId(groupReport.getId()));
        } else {
            return new AggregateCompetitorGroupReport(null, null);
        }
    }

    public List<LocalDate> getAvailableReportDays(String competitorGroupId) {
        return competitorGroupReportRepository.findAllByGroupId(competitorGroupId).stream()
                .map(CompetitorGroupReport::getTimestampGenerated)
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toList());
    }

    /**
     * Extracts competitor data from an Airbnb listing.
     * 
     * @return Updated competitor data with extracted information
     */
    private Pair<CompetitorStatistics, Map<Month, CalendarMonth>> extractCompetitorData(CompetitorConfig competitorConfig) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);

        try {
            log.info("Extracting competitor data for {}", competitorConfig.getKey());

            if (competitorConfig.getUrl() == null || competitorConfig.getUrl().isBlank()) {
                return Pair.of(null, EMPTY_MAP);
            }

            // Navigate to the URL (using the sample URL from the issue description)
            driver.get(competitorConfig.getUrl());

            // Wait for the page to load
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[aria-label='Close']")));
                WebElement closeButton = driver.findElement(By.cssSelector("button[aria-label='Close']"));
                closeButton.click();
            } catch (TimeoutException e) {
                log.info("Unable to find close button within 10 seconds, skipping close button click");
            }

            Thread.sleep(1000);

            // Dismiss pop up
            WebElement acceptTermsButton = driver.findElement(By.xpath("//button[normalize-space(text())='Accept all']"));
            acceptTermsButton.click();

            Thread.sleep(1000);

            Map<Month, CalendarMonth> monthCalendarMonthMap = extractCalendarMonths(driver);

            List<PriceSpan> priceSpans;
            if (competitorConfig.isOpenForBookings()) {
                WebElement calendarElement = driver.findElement(By.cssSelector("div[data-testid='inline-availability-calendar']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", calendarElement);

                Set<Integer> calendarMonthsInteractions = new HashSet<>();
                LocalDate now = LocalDate.now();
                calendarMonthsInteractions.add(now.getMonth().getValue());
                calendarMonthsInteractions.add(now.getMonth().plus(1).getValue());

                priceSpans = generateAvailableSpans(monthCalendarMonthMap, competitorConfig.getWindowSize(), driver, calendarMonthsInteractions, competitorConfig);
            } else {
                priceSpans = Collections.emptyList();
                log.info("Competitor {} is not open for bookings, skipping price span extraction", competitorConfig.getKey());
            }

            return Pair.of(CompetitorStatisticsExtractor.compute(priceSpans), monthCalendarMonthMap);
        } catch (Exception e) {
            log.error("Failed to extract competitor data", e);
            return null;
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    private static Map<Month, CalendarMonth> extractCalendarMonths(WebDriver driver) {
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsAhead = today.plusMonths(3).withDayOfMonth(today.plusMonths(3).lengthOfMonth());

        List<WebElement> days = driver.findElements(By.cssSelector("[data-testid^='calendar-day-']"));
        TreeMap<Month, List<CalendarDay>> monthEntries = days.stream()
                .map(el -> {
                    try {
                        // Example: "calendar-day-03/18/2025"
                        String testId = el.getDomAttribute("data-testid");
                        String datePart = testId.replace("calendar-day-", "").trim();
                        LocalDate date = LocalDate.parse(datePart, DATE_FORMATTER);

                        boolean available = !"true".equalsIgnoreCase(el.getDomAttribute("data-is-day-blocked"));

                        return new CalendarDay(
                                date.getDayOfMonth(),
                                date.getMonthValue(),
                                date.getYear(),
                                date.getDayOfWeek(),
                                available
                        );
                    } catch (Exception e) {
                        log.error("Failed to parse calendar day: " + el.getDomAttribute("data-testid"), e);
                        // Skip malformed entries
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(day -> {
                    LocalDate date = LocalDate.of(day.year(), day.month(), day.dayOfMonth());
                    return !date.isBefore(today) && !date.isAfter(threeMonthsAhead);
                })
                .distinct()
                .collect(Collectors.groupingBy(day -> Month.of(day.month()),
                        TreeMap::new, // Keeps months in calendar order
                        Collectors.toList()));

        Map<Month, CalendarMonth> result = new TreeMap<>();

        for (Map.Entry<Month, List<CalendarDay>> entry : monthEntries.entrySet()) {
            Month month = entry.getKey();
            List<CalendarDay> monthDays = entry.getValue();

            // Compute occupancyRate = (unavailableDays / totalDays)
            long totalDays = monthDays.size();
            long unavailableDays = monthDays.stream().filter(d -> !d.available()).count();

            BigDecimal occupancyRate = BigDecimal.ZERO;
            if (totalDays > 0) {
                occupancyRate = BigDecimal.valueOf((double) unavailableDays / totalDays);
                occupancyRate = occupancyRate.setScale(2, BigDecimal.ROUND_HALF_UP); // e.g. 0.42
            }

            result.put(month, new CalendarMonth(entry.getValue(), occupancyRate));
        }

        return result;
    }

    private List<PriceSpan> generateAvailableSpans(Map<Month, CalendarMonth> months, int windowSize, WebDriver driver, Set<Integer> calendarMonthsInteractions, CompetitorConfig competitorConfig) {
        List<PriceSpan> spans = new ArrayList<>();
        // A 3-night stay means that you are booking on the first day, and checking out on the last day, 3 nights later.
        // This results in booking 4 days.
        int trueWindowSize = windowSize + 1;

        List<CalendarDay> days = months.values().stream().flatMap(month -> month.calendarDays().stream())
                // Sort days chronologically (just to be safe)
                .sorted(Comparator.comparing(d -> LocalDate.of(d.year(), d.month(), d.dayOfMonth())))
                .toList();

        // We’ll track available sequences
        for (int i = 0; i <= days.size() - trueWindowSize; i++) {
            boolean isProcessingComplete = false;
            Integer appliedWindowSize = trueWindowSize;

            while (!isProcessingComplete) {
                if (i + appliedWindowSize > days.size()) break;

                List<CalendarDay> window = days.subList(i, i + appliedWindowSize);

                boolean allAvailable = window.stream().allMatch(CalendarDay::available);
                if (!allAvailable) break;

                CalendarDay start = window.get(0);
                CalendarDay end = window.get(appliedWindowSize - 1);

                boolean includesWeekend = window.stream()
                        .anyMatch(d -> d.dayOfWeek() == DayOfWeek.SATURDAY || d.dayOfWeek() == DayOfWeek.SUNDAY);

                PriceExtractionResult priceExtractionResult = extractPriceForSpan(start, end, appliedWindowSize, driver, calendarMonthsInteractions, competitorConfig);

                if (PriceExtractionOutcome.SUCCESS.equals(priceExtractionResult.outcome())) {
                    spans.add(new PriceSpan(
                            start.month(),
                            start.year(),
                            start.dayOfMonth(),
                            start.dayOfWeek(),
                            end.month(),
                            end.year(),
                            end.dayOfMonth(),
                            end.dayOfWeek(),
                            includesWeekend,
                            windowSize,
                            priceExtractionResult.price,
                            priceExtractionResult.pricePerDay
                    ));

                    isProcessingComplete = true;
                } else if (PriceExtractionOutcome.IMPOSSIBLE_SPAN.equals(priceExtractionResult.outcome())) {
                    log.info("Encountered impossible span for property {}, starting on date {}, with window size {}. Skipping.", competitorConfig.getKey(), start, appliedWindowSize);
                    isProcessingComplete = true;
                } else if (PriceExtractionOutcome.BOOKING_WINDOW_TOO_SMALL.equals(priceExtractionResult.outcome())) {
                    appliedWindowSize = priceExtractionResult.minimumWindowSize() + 1;
                }
            }
        }

        return spans;
    }

    @SneakyThrows
    private PriceExtractionResult extractPriceForSpan(CalendarDay start, CalendarDay end, Integer windowSize, WebDriver driver, Set<Integer> calendarMonthsInteractions, CompetitorConfig competitorConfig) {
        log.info("Extracting price for span {}/{}/{}-{}/{}/{}, with window size {}", start.dayOfMonth(), start.month(), start.year(), end.dayOfMonth(), end.month(), end.year(), windowSize);

        if ((!calendarMonthsInteractions.contains(start.month()) || !calendarMonthsInteractions.contains(end.month())) && calendarMonthsInteractions.size() != 1) {
            WebElement nextMonthButton = driver.findElement(By.cssSelector("button[aria-label='Move forward to switch to the next month.']"));
            nextMonthButton.click();

            Thread.sleep(1000);
        }
        calendarMonthsInteractions.add(end.month());

        WebElement startCalendarElement = driver.findElement(By.cssSelector("div[data-testid='calendar-day-" + padNumber(start.month()) + "/" + padNumber(start.dayOfMonth()) + "/" + start.year() + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startCalendarElement);
        if (!isCheckinAllowed(driver, start)) {
            return new PriceExtractionResult(PriceExtractionOutcome.IMPOSSIBLE_SPAN, null, null, windowSize);
        }

        if (windowSize > 2) {
            // Ensure the availability text confirms the correctness of the span. If not, extract the minimum span size.
            final WebElement availabilityCalendar = driver.findElement(By.cssSelector("div[data-testid='availability-calendar-date-range']"));
            // If the current window size is smaller than the minimum window size, then moving the calendar by one day during the next loop will not generate the
            // Minimum Stay: text, since it is still valid.
            await()
                    .atMost(Duration.ofSeconds(10))
                    .pollInterval(Duration.ofMillis(500))
                    .until(() -> availabilityCalendar.getText().contains("Minimum stay") ||
                                    availabilityCalendar.getText().contains(start.year().toString()) ||
                                        availabilityCalendar.getText().contains("The closest available"));
            Integer requiredWindowSize = extractMinimumWindowSize(availabilityCalendar.getText());
            if (requiredWindowSize >= windowSize) {
                log.info("Extending window size to {} for property {}, starting on date {}", requiredWindowSize + 1, competitorConfig.getKey(), start);
                return new PriceExtractionResult(PriceExtractionOutcome.BOOKING_WINDOW_TOO_SMALL, null, null, requiredWindowSize);
            } else if (availabilityCalendar.getText().contains("The closest available")) {
                return new PriceExtractionResult(PriceExtractionOutcome.IMPOSSIBLE_SPAN, null, null, requiredWindowSize);
            }
        }

        WebElement endCalendarElement = driver.findElement(By.cssSelector("div[data-testid='calendar-day-" + padNumber(end.month()) + "/" + padNumber(end.dayOfMonth()) + "/" + end.year() + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", endCalendarElement);

        BigDecimal spanPrice = safelyExtractAmount(driver, windowSize, 0);
        log.info("Extracted price {} for span {}/{}/{}-{}/{}/{}, with window size {}", spanPrice, start.dayOfMonth(), start.month(), start.year(), end.dayOfMonth(), end.month(), end.year(), windowSize);

        return new PriceExtractionResult(PriceExtractionOutcome.SUCCESS, spanPrice, spanPrice.divide(BigDecimal.valueOf(windowSize - 1), 2, BigDecimal.ROUND_HALF_UP), null);
    }

    private String padNumber(Integer num) {
        return (num <= 9) ? "0" + num : num.toString();
    }

    private Integer extractMinimumWindowSize(String text) {
        if (text == null || text.isBlank() || !text.contains("Minimum stay:")) {
            return -1;
        }

        return Integer.valueOf(text.replace("Minimum stay:", "")
                .replace("nights", "")
                .replace("night", "")
                .trim());
    }

    private BigDecimal safelyExtractAmount(WebDriver driver, Integer windowSize, Integer failureCount) throws StaleElementReferenceException {
        try {
            // Ensure that the amount being extracted is for the correct time span size
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//div[@data-testid='book-it-default']//*[contains(text(), 'for " + (windowSize - 1) + " night')]")
                    ));

            WebElement price = new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("[data-testid='book-it-default'] button[role='button'] > span:first-child")
                    ));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            String text = (String) js.executeScript("return arguments[0].innerText;", price);
            return parsePrice(text);
        } catch (StaleElementReferenceException e) {
            log.warn("Failed to extract price after {} attempts", failureCount);
            if (failureCount < 3) {
                return safelyExtractAmount(driver, windowSize, failureCount + 1);
            } else {
                throw e;
            }
        }
    }

    private boolean isCheckinAllowed(WebDriver driver, CalendarDay start) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(d -> {
            WebElement startCalendarElement = driver.findElement(By.cssSelector("div[data-testid='calendar-day-" + padNumber(start.month()) + "/" + padNumber(start.dayOfMonth()) + "/" + start.year() + "']"));
            WebElement parent = startCalendarElement.findElement(By.xpath(".."));

            String ariaLabel = parent.getDomAttribute("aria-label");
            return ariaLabel != null && ariaLabel.contains(start.year().toString());
        });

        WebElement startCalendarElement = driver.findElement(By.cssSelector("div[data-testid='calendar-day-" + padNumber(start.month()) + "/" + padNumber(start.dayOfMonth()) + "/" + start.year() + "']"));
        WebElement parent = startCalendarElement.findElement(By.xpath(".."));
        String parentAriaLabel = parent.getDomAttribute("aria-label");

        if (parentAriaLabel != null &&
                (parentAriaLabel.toLowerCase().contains("this day is only available for checkout") || parentAriaLabel.toLowerCase().contains("no eligible checkout date"))) {
            return false;
        }

        return true;
    }

    private void clickWithRobot(Integer xLocation, Integer yLocation) throws AWTException, InterruptedException {
        // Click with Robot
        Robot robot = new Robot();
        robot.mouseMove(xLocation, yLocation);
        Thread.sleep(1000);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1000);
    }

    private static BigDecimal parsePrice(String priceText) {
        if (priceText == null || priceText.isBlank()) {
            return BigDecimal.ZERO;
        }

        // Remove currency symbols, spaces, and non-numeric characters except comma/dot
        String numeric = priceText.replaceAll("[^\\d.,-]", "");

        // Handle European-style commas (e.g. "1.234,56" or "631,50")
        if (numeric.contains(",") && numeric.contains(".")) {
            // Assume '.' = thousands separator, ',' = decimal
            numeric = numeric.replace(".", "").replace(",", ".");
        } else if (numeric.contains(",")) {
            // Assume ',' = decimal
            numeric = numeric.replace(",", ".");
        }

        return new BigDecimal(numeric);
    }

    private record CompetitorData(CompetitorStatistics competitorStatistics, Map<Month, CalendarMonth> calendarMonths, CompetitorConfig competitorConfig) { }

    private record PriceExtractionResult(PriceExtractionOutcome outcome, BigDecimal price, BigDecimal pricePerDay, Integer minimumWindowSize) { }

    private enum PriceExtractionOutcome {
        SUCCESS,
        BOOKING_WINDOW_TOO_SMALL,
        IMPOSSIBLE_SPAN
    }

}
