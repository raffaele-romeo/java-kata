package com.kata.parkinglot;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParkingLot {
    private final List<ParkingFloor> parkingFloors;
    private final PaymentStrategy paymentStrategy;
    private final Clock clock;

    public ParkingLot(List<ParkingFloorCapacity> floorCapacities, PaymentStrategy paymentStrategy) {
        this(floorCapacities, paymentStrategy, Clock.systemUTC());
    }

    public ParkingLot(List<ParkingFloorCapacity> floorCapacities, PaymentStrategy paymentStrategy, Clock clock) {
        var parkingFloors = new ArrayList<ParkingFloor>();

        for (ParkingFloorCapacity capacity : floorCapacities) {
            parkingFloors.add(new ParkingFloor(capacity));
        }

        this.parkingFloors = parkingFloors;
        this.paymentStrategy = paymentStrategy;
        this.clock = clock;
    }

    public Optional<Ticket> park(Vehicle vehicle) {
        for (ParkingFloor floor : parkingFloors) {
            var spotId = floor.park(vehicle);

            if (spotId.isPresent()) {
                var ticket = new Ticket(
                        spotId.get(),
                        vehicle.getLicensePlate(),
                        clock.instant()
                );
                return Optional.of(ticket);
            }
        }

        return Optional.empty();
    }

    public BigDecimal calculateFees(Ticket ticket) {
        return paymentStrategy.calculateFees(ticket);
    }

    public void pay(Ticket ticket, PaymentService paymentService) {
        if (ticket.isPaid()) {
            throw new IllegalStateException("Ticket already paid");
        }

        BigDecimal fee = calculateFees(ticket);
        paymentService.pay(fee);
        ticket.markPaid();

        for (ParkingFloor floor : parkingFloors) {
            if (floor.unPark(ticket.spotId()))
                break;
        }
    }
}
