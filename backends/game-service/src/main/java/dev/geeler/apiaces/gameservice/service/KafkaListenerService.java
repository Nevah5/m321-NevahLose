package dev.geeler.apiaces.gameservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import dev.geeler.apiaces.gameservice.model.game.GameActivity;
import dev.geeler.apiaces.gameservice.model.game.GameActivityType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaListenerService {
    private final ObjectMapper objectMapper;
    private final WebsocketService websocketService;
    private final GameService gameService;

    @KafkaListener(topicPattern = "games.activity")
    public void listenToGameTerminateTopics(String msg) {
        try {
            GameActivity activity = objectMapper.readValue(msg, GameActivity.class);
            if (activity.getType() == GameActivityType.CURRENT_CARDS) {
                gameService.getConnectedPlayers(activity.getGameId())
                        .stream()
                        .filter(player -> player.getPlayerId().equals(activity.getPlayerId()))
                        .findFirst()
                        .ifPresent(player -> websocketService.sendToUserIfConnected(
                                player.getPlayerId(),
                                "/queue/game",
                                activity
                        ));
                return;
            }
            gameService.getConnectedPlayers(activity.getGameId()).forEach(player -> websocketService.sendToUserIfConnected(
                    player.getPlayerId(),
                    "/queue/game",
                    activity
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
