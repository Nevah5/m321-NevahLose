package dev.geeler.apiaces.gameservice.model.game;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Builder
@ToString
public class ChatMessage {
    @Getter
    private final UUID id = UUID.randomUUID();

    @Getter
    @Setter
    private UUID gameId;

    @Getter
    @Setter
    private UUID senderId;

    @Getter
    @Setter
    private String senderUsername;

    @Setter
    @Getter
    private ChatType type;

    @Getter
    @Setter
    private boolean isJoined;

    @Setter
    @Getter
    private String message;

    @Getter
    @Setter
    private boolean isHost;
}
