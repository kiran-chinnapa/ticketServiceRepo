package com.assesment.ticketserviceapp;

import com.assesment.ticketserviceapp.model.SeatHold;
import com.assesment.ticketserviceapp.serviceImpl.AsyncServiceImpl;
import com.assesment.ticketserviceapp.serviceImpl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Optional;

public class TicketServiceImplTest {

    private TicketServiceImpl ticketService;
    private AsyncServiceImpl asyncService;

    TicketServiceImplTest() throws Exception {
        asyncService = new AsyncServiceImpl();
        ticketService = new TicketServiceImpl(asyncService);
    }

    @Test
    public void numSeatsAvailable() {
        int num = ticketService.numSeatsAvailable(Optional.of(2));
        Assert.isTrue(num==2000,"numSeatsAvailable test failed");
    }

    @Test
    void findAndHoldSeats() {
        SeatHold seatHold = ticketService.findAndHoldSeats(4000,Optional.of(2),Optional.of(4),"test@email");
        System.out.println(seatHold);
        Assert.isTrue(null!= seatHold && seatHold.totalSeatsPrice()==245000.0,"findAndHoldSeats test failed");
    }

    @Test
    void findAndHoldSeats_Negative() {
        SeatHold seatHold = ticketService.findAndHoldSeats(4000,Optional.of(2),Optional.of(2),"test@email");
        System.out.println(seatHold);
        Assert.isTrue(null!= seatHold && seatHold.totalSeatsPrice()!=245000.0,"findAndHoldSeats test failed");
    }

    @Test
    void reserveSeats() {
        ticketService.setSeatHoldSeconds(1);
        SeatHold seatHold = ticketService.findAndHoldSeats(4000,Optional.of(2),Optional.of(4),"test@email");
        String reservationId = ticketService.reserveSeats(seatHold.seatHoldId(),"test@email");
        Assert.isTrue(null!=reservationId , "reserveSeats test failed");
    }
}