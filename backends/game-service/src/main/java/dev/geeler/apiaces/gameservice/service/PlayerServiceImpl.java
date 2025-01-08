package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GamePlayer;
import dev.geeler.apiaces.gameservice.model.security.UserPrincipal;
import dev.geeler.apiaces.gameservice.repository.GamePlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final GamePlayerRepository gamePlayerRepository;
    private final GameService gameService;

    private final HashMap<UUID, String> playerSessionIdMapping = new HashMap<>();

    @Override
    public void connectPlayer(UUID playerId, String sessionId) {
        playerSessionIdMapping.put(playerId, sessionId);
    }

    @Override
    public void disconnectPlayer(UserPrincipal principal) {
        playerSessionIdMapping.remove(principal.getId());
        Optional.of(gamePlayerRepository.findByPlayerIdAndLeftAtIsNull(principal.getId()))
                .flatMap(gamePlayer -> gameService.getGame(gamePlayer.getGameId())).ifPresent(game -> {
                    gameService.leaveGame(game.getId(), principal.getId(), principal.getUsername());
                });
    }

    private UUID getPlayerIdFromSessionId(String sessionId) {
        return playerSessionIdMapping.entrySet().stream()
                .filter(entry -> entry.getValue().equals(sessionId))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    @Override
    public Optional<Game> getCurrentGame(UUID playerId) {
        Game game = null;
        GamePlayer player = gamePlayerRepository.findByPlayerIdAndLeftAtIsNull(playerId);
        if (player != null) {
            game = gameService.getGame(player.getGameId()).orElse(null);
        }
        return Optional.ofNullable(game);
    }

    @Override
    public Optional<Game> getCurrentGame(UserPrincipal principal) {
        return this.getCurrentGame(principal.getId());
    }

    @Override
    public Optional<UUID> getCurrentGameId(UUID playerId) {
        UUID gameId = null;
        Game game = this.getCurrentGame(playerId).orElse(null);
        if (game != null) {
            gameId = game.getId();
        }
        return Optional.ofNullable(gameId);
    }

    @Override
    public Optional<UUID> getCurrentGameId(UserPrincipal principal) {
        UUID gameId = null;
        Game game = this.getCurrentGame(principal).orElse(null);
        if (game != null) {
            gameId = game.getId();
        }
        return Optional.ofNullable(gameId);
    }

    @Override
    public Optional<String> getSessionId(UUID playerId) {
        return Optional.ofNullable(playerSessionIdMapping.get(playerId));
    }
}
