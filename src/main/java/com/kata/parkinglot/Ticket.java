package com.kata.parkinglot;

import java.time.Instant;
import java.util.UUID;

public class Ticket {
    private final UUID spotId;
    private final String licensePlate;
    private final Instant entryTime;
    private boolean paid;

    public Ticket(UUID spotId, String licensePlate, Instant entryTime) {
        this.spotId = spotId;
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
        this.paid = false;
    }

    public UUID spotId() { return spotId; }
    public String licensePlate() { return licensePlate; }
    public Instant entryTime() { return entryTime; }
    public boolean isPaid() { return paid; }
    public void markPaid() { this.paid = true; }
}

