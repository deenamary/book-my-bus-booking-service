package com.example.bookmybusbookingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${kafka.topic.name}")
    String topicname;

    @Value("${kafka.booking.confirmation.failure.topic.name}")
    String bookingConfirmationFailureTopicName;

    @Value("${gateway.server.hostname}")
    String hostname;

    @Value("${gateway.server.portnumber}")
    String portnumber;

    @Value("${bmb.inventory.getbusinventory.path}")
    String busInventoryPath;

    @Value("${bmb.busroute.getbusroute.path}")
    String busRoutePath;

    @Bean("inventory_service_url")
    String getInventoryServiceUrl() {
        return "http://" + hostname + ":" + portnumber+busInventoryPath;
    }

    @Bean("admin_service_url")
    String getAdminServiceUrl() {
        return "http://" + hostname + ":" + portnumber+busRoutePath;
    }

    @Bean
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("kafka_topic")
    String getBookingKafkaTopic() {
        return topicname;
    }

    @Bean("booking_confirmation_failure_topic")
    String getBookingConfirmationFailureTopicName() {
        return bookingConfirmationFailureTopicName;
    }
}
