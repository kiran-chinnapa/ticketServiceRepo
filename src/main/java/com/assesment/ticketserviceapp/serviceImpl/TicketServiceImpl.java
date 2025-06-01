package com.assesment.ticketserviceapp.serviceImpl;

import com.assesment.ticketserviceapp.constants.SeatStatus;
import com.assesment.ticketserviceapp.model.Seat;
import com.assesment.ticketserviceapp.model.SeatHold;
import com.assesment.ticketserviceapp.model.SeatObj;
import com.assesment.ticketserviceapp.service.TicketService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class TicketServiceImpl implements TicketService {

    @Value("${app.seat.hold.seconds}")
    private int seatHoldSeconds;

    private Map<Integer, SeatObj> venue = new ConcurrentHashMap<>();

    final AsyncServiceImpl asyncService;

    public TicketServiceImpl(AsyncServiceImpl asyncService) throws Exception {
        this.asyncService = asyncService;
        venue.put(1, new SeatObj(1));
        venue.put(2, new SeatObj(2));
        venue.put(3, new SeatObj(3));
        venue.put(4, new SeatObj(4));
    }

    private Map<String, SeatHold> heldSeats = new ConcurrentHashMap<>();

    @Override
    public int numSeatsAvailable(Optional<Integer> venueLevel) {
        try{
            return venue.get(venueLevel.get()).getSeatsAvailable();
        }catch (Exception e){
            log.error("invalid venueLevel ",e);
        }
        return 0;
    }


    @Override
    public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel,
                                     Optional<Integer> maxLevel, String customerEmail) {
        int seatsHeld = 0;
        int seatsNeeded = numSeats;
        List<Seat> holdSeats = new ArrayList<>();
        for (int l = minLevel.get(); l <= maxLevel.get(); l++) {
            Seat seat = holdSeats(l, seatsNeeded);
            if(seat.noOfSeats()>0)
                holdSeats.add(seat);
            seatsHeld = seatsHeld +seat.noOfSeats();
            if (seat.noOfSeats() == numSeats)
                break;
            else
                seatsNeeded = numSeats - seatsHeld;
        }
        SeatHold seatHold = new SeatHold(holdSeats, customerEmail, seatHoldSeconds);
        this.heldSeats.put(seatHold.seatHoldId()+customerEmail,seatHold);
        if(numSeats - seatsHeld >0)
            log.info("sorry only "+seatsHeld+" seats are available, all other seats sold out");

        if(seatsHeld>0){
            // release seats if timer expries
            asyncService.checkTimerAndReleaseSeats(seatHold,venue,heldSeats,seatHoldSeconds);
        }

        return seatHold;
    }

    private Seat holdSeats(int level, int seatsNeeded) {
        SeatObj seatObj= venue.get(level);
        double totalSeatCost =0;
        int seatsOnhold=0;
        int availableSeats= seatObj.getSeatsAvailable();
        if(availableSeats>0){
            for(Map.Entry<Integer,String> seatNumStatus: seatObj.getSeatDetail().entrySet()){
                if(SeatStatus.AVAILABLE.equals(seatNumStatus.getValue())){
                    seatNumStatus.setValue(SeatStatus.ON_HOLD);
                    seatsOnhold++;
                    totalSeatCost = totalSeatCost+ seatObj.getPricePerSeat();
                }
                if(seatsOnhold ==seatsNeeded) break;
            }
        }
        return new Seat(level,seatObj.getLevelName(),totalSeatCost,seatsOnhold);
    }


    @Override
    public String reserveSeats(int seatHoldId, String customerEmail) {
        //get Seats based on seat hold Id
        SeatHold seatHold = this.heldSeats.get(seatHoldId+customerEmail);
        if(null== seatHold) return "Invalid seatHoldId";
        //update seats in venue as reserved and generate reservation Id
        for(Seat seat: seatHold.seats())
            updateVenueAsReserved(seat);
        log.info("seats reserved for seatHoldId "+seatHoldId);
        this.heldSeats.remove(seatHoldId+customerEmail);
        return UUID.randomUUID().toString();
    }

    private void updateVenueAsReserved(Seat seat) {
        SeatObj seatObj= venue.get(seat.levelId());
        int reserveSeatCnt = 0;
        for(Map.Entry<Integer,String> seatNumStatus: seatObj.getSeatDetail().entrySet()){
            if(SeatStatus.ON_HOLD.equals(seatNumStatus.getValue())){
                seatNumStatus.setValue(SeatStatus.RESERVED);
                reserveSeatCnt++;
            }
            if(reserveSeatCnt==seat.noOfSeats()) break;
        }
    }

    public Map<Integer, SeatObj> getVenue() {
        return venue;
    }

    public Map<String, SeatHold> getHeldSeats() {
        return heldSeats;
    }

    public void setSeatHoldSeconds(int seatHoldSeconds) {
        this.seatHoldSeconds = seatHoldSeconds;
    }
}
