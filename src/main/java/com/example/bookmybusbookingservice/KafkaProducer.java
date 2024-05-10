package com.example.bookmybusbookingservice;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC_NAME; // Replace with your desired topic name

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
                         String kafka_topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.TOPIC_NAME=kafka_topic;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC_NAME, message);
        System.out.println("Message " + message +
                " has been sucessfully sent to the topic: " + TOPIC_NAME);
    }
}
