package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.Game;

import java.util.UUID;

public interface GameService {
    Game createGame(final UUID playerId);
    Game getGame(final UUID gameId);
    Game joinGame(final String roomId, final UUID playerId);
}
