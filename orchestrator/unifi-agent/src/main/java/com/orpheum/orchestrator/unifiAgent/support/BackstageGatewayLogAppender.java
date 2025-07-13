package com.orpheum.orchestrator.unifiAgent.support;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import com.orpheum.orchestrator.unifiAgent.model.BackstageLogEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BackstageGatewayLogAppender extends AppenderBase<ILoggingEvent> {

    private static final String SITE_FRIENDLY_NAME = ApplicationProperties.getString("site_friendly_name");

    private int batchSize;
    private long timeoutMillis;
    private int maxRetryCount;

    private final List<BackstageLogEntry> eventBuffer = Collections.synchronizedList(new ArrayList<>());
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private final AtomicLong lastSentTime = new AtomicLong(System.currentTimeMillis());
    private final AtomicBoolean sendingInProgress = new AtomicBoolean(false);
    private final AtomicInteger retryCount = new AtomicInteger(0);

    @Override
    public void start() {
        super.start();
        scheduler.scheduleAtFixedRate(this::checkTimeoutAndSend, timeoutMillis, timeoutMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        // Extract stacktrace if throwable is present
        String stacktrace = null;
        IThrowableProxy throwableProxy = eventObject.getThrowableProxy();
        if (throwableProxy != null) {
            stacktrace = ThrowableProxyUtil.asString(throwableProxy);
        }

        BackstageLogEntry logEvent = new BackstageLogEntry(
                eventObject.getFormattedMessage(),
                eventObject.getTimeStamp(),
                eventObject.getLevel().toString(),
                eventObject.getLoggerName(),
                SITE_FRIENDLY_NAME,
                stacktrace
        );

        eventBuffer.add(logEvent);

        if (eventBuffer.size() >= batchSize) {
            triggerSend();
        }
    }

    private void checkTimeoutAndSend() {
        long now = System.currentTimeMillis();
        if (now - lastSentTime.get() >= timeoutMillis) {
            triggerSend();
        }
    }

    private void triggerSend() {
        if (sendingInProgress.compareAndSet(false, true)) {
            scheduler.submit(this::sendLogs);
        }
    }

    private void sendLogs() {
        List<BackstageLogEntry> pendingLogs = new ArrayList<>(eventBuffer);
        boolean success = false;
        try {
            if (!pendingLogs.isEmpty()) {
                BackstageClient.sendLogs(pendingLogs);
            }
            success = true;
        } catch (Exception e) {
            retryCount.incrementAndGet();
            addError("Failed to send logs", e);
        } finally {
            if (success || retryCount.get() >= maxRetryCount) {
                if (!pendingLogs.isEmpty()) {
                    eventBuffer.removeAll(pendingLogs);
                }
                retryCount.set(0);
            }
            lastSentTime.set(System.currentTimeMillis());
            sendingInProgress.set(false);
        }
    }

    @Override
    public void stop() {
        super.stop();
        scheduler.shutdown();
    }

    // Config setters
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }
}
