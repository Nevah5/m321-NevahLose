package dev.geeler.apiaces.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaServiceImpl implements KafkaService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, Object payload) {
        kafkaTemplate.send(topic, payload.toString());
    }

    @Override
    public void sendMessage(UUID gameId, String message) {
        kafkaTemplate.send("games." + gameId.toString(), message);
    }

    @Override
    public void sendMessageDefault(String message) {
        kafkaTemplate.send("default", message);
    }
}
