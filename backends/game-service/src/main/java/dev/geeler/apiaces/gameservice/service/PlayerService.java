package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.security.UserPrincipal;

import java.util.Optional;
import java.util.UUID;

public interface PlayerService {
    void connectPlayer(UUID playerId, String sessionId);

    void disconnectPlayer(UserPrincipal principal);

    Optional<Game> getCurrentGame(UUID playerId);

    Optional<Game> getCurrentGame(UserPrincipal principal);

    Optional<UUID> getCurrentGameId(UUID playerId);

    Optional<UUID> getCurrentGameId(UserPrincipal principal);

    Optional<String> getSessionId(UUID playerId);
}
