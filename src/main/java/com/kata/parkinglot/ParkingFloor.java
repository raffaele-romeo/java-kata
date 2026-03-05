package com.kata.parkinglot;

import java.util.*;

public class ParkingFloor {
    private final List<ParkingSpot> spots;

    public ParkingFloor(ParkingFloorCapacity parkingFloorCapacity) {
        this.spots = makeParkingFloor(parkingFloorCapacity);
    }

    public synchronized Optional<UUID> park(Vehicle vehicle) {
        var spotSize = vehicle.requiredSpotSize();
        var parkingSpot = spots.stream().filter(spot -> !spot.isOccupied() && spot.getSpotSize() == spotSize).findFirst();

        return parkingSpot.map(spot -> {
            spot.setVehicle(vehicle);

            return spot.getSpotId();
        });
    }

    public synchronized boolean unPark(UUID spotId) {
        var maybeParkingSpot = spots.stream().filter(spot -> spot.getSpotId().equals(spotId)).findFirst();

        if (maybeParkingSpot.isPresent()) {
            var parkingSpot = maybeParkingSpot.get();
            parkingSpot.removeVehicle();
            return true;
        }

        return false;
    }

    public long size(SpotSize spotSize) {
        return spots.stream().filter(spot -> !spot.isOccupied() && spot.getSpotSize() == spotSize).count();
    }

    private List<ParkingSpot> makeParkingFloor(ParkingFloorCapacity parkingFloorCapacity) {
        var spots = new ArrayList<ParkingSpot>();

        for(SpotSize spotSize: parkingFloorCapacity.capacity().keySet()) {
            for (int i = 0; i < parkingFloorCapacity.capacity().get(spotSize); i++) {
                spots.add(new ParkingSpot(UUID.randomUUID(), spotSize));
            }
        }

        return spots;
    }
}
