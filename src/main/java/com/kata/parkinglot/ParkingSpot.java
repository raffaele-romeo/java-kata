package com.kata.parkinglot;

import java.util.Optional;
import java.util.UUID;

public class ParkingSpot {
    private final UUID spotId;
    private final SpotSize spotSize;
    private Vehicle vehicle;

    public ParkingSpot(UUID spotId, SpotSize spotSize) {
        this.spotId = spotId;
        this.spotSize = spotSize;
    }

    public UUID getSpotId() {
        return spotId;
    }

    public SpotSize getSpotSize() {
        return spotSize;
    }

    public boolean isOccupied() {
        return vehicle != null;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Optional<Vehicle> getVehicle() {
        return  Optional.ofNullable(vehicle);
    }

    public void removeVehicle() {
        this.vehicle = null;
    }
}
