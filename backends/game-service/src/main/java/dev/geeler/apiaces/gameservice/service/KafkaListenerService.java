package dev.geeler.apiaces.gameservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaListenerService {
    private final ObjectMapper objectMapper;
    private final WebsocketService websocketService;
    private final GameService gameService;

    @KafkaListener(topicPattern = "games.terminate")
    public void listenToGameTerminateTopics(String msg) {
        try {
            UUID gameId = objectMapper.readValue(msg, UUID.class);
            gameService.getConnectedPlayers(gameId).forEach(player -> websocketService.sendToUserIfConnected(
                    player.getPlayerId(),
                    "/queue/game/terminate",
                    "The owner of this game has left the game."
            ));
        } catch (Exception e) {
            log.error("Error processing game termination for gameId: " + msg, e);
        }
    }

    @KafkaListener(topicPattern = "games.chat")
    public void listenToGameChat(String msg) throws JsonProcessingException {
        ChatMessage message = objectMapper.readValue(msg, ChatMessage.class);
        gameService.getConnectedPlayers(message.getGameId()).forEach(player -> {
            websocketService.sendToUserIfConnected(player.getPlayerId(), "/queue/chat", message);
        });
    }
}
