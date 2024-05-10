package com.example.bookmybusbookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BookMyBusBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookMyBusBookingServiceApplication.class, args);
    }

}
