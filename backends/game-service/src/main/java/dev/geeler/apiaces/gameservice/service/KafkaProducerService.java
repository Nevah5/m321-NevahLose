package dev.geeler.apiaces.gameservice.service;

import java.util.UUID;

public interface KafkaProducerService {
    void sendMessage(UUID gameId, String message);

    void sendMessageDefault(String message);
}
