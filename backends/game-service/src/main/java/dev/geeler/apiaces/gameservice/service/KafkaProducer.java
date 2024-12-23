package dev.geeler.apiaces.gameservice.service;

public interface KafkaProducer {
    void sendMessage(String topic, String message);

    void sendMessageDefault(String message);
}
