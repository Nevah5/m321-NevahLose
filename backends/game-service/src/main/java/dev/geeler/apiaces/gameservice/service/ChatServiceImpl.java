package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private final WebsocketService websocketService;
    private final GameService gameService;
    private final KafkaService kafkaService;


    public ChatServiceImpl(@Lazy GameService gameService,  @Lazy WebsocketService websocketService, KafkaService kafkaService) {
        this.gameService = gameService;
        this.websocketService = websocketService;
        this.kafkaService = kafkaService;
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
//        kafkaService.sendMessage("/games/chat/", message); // TODO: use kafka
        gameService.getConnectedPlayers(message.getGameId()).forEach(player -> {
            websocketService.sendToUserIfConnected(player.getPlayerId(), "/queue/chat", message);
        });
    }

    @KafkaListener(topicPattern = "/games/chat", groupId = "game-service")
    public void listen(ConsumerRecord<String, ChatMessage> record) {
        ChatMessage message = record.value();
        gameService.getConnectedPlayers(message.getGameId()).forEach(player -> {
            websocketService.sendToUserIfConnected(player.getPlayerId(), "/queue/chat", message);
        });
    }
}
