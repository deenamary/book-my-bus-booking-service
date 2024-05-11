package com.example.bookmybusbookingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaConsumer {

    public static final String CONFIRMED = "CONFIRMED";
    public static final String FAILED = "FAILED";
    private final BookingRepository bookingRepository;
    PassengerRepository passengerRepository;

    KafkaConsumer(BookingRepository bookingRepository,
                  PassengerRepository passengerRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
    }

    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "book-my-bus-booking-confirmation-topic", groupId = "console-consumer-68654")
    public void consumeMessage(String message) throws JsonProcessingException {
        logger.info("Received message: {}", message);
        String bookingId = message;
        bookingRepository.updateStatusByBookingId(CONFIRMED,bookingId);
        logger.info("Updated booking status to {} for Booking ID: {}" ,CONFIRMED, bookingId);

        Passenger passenger = new Passenger();
        passenger.setPassengerId(UUID.randomUUID().toString());
        passenger.setBookingId(bookingId);
        passengerRepository.save(passenger);
        logger.info("Saved passenger details {}",passenger);

    }

    @KafkaListener(topics = "book-my-bus-payment-failure-topic", groupId = "console-consumer-68654")
    public void consumePaymentFailureMessage(String message) throws JsonProcessingException {
        logger.info("Received payment failure message: {}", message);
        logger.info("Proceeding with marking booking {} as FAILED",message);
        String bookingId = message;
        bookingRepository.updateStatusByBookingId(FAILED,bookingId);
        logger.info("Updated booking status to {} for Booking ID: {}" ,FAILED, bookingId);

    }


}
