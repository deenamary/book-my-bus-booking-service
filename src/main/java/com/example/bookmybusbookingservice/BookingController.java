package com.example.bookmybusbookingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
;

@RestController
@RequestMapping("api/v1")
public class BookingController {

    private  BookingRepository bookingRepository;
    private KafkaProducer kafkaProducer;
    private String bookingTopicName;
    private BusInventoryService busInventoryService;

    Logger logger = LoggerFactory.getLogger(BookingController.class);

    BookingController(BookingRepository bookingRepository,
                      KafkaProducer kafkaProducer,
                      BusInventoryService busInventoryService,
                      String kafka_topic) {
        this.bookingRepository = bookingRepository;
        this.kafkaProducer = kafkaProducer;
        this.busInventoryService = busInventoryService;
        this.bookingTopicName = kafka_topic;
    }

    @Transactional
    @PostMapping("add/booking")
    public ResponseEntity<String> addBusInventory(@RequestBody Booking booking, BookingMessage bookingMessage)
    {
        logger.info("Booking request received: " + booking);

        //Fetch available seats for the bus
        BusInventory busInventory;
        int availableSeats;
       try {
            availableSeats = busInventoryService.getAvailableSeats(booking.getBusId());
        }catch(Exception ex){
           logger.warn("Exception occurred while fetching inventory details from inventory service {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Could not fetch inventory details");
        }
        if(availableSeats>=booking.getNoOfSeats()) {
            logger.info("Seats are available.Proceeding with booking");
            String bookingId = UUID.randomUUID().toString();
            booking.setBookingId(bookingId);
            booking.setBookingDate(new Date());
            booking.setStatus("PENDING");
            bookingRepository.save(booking);
            logger.info("Booking {} has been saved", booking);

            //Send message to Kafka booking topic
            bookingMessage.setBookingId(bookingId);
            bookingMessage.setNoOfSeats(booking.getNoOfSeats());
            bookingMessage.setBusId(booking.getBusId());
            ObjectMapper mapper = new ObjectMapper();
            try {
                String message = mapper.writeValueAsString(bookingMessage);
                kafkaProducer.sendMessage(bookingTopicName,message);
                logger.info("Successfully sent the booking message {}", message);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                return ResponseEntity.internalServerError().body("Could not send booking message to kafka topic");
            }

            return ResponseEntity.ok("Booking successful with booking id "+bookingId+" and status PENDING");
        }else{
            logger.warn("Could not process booking request as requested number of seats are not available");
            return ResponseEntity.ok("Requested number of seats are not available for bus "+booking.getBusId());
        }
    }

}
