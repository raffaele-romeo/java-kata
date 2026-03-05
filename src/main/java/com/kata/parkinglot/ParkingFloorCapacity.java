package com.kata.parkinglot;

import java.util.Map;

public record ParkingFloorCapacity(Map<SpotSize, Integer> capacity) {
}
