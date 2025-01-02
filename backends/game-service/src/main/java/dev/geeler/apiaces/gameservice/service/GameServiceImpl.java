package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.Game;
import dev.geeler.apiaces.gameservice.model.GamePlayer;
import dev.geeler.apiaces.gameservice.model.GameStatus;
import dev.geeler.apiaces.gameservice.repository.GamePlayerRepository;
import dev.geeler.apiaces.gameservice.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Override
    public Game createGame(UUID playerId) {
        final Game game = new Game.Builder()
                .setOwnerId(playerId)
                .setCreatedAt()
                .build();
        gameRepository.save(game);
        return game;
    }

    @Override
    public Game getGame(UUID gameId) {
        return gameRepository.findById(gameId).orElse(null);
    }

    @Override
    public Game joinGame(String roomId, UUID playerId) {
        // TODO: improve this mess
        final Game game = gameRepository.findByRoomId(roomId);
        if (game == null) {
            return null;
        }
        if (game.getOwnerId().equals(playerId)) {
            return game;
        }
        GamePlayer gamePlayer = gamePlayerRepository.findByPlayerIdAndGameId(playerId, game.getId());
        if (gamePlayer != null) {
            return null;
        }
        if (gamePlayerRepository.findByGameId(game.getId()).size() >= game.getMaxPlayers()) {
            return null;
        }
        gamePlayer = new GamePlayer.Builder()
                .setGameId(game.getId())
                .setPlayerId(playerId)
                .build();

        gamePlayerRepository.save(gamePlayer);
        gameRepository.save(game);
        return game;
    }

    @Override
    public void leaveGame(UUID gameId, UUID playerId) {
        GamePlayer gamePlayer = gamePlayerRepository.findByPlayerIdAndGameId(playerId, gameId);
        if (gamePlayer == null) throw new IllegalStateException("Player not found");
        gamePlayer = gamePlayer.toBuilder()
                .leave()
                .build();
        gamePlayerRepository.save(gamePlayer);

        Game game = gameRepository.findById(gameId).orElseThrow(() -> new IllegalStateException("Game not found"));
        if (game.getOwnerId().equals(playerId)) {
            game = game.builder()
                    .setStatus(GameStatus.OWNER_LEFT)
                    .build();
            gameRepository.save(game);
            // TODO: broadcast deletion
        }
    }

    @Override
    public void startGame(UUID gameId, UUID playerId) {
        final Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            return;
        }
        if (!game.getOwnerId().equals(playerId)) {
            return;
        }
        game.builder()
                .setStatus(GameStatus.IN_PROGRESS)
                .setStartedAt()
                .build();
        gameRepository.save(game);
    }

    @Override
    public List<GamePlayer> getPlayers(UUID gameId) {
        return gamePlayerRepository.findGamePlayersByGameId(gameId);
    }
}
