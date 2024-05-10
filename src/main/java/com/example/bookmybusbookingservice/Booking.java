package com.example.bookmybusbookingservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(name = "bookingid", length = 500)
    private String bookingId;

    @Column(name = "busid", length = 500)
    private String busId;

    @Column(name = "source", length = 40)
    private String source;

    @Column(name = "destination", length = 40)
    private String destination;

    @Column(name = "no_of_seats", length = 20)
    private Integer noOfSeats;

    @Column(name = "bookingdate", length = 20)
    private Date bookingDate;

    @Column(name = "status", length = 40)
    private String status;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(Integer noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", busId='" + busId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", noOfSeats=" + noOfSeats +
                ", bookingDate=" + bookingDate +
                ", status='" + status + '\'' +
                '}';
    }
}
