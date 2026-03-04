package com.kata.ratelimiter;

import java.time.Instant;

public class FixedWindowCounter {
    private final long maxRequestsPerWindow;
    private final long windowSizeInSeconds;

    private Instant windowStart;
    private long numberOfRequests;

    public FixedWindowCounter(long maxRequestsPerWindow, long windowSizeInSeconds) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.windowStart = Instant.now();
        this.numberOfRequests = 0;
    }

    public synchronized boolean allowRequest() {
        var windowSize = Instant.now().getEpochSecond() - windowStart.getEpochSecond();

        if (windowSize >= windowSizeInSeconds) {
            windowStart = Instant.now();
            numberOfRequests = 0;
        }

        if (numberOfRequests < maxRequestsPerWindow) {
            numberOfRequests++;
            return true;
        }

        return false;
    }
}
