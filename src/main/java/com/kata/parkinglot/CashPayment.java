package com.kata.parkinglot;

import java.math.BigDecimal;

public class CashPayment implements PaymentService {
    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " in cash");
    }
}
