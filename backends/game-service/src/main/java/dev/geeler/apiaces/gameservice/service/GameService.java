package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GamePlayer;

import java.util.List;
import java.util.UUID;

// TODO: fix inconsistencies with final
public interface GameService {
    UUID getCurrentGameIdFromPlayer(final UUID playerId);

    Game createGame(final UUID playerId);

    Game getGame(final Long roomId);

    Game getGame(final UUID gameId);

    void joinGame(final UUID gameId, final UUID playerId);

    void leaveGame(final UUID gameId, final UUID playerId, String username);

    void startGame(final UUID gameId, final UUID playerId);

    List<GamePlayer> getPlayers(final UUID gameId);

    void sendChatMessage(ChatMessage chatMessage);

    void connectUser(UUID playerId, String sessionId);

    void disconnectUser(UUID playerId, String username);
}
