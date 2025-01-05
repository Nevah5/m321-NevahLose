package dev.geeler.apiaces.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(UUID gameId, String message) {
        kafkaTemplate.send("games." + gameId.toString(), message);
    }

    @Override
    public void sendMessageDefault(String message) {
        kafkaTemplate.send("default", message);
    }
}
