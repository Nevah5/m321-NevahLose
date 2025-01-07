package dev.geeler.apiaces.gameservice.model.game;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
public class ChatMessage {
    @Getter
    @Setter
    private UUID gameId;

    @Getter
    @Setter
    private UUID playerId;

    @Setter
    @Getter
    private String message;
}
