package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.game.ChatMessage;

public interface ChatService {
    void sendChatMessage(ChatMessage message);
}
