package com.kata.parkinglot;

public class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate);
    }

    @Override
    public SpotSize requiredSpotSize() {
        return SpotSize.MEDIUM;
    }
}
