package com.kata.parkinglot;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;

public class HourlyPaymentStrategy implements PaymentStrategy {
    private final BigDecimal hourlyRate;
    private final Clock clock;

    public HourlyPaymentStrategy(BigDecimal hourlyRate) {
        this(hourlyRate, Clock.systemUTC());
    }

    public HourlyPaymentStrategy(BigDecimal hourlyRate, Clock clock) {
        this.hourlyRate = hourlyRate;
        this.clock = clock;
    }

    @Override
    public BigDecimal calculateFees(Ticket ticket) {
        var parked = Duration.between(ticket.entryTime(), clock.instant());
        long hours = Math.max(1, (long) Math.ceil(parked.toMinutes() / 60.0));

        return hourlyRate.multiply(BigDecimal.valueOf(hours));
    }
}
