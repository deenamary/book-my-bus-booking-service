package com.example.bookmybusbookingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${kafka.topic.name}")
    String topicname;

    @Value("${gateway.server.hostname}")
    String hostname;

    @Value("${gateway.server.portnumber}")
    String portnumber;

    @Value("${bmb.inventory.getbusinventory.path}")
    String busInventoryPath;

    @Bean("inventory_service_url")
    String getInventoryServiceUrl() {
        return "http://" + hostname + ":" + portnumber+busInventoryPath;
    }

    @Bean
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean("kafka_topic")
    String getBookingKafkaTopic() {
        return topicname;
    }
}
