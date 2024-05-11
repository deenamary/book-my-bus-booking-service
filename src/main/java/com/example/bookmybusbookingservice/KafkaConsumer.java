package com.example.bookmybusbookingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class KafkaConsumer {

    public static final String CONFIRMED = "CONFIRMED";
    public static final String FAILED = "FAILED";
    private final BookingRepository bookingRepository;
    PassengerRepository passengerRepository;
    String bookingConfirmationFailureTopicName;
    KafkaProducer kafkaProducer;

    KafkaConsumer(BookingRepository bookingRepository,
                  PassengerRepository passengerRepository,
                  String booking_confirmation_failure_topic,
                  KafkaProducer kafkaProducer) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.bookingConfirmationFailureTopicName = booking_confirmation_failure_topic;
        this.kafkaProducer = kafkaProducer;
    }

    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "book-my-bus-booking-confirmation-topic", groupId = "console-consumer-68654")
    public void consumeMessage(String message) throws JsonProcessingException {
        logger.info("Received message: {}", message);
        String bookingId = message;
        try {
            bookingRepository.updateStatusByBookingId(CONFIRMED, bookingId);
            logger.info("Updated booking status to {} for Booking ID: {}", CONFIRMED, bookingId);

            Passenger passenger = new Passenger();
            passenger.setPassengerId(UUID.randomUUID().toString());
            passenger.setBookingId(bookingId);
            passengerRepository.save(passenger);
            logger.info("Saved passenger details {}", passenger);
        } catch (Exception e) {
            logger.error("Exception occurred while updating booking status to CONFIRMED. Exception details : {}",e.getMessage());
            InventoryUpdateMessage inventoryUpdateMessage = new InventoryUpdateMessage();
            inventoryUpdateMessage.setBookingId(bookingId);
            Optional<Booking> booking = bookingRepository.findById(bookingId);
            if(booking.isPresent()){
                inventoryUpdateMessage.setBusId(booking.get().getBusId());
                inventoryUpdateMessage.setNoOfBookings(booking.get().getNoOfSeats());
            }
            ObjectMapper mapper = new ObjectMapper();
            String inventoryUpdateMessageString = mapper.writeValueAsString(inventoryUpdateMessage);
            kafkaProducer.sendMessage(bookingConfirmationFailureTopicName,inventoryUpdateMessageString);
        }

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
