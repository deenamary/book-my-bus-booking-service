package com.example.bookmybusbookingservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class BookingController {

    private  BookingRepository bookingRepository;

    BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("add/booking")
    public ResponseEntity<Booking> addBusInventory(@RequestBody Booking booking)
    {
        booking.setBookingId(String.valueOf(UUID.randomUUID()));
        booking.setBookingDate(new Date());
        booking.setStatus("PENDING");
        bookingRepository.save(booking);
        return ResponseEntity.ok(booking);
    }
}
