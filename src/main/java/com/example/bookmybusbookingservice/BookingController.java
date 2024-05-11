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
    private  String inventory_service_url;
    private WebClient.Builder webClientBuilder;

    Logger logger = LoggerFactory.getLogger(BookingController.class);

    BookingController(BookingRepository bookingRepository,
                      KafkaProducer kafkaProducer,
                      String inventory_service_url,
                      WebClient.Builder webClientBuilder) {
        this.bookingRepository = bookingRepository;
        this.kafkaProducer = kafkaProducer;
        this.inventory_service_url = inventory_service_url;
        this.webClientBuilder = webClientBuilder;
    }

    @Transactional
    @PostMapping("add/booking")
    public ResponseEntity<String> addBusInventory(@RequestBody Booking booking, BookingMessage bookingMessage)
    {
        logger.info("Booking request received: " + booking);

        //Fetch available seats for the bus
        BusInventory busInventory;
       try {
            busInventory = webClientBuilder.build().get().uri(inventory_service_url + booking.getBusId())
                    .retrieve()
                    .bodyToMono(BusInventory.class)
                    .block();
            logger.info("Fetched bus inventory details {}", busInventory);
        }catch(Exception ex){
           logger.warn("Exception occured while fetching inventory details from inventory service "+ex.getMessage());
            return ResponseEntity.badRequest().body("Could not fetch inventory details");
        }
        if(Objects.requireNonNull(busInventory).getAvailableSeats()>=booking.getNoOfSeats()) {
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
                kafkaProducer.sendMessage(message);
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
