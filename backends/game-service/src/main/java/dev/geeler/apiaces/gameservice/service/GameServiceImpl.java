package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.exception.MaxGameSizeException;
import dev.geeler.apiaces.gameservice.exception.NotFoundException;
import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GamePlayer;
import dev.geeler.apiaces.gameservice.model.game.GameStatus;
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
                .orElseThrow(() -> new NotFoundException("No game was found with the provided id."));
    }

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
        return gameRepository.findById(gameId).orElseThrow(() -> new NotFoundException("The game was not found"));
    }

    /**
     * When the player's websocket connection is established, they will join the game.
     *
     * @param gameId   The UUID of the game to join
     * @param playerId The UUID of the player joining the game
     * @return The game object of the game that the player joined
     */
    @Override
    public Game joinGame(UUID gameId, UUID playerId) {
        final Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException("The game was not found"));
        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            if (game.getOwnerId().equals(playerId) && game.getStatus() == GameStatus.INITIALIZING) {
                final Game updatedGame = game.builder()
                        .setStatus(GameStatus.WAITING_FOR_PLAYERS)
                        .build();
                gameRepository.save(updatedGame); // TODO: create method in service to update game attributes
            } else {
                throw new IllegalStateException("Game is already running or complete.");
            }
        }
        GamePlayer gamePlayer = gamePlayerRepository.findByPlayerIdAndGameId(playerId, game.getId())
                .orElse(null);
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

    /**
     * Triggered when the player's websocket connection is closed.
     *
     * @param gameId   The UUID of the game to leave
     * @param playerId The UUID of the player leaving the game
     */
    @Override
    public void leaveGame(UUID gameId, UUID playerId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalStateException("Game not found"));
        GamePlayer gamePlayer = gamePlayerRepository.findByPlayerIdAndGameId(playerId, gameId)
                .orElseThrow(() -> new IllegalStateException("Player not found"));

        if (game.getStatus() != GameStatus.FINISHED) {
            gamePlayer = gamePlayer.toBuilder()
                    .leave()
                    .build();
            gamePlayerRepository.save(gamePlayer);
        }

        if (game.getOwnerId().equals(playerId)) {
            game = game.builder()
                    .setStatus(GameStatus.OWNER_LEFT)
                    .build();
            gameRepository.save(game);
            // TODO: broadcast deletion & force leave players
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
