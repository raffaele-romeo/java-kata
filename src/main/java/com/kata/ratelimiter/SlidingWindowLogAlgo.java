package com.kata.ratelimiter;

import java.time.Instant;
import java.util.LinkedList;

public class SlidingWindowLogAlgo {
    private final long maxRequestsPerWindow;
    private final long windowSizeInSeconds;

    private final LinkedList<Instant> requestLog;

    public SlidingWindowLogAlgo(long maxRequestsPerWindow, long windowSizeInSeconds) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.windowSizeInSeconds = windowSizeInSeconds;
        this.requestLog = new LinkedList<>();
    }

    public synchronized boolean allowRequest() {
        dropOldRequests();

        if (requestLog.size() < maxRequestsPerWindow) {
            requestLog.add(Instant.now());
            return true;
        }

        return false;
    }

    private void dropOldRequests() {
        var windowStart = Instant.now().getEpochSecond() - windowSizeInSeconds;

        while (!requestLog.isEmpty() && requestLog.peek().getEpochSecond() <= windowStart) {
            requestLog.poll();
        }
    }
}
