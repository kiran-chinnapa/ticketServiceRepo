package com.assesment.ticketserviceapp.serviceImpl;

import com.assesment.ticketserviceapp.constants.SeatStatus;
import com.assesment.ticketserviceapp.model.Seat;
import com.assesment.ticketserviceapp.model.SeatHold;
import com.assesment.ticketserviceapp.model.SeatObj;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
public class AsyncServiceImpl {

    @Async
    public void checkTimerAndReleaseSeats(SeatHold seatHold, Map<Integer, SeatObj> venue, int seatHoldSeconds){
        try {
            log.info("checkTimerAndReleaseSeats thread sleeping for "+seatHoldSeconds+" seconds");
            Thread.sleep(seatHoldSeconds);
            markHeldSeatsAsAvailable(seatHold,venue);
        } catch (InterruptedException e) {
            log.error("Exception in checkTimerAndReleaseSeats",e);
        }
    }


    private void markHeldSeatsAsAvailable(SeatHold seatHold, Map<Integer, SeatObj> venue){
        for(Seat seat: seatHold.seats()){
            SeatObj seatObj= venue.get(seat.levelId());
            int availableSeatCnt = 0;
            for(Map.Entry<Integer,String> seatNumStatus: seatObj.getSeatDetail().entrySet()){
                if(SeatStatus.ON_HOLD.equals(seatNumStatus.getValue())){
                    seatNumStatus.setValue(SeatStatus.AVAILABLE);
                    availableSeatCnt++;
                }
                if(availableSeatCnt==seat.noOfSeats()) break;
            }
        }
    }
}
