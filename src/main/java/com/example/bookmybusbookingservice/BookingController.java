package com.example.bookmybusbookingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private KafkaProducer kafkaProducer;

    BookingController(BookingRepository bookingRepository,
                      KafkaProducer kafkaProducer) {
        this.bookingRepository = bookingRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("add/booking")
    public ResponseEntity<Booking> addBusInventory(@RequestBody Booking booking, BookingMessage bookingMessage)
    {
        String bookingId = UUID.randomUUID().toString();
        booking.setBookingId(bookingId);
        booking.setBookingDate(new Date());
        booking.setStatus("PENDING");
        bookingRepository.save(booking);

        //Send message to Kafka
        bookingMessage.setBookingId(bookingId);
        bookingMessage.setNoOfSeats(booking.getNoOfSeats());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String message = mapper.writeValueAsString(bookingMessage);
            kafkaProducer.sendMessage(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(booking);
    }
}
