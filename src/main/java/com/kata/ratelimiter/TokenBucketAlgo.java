package com.kata.ratelimiter;

import java.time.Instant;

public class TokenBucketAlgo {
    private final long bucketSize;
    private final double refillRateInSeconds;
    private double tokens;

    private Instant lastRefillTimestamp;

    public TokenBucketAlgo(long bucketSize, double refillRateInSeconds) {
        this.bucketSize = bucketSize;
        this.refillRateInSeconds = refillRateInSeconds;
        this.lastRefillTimestamp = Instant.now();
        this.tokens = bucketSize;
    }

    public synchronized boolean allowRequest() {
        refill();

        if (tokens > 0) {
            tokens--;
            return true;
        }

        return false;
    }

    private void refill() {
        var tokensToAdd = (Instant.now().toEpochMilli() - lastRefillTimestamp.toEpochMilli()) * refillRateInSeconds / 1000.0;

        if(tokensToAdd > 0) {
            tokens = Math.min(bucketSize, tokens + tokensToAdd);
            lastRefillTimestamp = Instant.now();
        }

    }
}
