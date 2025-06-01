package com.assesment.ticketserviceapp.controller;

import com.assesment.ticketserviceapp.model.SeatHold;
import com.assesment.ticketserviceapp.service.TicketService;
import com.assesment.ticketserviceapp.validation.HoldSeatRequest;
import com.assesment.ticketserviceapp.validation.ReserveSeatRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@Log4j2
public class TicketServiceController {

    @Autowired
    TicketService ticketService;

    @GetMapping("/numSeatsAvailable")
    public ResponseEntity<String> numSeatsAvailable(
            @RequestParam Optional<Integer> level) {
        if (level.isPresent()) {
            int value = level.get();
            if (value < 1 || value > 4) {
                return ResponseEntity.ok("Level must be between 1 and 4");
            }
            int available = ticketService.numSeatsAvailable(level);
            return ResponseEntity.ok(String.valueOf(available));
        }
        return ResponseEntity.ok("No level provided");
    }

    @PostMapping("/findAndHoldSeats")
    public ResponseEntity<SeatHold> findAndHoldSeats(@Valid @RequestBody HoldSeatRequest request) {
        SeatHold seatHold = ticketService.findAndHoldSeats(
                request.getNumSeats(),
                Optional.ofNullable(request.getMinLevel()),
                Optional.ofNullable(request.getMaxLevel()),
                request.getCustomerEmail());

        if(null== seatHold || null==seatHold.seats() || seatHold.seats().size()==0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to hold seats. No seats available in this Venue");

        return ResponseEntity.ok(seatHold);
    }


    @PostMapping("/reserveSeats")
    public ResponseEntity<String> reserveSeats(@Valid @RequestBody ReserveSeatRequest reserveSeatRequest) {
        String confirmation = ticketService.reserveSeats(reserveSeatRequest.getSeatHoldId(),
                reserveSeatRequest.getCustomerEmail());
        return ResponseEntity.ok("Reservation Number: "+confirmation);
    }
}
