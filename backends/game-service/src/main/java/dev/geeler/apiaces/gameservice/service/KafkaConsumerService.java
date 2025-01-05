package dev.geeler.apiaces.gameservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topicPattern = "games\\..*", groupId = "game-service")
    public void listen(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        String message = record.value();
        String gameId = topic.substring(topic.indexOf('.') + 1);
        messagingTemplate.convertAndSend("/topic/games/" + gameId, message);
    }
}
