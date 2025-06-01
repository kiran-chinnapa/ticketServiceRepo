package com.assesment.ticketserviceapp;

import com.assesment.ticketserviceapp.model.SeatHold;
import com.assesment.ticketserviceapp.serviceImpl.AsyncServiceImpl;
import com.assesment.ticketserviceapp.serviceImpl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.Optional;

public class AsyncServiceImplTest {

    private AsyncServiceImpl asyncService;
    private TicketServiceImpl ticketService;

    public AsyncServiceImplTest() throws Exception {
        asyncService = new AsyncServiceImpl();
        ticketService = new TicketServiceImpl(asyncService);
    }

    @Test
    void testCheckTimerAndReleaseSeats() throws Exception {

        SeatHold seatHold = ticketService.findAndHoldSeats(4000,Optional.of(1),Optional.of(4),"test@email");
        System.out.println(seatHold);
        asyncService.checkTimerAndReleaseSeats(seatHold,ticketService.getVenue(),60);
        Assert.isTrue(ticketService.numSeatsAvailable(Optional.of(1))==1250,"testCheckTimerAndReleaseSeats test failed");
        Assert.isTrue(ticketService.numSeatsAvailable(Optional.of(2))==2000,"testCheckTimerAndReleaseSeats test failed");
        Assert.isTrue(ticketService.numSeatsAvailable(Optional.of(3))==1500,"testCheckTimerAndReleaseSeats test failed");
        Assert.isTrue(ticketService.numSeatsAvailable(Optional.of(4))==1500,"testCheckTimerAndReleaseSeats test failed");
    }
}
