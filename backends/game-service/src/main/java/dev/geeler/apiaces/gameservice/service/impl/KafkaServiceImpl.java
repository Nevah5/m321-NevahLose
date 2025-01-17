package dev.geeler.apiaces.gameservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.geeler.apiaces.gameservice.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaServiceImpl implements KafkaService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void sendMessage(String topic, Object payload) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, jsonPayload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payload to JSON", e);
        }
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
