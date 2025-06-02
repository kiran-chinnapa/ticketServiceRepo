package com.assesment.ticketserviceapp.model;

import java.util.List;

public record Seat(int levelId, String levelName, double price, int noOfSeats, List<Integer> seatNumbers) {
}
