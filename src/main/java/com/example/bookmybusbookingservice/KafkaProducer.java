package com.example.bookmybusbookingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC_NAME;
    Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
                         String kafka_topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.TOPIC_NAME=kafka_topic;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC_NAME, message);
        logger.info("Message {} has been successfully sent to the topic: {}" ,message, TOPIC_NAME);
    }
}
