package com.kata.ratelimiter;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

public class LeakyBucket {
    private final long bucketSize;
    private final long outflowRateInSeconds;
    private Instant lastRefillTimestamp;

    private final Queue<Instant> requestLog;

    public LeakyBucket(long bucketSize, long outflowRateInSeconds) {
        this.bucketSize = bucketSize;
        this.outflowRateInSeconds = outflowRateInSeconds;
        this.requestLog = new LinkedList<>();
        this.lastRefillTimestamp = Instant.now();
    }

    public synchronized boolean allowRequest() {
        processElems();

        if (requestLog.size() < bucketSize) {
            requestLog.add(Instant.now());
            return true;
        }

        return false;
    }

    private void processElems() {
        var elemsToProcess = (Instant.now().toEpochMilli() - lastRefillTimestamp.toEpochMilli()) * outflowRateInSeconds / 1000;

        if (elemsToProcess > 0) {
            for (long i = 0; i < elemsToProcess; i++) {
                requestLog.poll();
            }

            lastRefillTimestamp = Instant.now();
        }
    }
}
