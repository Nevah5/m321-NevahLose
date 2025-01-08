package dev.geeler.apiaces.gameservice.service;

import java.util.UUID;

public interface KafkaService {
    void sendMessage(String topic, Object payload);

    void sendMessage(UUID gameId, String message);

    void sendMessageDefault(String message);
}
