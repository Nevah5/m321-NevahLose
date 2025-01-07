package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GamePlayer;

import java.util.List;
import java.util.UUID;

public interface GameService {
    Game createGame(final UUID playerId);

    Game getGame(final Long roomId);

    Game getGame(final UUID gameId);

    void joinGame(final UUID gameId, final UUID playerId);

    void leaveGame(final UUID gameId, final UUID playerId);

    void startGame(final UUID gameId, final UUID playerId);

    List<GamePlayer> getPlayers(final UUID gameId);
}
