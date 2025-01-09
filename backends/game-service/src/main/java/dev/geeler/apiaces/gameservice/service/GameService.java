package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GamePlayer;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameService {
    Optional<Game> getGame(Long roomId);

    Optional<Game> getGame(UUID gameId);

    Game createGame(UUID playerId);

    void joinGame(UUID gameId, Principal principal);

    void leaveGame(UUID gameId, UUID playerId, String username);

    void startGame(UUID gameId, UUID playerId);

    List<GamePlayer> getConnectedPlayers(UUID gameId);

    int getCurrentGamePlayerCount(UUID gameId);
}
