package com.kata.parkinglot;

public class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate) {
        super(licensePlate);
    }

    @Override
    public SpotSize requiredSpotSize() {
        return SpotSize.SMALL;
    }
}
