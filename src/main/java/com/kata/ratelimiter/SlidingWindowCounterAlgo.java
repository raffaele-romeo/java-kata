package com.kata.ratelimiter;

import java.time.Instant;

public class SlidingWindowCounterAlgo {
    private final long windowSizeInSeconds;
    private final long maxRequestsPerWindow;
    private long currentWindowStart;
    private long previousWindowCount;
    private long currentWindowCount;

    public SlidingWindowCounterAlgo(long windowSizeInSeconds, long maxRequestsPerWindow) {
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.currentWindowStart = Instant.now().getEpochSecond();
        this.previousWindowCount = 0;
        this.currentWindowCount = 0;
    }

    public synchronized boolean allowRequest() {
        long now = Instant.now().getEpochSecond();
        long timePassedInWindow = now - currentWindowStart;

        if (timePassedInWindow >= windowSizeInSeconds) {
            previousWindowCount = currentWindowCount;
            currentWindowCount = 0;
            currentWindowStart = now;
            timePassedInWindow = 0;
        }

        double weightedCount = previousWindowCount * ((windowSizeInSeconds - timePassedInWindow) / (double) windowSizeInSeconds)
                + currentWindowCount;

        if (weightedCount < maxRequestsPerWindow) {
            currentWindowCount++;
            return true;
        }
        return false;
    }
}
