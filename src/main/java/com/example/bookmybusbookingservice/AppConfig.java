package com.example.bookmybusbookingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${kafka.topic.name}")
    String topicname;

    @Bean("kafka_topic")
    String getBookingKafkaTopic() {
        return topicname;
    }
}
