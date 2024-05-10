package com.example.bookmybusbookingservice;

public class BusInventory {

    private String busId;

    private Integer availableSeats;

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }


    @Override
    public String toString() {
        return "BusInventory{" +
                "busId='" + busId + '\'' +
                ", availableSeats=" + availableSeats +
                '}';
    }
}

