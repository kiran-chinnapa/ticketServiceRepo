package com.assesment.ticketserviceapp.model;

import com.assesment.ticketserviceapp.constants.SeatStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SeatObj {
        private int levelId;
        private String levelName;
        private double pricePerSeat;
        private Map<Integer,String> seatDetail= new ConcurrentHashMap<>();

        public SeatObj(int levelId) throws Exception {
            this.levelId = levelId;
            if(1==levelId){
                this.levelName="Orchestra";
                this.pricePerSeat=100.00;
                for (int i = 1; i <= (25*50); i++) {
                    this.seatDetail.put(i, SeatStatus.AVAILABLE);
                }
            }else if(2==levelId) {
                this.levelName = "Main";
                this.pricePerSeat = 75.00;
                for (int i = 1; i <= (20*100); i++) {
                    this.seatDetail.put(i,SeatStatus.AVAILABLE);
                }
            }else if(3==levelId) {
                this.levelName = "Balcony 1";
                this.pricePerSeat = 50.00;
                for (int i = 1; i <= (15*100); i++) {
                    this.seatDetail.put(i,SeatStatus.AVAILABLE);
                }
            }else if(4==levelId) {
                this.levelName = "Balcony 2";
                this.pricePerSeat = 40.00;
                for (int i = 1; i <= (15*100); i++) {
                    this.seatDetail.put(i,SeatStatus.AVAILABLE);
                }
            }else
                throw new Exception("Unknown level");
        }

    public Map<Integer, String> getSeatDetail() {
        return seatDetail;
    }

    public int getLevelId() {
        return levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public double getPricePerSeat() {
        return pricePerSeat;
    }

    public int getSeatsAvailable(){
            return (int)this.seatDetail.values().stream()
                    .filter(seatStatus-> SeatStatus.AVAILABLE.equals(seatStatus))
                    .count();
    }

    public int getSeatsOnHold(){
            return (int)this.seatDetail.values().stream()
                    .filter((seatStatus-> SeatStatus.ON_HOLD.equals(seatStatus)))
                    .count();

    }

    public int getSeatsReserved(){
        return (int)this.seatDetail.values().stream()
                .filter((seatStatus-> SeatStatus.RESERVED.equals(seatStatus)))
                .count();
    }
}

