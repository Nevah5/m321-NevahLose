package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.exception.MaxGameSizeException;
import dev.geeler.apiaces.gameservice.exception.NotFoundException;
import dev.geeler.apiaces.gameservice.model.game.*;
import dev.geeler.apiaces.gameservice.repository.GamePlayerRepository;
import dev.geeler.apiaces.gameservice.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final ChatService chatService;

    @Override
    public Optional<Game> getGame(Long roomId) {
        return Optional.ofNullable(gameRepository.findByRoomId(roomId.toString())); // TODO: FIX DUPLICATE ROOM ID CASE
    }

    @Override
    public Optional<Game> getGame(UUID gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public Game createGame(UUID ownerId) {
        Optional.ofNullable(gamePlayerRepository.findByPlayerIdAndLeftAtIsNull(ownerId))
                .ifPresent(game -> {
                    throw new IllegalStateException("You are already in a game! gameId: " + game.getGameId().toString());
                });
        final Game game = new Game.Builder()
                .setOwnerId(ownerId)
                .setCreatedAt()
                .build();
        gameRepository.save(game);
        return game;
    }

    @Override
    public void joinGame(UUID gameId, UUID playerId) {
        final Game game = this.getGame(gameId).orElseThrow(() -> new NotFoundException("The game was not found"));

        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            if (game.getOwnerId().equals(playerId) && game.getStatus() == GameStatus.INITIALIZING) {
                this.updateGameStatus(game, GameStatus.WAITING_FOR_PLAYERS);
            } else {
                throw new IllegalStateException("This game is possibly not accessible anymore.");
            }
        }

        gamePlayerRepository.findByPlayerIdAndGameId(playerId, game.getId())
                .ifPresent(gamePlayer -> {
                    if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS)
                        throw new IllegalStateException("Player already in game.");
                });

        if (getCurrentGamePlayerCount(gameId) >= game.getMaxPlayers()) {
            throw new MaxGameSizeException("Max game size reached!");
        }

        gamePlayerRepository.save(new GamePlayer.Builder()
                .setGameId(game.getId())
                .setPlayerId(playerId)
                .build());
    }

    @Override
    public void leaveGame(UUID gameId, UUID playerId, String username) {
        final Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalStateException("Game not found"));
        final GamePlayer gamePlayer = gamePlayerRepository.findByPlayerIdAndGameId(playerId, gameId)
                .orElseThrow(() -> new IllegalStateException("Player not found"));

        if (game.getStatus() != GameStatus.FINISHED) {
            gamePlayerRepository.save(gamePlayer.toBuilder()
                    .leave()
                    .build());
        }

        chatService.sendChatMessage(ChatMessage.builder()
                .gameId(gameId)
                .senderId(playerId)
                .senderUsername(username)
                .isJoined(false)
                .type(ChatType.ACTIVITY)
                .build());


        if (game.getOwnerId().equals(playerId)) {
            updateGameStatus(game, GameStatus.OWNER_LEFT);
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
    public List<GamePlayer> getConnectedPlayers(UUID gameId) {
        return gamePlayerRepository.findGamePlayersByGameId(gameId);
    }

    @Override
    public int getCurrentGamePlayerCount(UUID gameId) {
        return gamePlayerRepository.findGamePlayersByGameId(gameId).size();
    }

    private void updateGameStatus(Game game, GameStatus status) {
        gameRepository.save(game.builder()
                .setStatus(status)
                .build());
    }
}
