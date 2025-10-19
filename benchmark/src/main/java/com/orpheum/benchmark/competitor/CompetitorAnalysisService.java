package com.orpheum.benchmark.competitor;

import com.orpheum.benchmark.model.CalendarDay;
import com.orpheum.benchmark.model.CalendarMonth;
import com.orpheum.benchmark.model.PriceSpan;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.InputEvent;

/**
 * Service for analyzing competitor data from Airbnb.
 * 
 * Required dependencies:
 * - org.seleniumhq.selenium:selenium-java
 * - io.github.bonigarcia:webdrivermanager
 */
@Component
@Slf4j
public class CompetitorAnalysisService {

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @PostConstruct
    public void init() {
        System.out.println(extractCompetitorData());
    }

    /**
     * Extracts competitor data from an Airbnb listing.
     * 
     * @return Updated competitor data with extracted information
     */
    public CompetitorStatistics extractCompetitorData() {
        // Initialize WebDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--ignore-certificate-errors");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        System.setProperty("java.awt.headless", "false");

        try {
            // Navigate to the URL (using the sample URL from the issue description)
            String url = "https://www.airbnb.com.mt/rooms/1405209871580505002";
            driver.get(url);

            // Wait for the page to load
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[aria-label='Close']")));
            WebElement closeButton = driver.findElement(By.cssSelector("button[aria-label='Close']"));
            closeButton.click();

            Thread.sleep(1000);

            // Dismiss pop up
            WebElement acceptTermsButton = driver.findElement(By.xpath("//button[normalize-space(text())='Accept all']"));
            acceptTermsButton.click();

            Thread.sleep(1000);

            Map<Month, CalendarMonth> monthCalendarMonthMap = extractCalendarMonths(driver);

            List<PriceSpan> priceSpans = generateAvailableSpans(monthCalendarMonthMap, 3);

            WebElement calendarElement = driver.findElement(By.cssSelector("div[data-testid='inline-availability-calendar']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", calendarElement);

            Set<Integer> calendarMonthsInteractions = new HashSet<>();
            calendarMonthsInteractions.add(priceSpans.get(0).startCalendarMonth());
            calendarMonthsInteractions.add(priceSpans.get(0).endCalendarMonth());
            List<PriceSpan> decoratedPriceSpans = priceSpans.stream().map(span -> extractPriceForSpan(span, driver, calendarMonthsInteractions)).toList();

            System.out.println(CompetitorReportGenerator.generateReport(
                    CompetitorStatisticsExtractor.compute(decoratedPriceSpans),
                    monthCalendarMonthMap
            ));

            return null;
        } catch (Exception e) {
            e.printStackTrace();
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

    public static List<PriceSpan> generateAvailableSpans(Map<Month, CalendarMonth> months, int windowSize) {
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
            List<CalendarDay> window = days.subList(i, i + trueWindowSize);

            boolean allAvailable = window.stream().allMatch(CalendarDay::available);
            if (!allAvailable) continue;

            CalendarDay start = window.get(0);
            CalendarDay end = window.get(trueWindowSize - 1);

            boolean includesWeekend = window.stream()
                    .anyMatch(d -> d.dayOfWeek() == DayOfWeek.SATURDAY || d.dayOfWeek() == DayOfWeek.SUNDAY);

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
                    null,
                    null
            ));
        }

        return spans;
    }

    @SneakyThrows
    public PriceSpan extractPriceForSpan(PriceSpan span, WebDriver driver, Set<Integer> calendarMonthsInteractions) {
        WebElement startCalendarElement = driver.findElement(By.cssSelector("div[data-testid='calendar-day-" + span.startCalendarMonth() + "/" + padDay(span.startDay()) + "/" + span.startCalendarYear() + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", startCalendarElement);

        if ((!calendarMonthsInteractions.contains(span.startCalendarMonth()) || !calendarMonthsInteractions.contains(span.endCalendarMonth())) && calendarMonthsInteractions.size() != 1) {
            WebElement nextMonthButton = driver.findElement(By.cssSelector("button[aria-label='Move forward to switch to the next month.']"));
            nextMonthButton.click();

            Thread.sleep(1000);
        }
        calendarMonthsInteractions.add(span.endCalendarMonth());

        WebElement endCalendarElement = driver.findElement(By.cssSelector("div[data-testid='calendar-day-" + span.endCalendarMonth() + "/" + padDay(span.endDay()) + "/" + span.endCalendarYear() + "']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", endCalendarElement);

        WebElement price = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("[data-testid='book-it-default'] span[style*='--pricing-guest-primary-line-unit-price-text-decoration: none']")
                ));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String text = (String) js.executeScript("return arguments[0].innerText;", price);
        BigDecimal spanPrice = parsePrice(text);

        return span.toBuilder()
                .price(spanPrice)
                .pricePerDay(spanPrice.divide(BigDecimal.valueOf(span.length()), 2, BigDecimal.ROUND_HALF_UP))
                .build();
    }

    public String padDay(Integer day) {
        return (day <= 9) ? "0" + day : day.toString();
    }

    public void clickWithRobot(Integer xLocation, Integer yLocation) throws AWTException, InterruptedException {
        // Click with Robot
        Robot robot = new Robot();
        robot.mouseMove(xLocation, yLocation);
        Thread.sleep(1000);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Thread.sleep(1000);
    }

    public static BigDecimal parsePrice(String priceText) {
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
}
