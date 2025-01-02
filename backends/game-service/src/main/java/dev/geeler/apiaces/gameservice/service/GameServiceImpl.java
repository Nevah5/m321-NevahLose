package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.exception.MaxGameSizeException;
import dev.geeler.apiaces.gameservice.exception.NotFoundException;
import dev.geeler.apiaces.gameservice.model.Game;
import dev.geeler.apiaces.gameservice.model.GamePlayer;
import dev.geeler.apiaces.gameservice.model.GameStatus;
import dev.geeler.apiaces.gameservice.repository.GamePlayerRepository;
import dev.geeler.apiaces.gameservice.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;

    @Override
    public Game getGame(Long roomId) {
        return gameRepository.findByRoomId(roomId.toString())
                .orElseThrow(() -> new NotFoundException("No game was found"));
    }

    @Override
    public Game createGame(UUID playerId) {
        final Game game = new Game.Builder()
                .setOwnerId(playerId)
                .setCreatedAt()
                .build();
        GamePlayer player = new GamePlayer.Builder()
                .setPlayerId(playerId)
                .setGameId(game.getId())
                .build();
        gameRepository.save(game);
        gamePlayerRepository.save(player);
        return game;
    }

    @Override
    public Game getGame(UUID gameId) {
        return gameRepository.findById(gameId).orElseThrow(() -> new NotFoundException("The game was not found"));
    }

    @Override
    public Game joinGame(UUID gameId, UUID playerId) {
        final Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException("The game was not found"));
        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS)
            throw new IllegalStateException("Game is already running or complete.");
        if (game.getOwnerId().equals(playerId))
            return game;
        GamePlayer gamePlayer = gamePlayerRepository.findByPlayerIdAndGameId(playerId, game.getId());
        if (gamePlayer != null)
            throw new IllegalStateException("Player already in game.");
        if (gamePlayerRepository.findGamePlayersByGameId(game.getId()).size() >= game.getMaxPlayers())
            throw new MaxGameSizeException("Max game size reached!");
        gamePlayer = new GamePlayer.Builder()
                .setGameId(game.getId())
                .setPlayerId(playerId)
                .build();
        gamePlayerRepository.save(gamePlayer);
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
