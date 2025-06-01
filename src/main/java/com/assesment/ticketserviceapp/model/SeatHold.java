package com.assesment.ticketserviceapp.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record SeatHold(List<Seat> seats, String customerEmail, int seatHoldId, double totalSeatsPrice, int seatHoldSeconds)
{
    private static final AtomicInteger counter = new AtomicInteger(1);
    public SeatHold(List<Seat> seats, String customerEmail, int seatHoldSeconds){
        this(seats,customerEmail, counter.getAndIncrement(),seats.stream().mapToDouble(Seat::price).sum(),  seatHoldSeconds);
    }
}
