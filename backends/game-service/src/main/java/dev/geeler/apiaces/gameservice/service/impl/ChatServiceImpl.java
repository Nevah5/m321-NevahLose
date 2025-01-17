package dev.geeler.apiaces.gameservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import dev.geeler.apiaces.gameservice.service.ChatService;
import dev.geeler.apiaces.gameservice.service.GameService;
import dev.geeler.apiaces.gameservice.service.KafkaService;
import dev.geeler.apiaces.gameservice.service.WebsocketService;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {
    private final WebsocketService websocketService;
    private final GameService gameService;
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;


    public ChatServiceImpl(@Lazy GameService gameService, @Lazy WebsocketService websocketService, KafkaService kafkaService, ObjectMapper objectMapper) {
        this.gameService = gameService;
        this.websocketService = websocketService;
        this.kafkaService = kafkaService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendChatMessage(ChatMessage message) {
        kafkaService.sendMessage("games.chat", message);
    }

    @KafkaListener(topicPattern = "games.chat", groupId = "game-service")
    public void listen(String msg) throws JsonProcessingException {
        ChatMessage message = objectMapper.readValue(msg, ChatMessage.class);
        gameService.getConnectedPlayers(message.getGameId()).forEach(player -> {
            websocketService.sendToUserIfConnected(player.getPlayerId(), "/queue/chat", message);
        });
    }
}
