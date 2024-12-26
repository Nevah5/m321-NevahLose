package dev.geeler.apiaces.gameservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerImpl implements KafkaConsumer {
    @KafkaListener(topics = "default", groupId = "game-svc")
    @Override
    public void consumeDefault(String message) {
        System.out.println("Message (default): " + message);
    }
}
