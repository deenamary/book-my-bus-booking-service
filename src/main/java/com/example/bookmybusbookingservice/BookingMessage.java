package com.example.bookmybusbookingservice;

public class BookingMessage {


    private String bookingId;
    private String busId;
    private int noOfSeats;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    @Override
    public String toString() {
        return "BookingMessage{" +
                "bookingId='" + bookingId + '\'' +
                ", busId='" + busId + '\'' +
                ", noOfSeats=" + noOfSeats +
                '}';
    }
}
