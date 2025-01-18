package dev.geeler.apiaces.gameservice.service.impl;

import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import dev.geeler.apiaces.gameservice.service.ChatService;
import dev.geeler.apiaces.gameservice.service.KafkaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final KafkaService kafkaService;

    @Override
    public void sendChatMessage(ChatMessage message) {
        kafkaService.sendMessage("games.chat", message);
    }

}
