package com.kata.parkinglot;

import java.math.BigDecimal;

public interface PaymentStrategy {
    BigDecimal calculateFees(Ticket ticket);
}
