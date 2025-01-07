package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.exception.MaxGameSizeException;
import dev.geeler.apiaces.gameservice.exception.NotFoundException;
import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import dev.geeler.apiaces.gameservice.model.game.ChatType;
import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GamePlayer;
import dev.geeler.apiaces.gameservice.model.game.GameStatus;
import dev.geeler.apiaces.gameservice.repository.GamePlayerRepository;
import dev.geeler.apiaces.gameservice.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final HashMap<UUID, String> playerSessionIdMapping = new HashMap<>();

    @Override
    public UUID getCurrentGameIdFromPlayer(UUID playerId) {
        GamePlayer player = gamePlayerRepository.findByPlayerIdAndLeftAtIsNull(playerId);
        if (player == null) {
            throw new IllegalStateException("Player not in game.");
        }
        return player.getGameId();
    }

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
     * When the player's websocket connection is established, they will join the
     * game.
     *
     * @param gameId   The UUID of the game to join
     * @param playerId The UUID of the player joining the game
     * @return The game object of the game that the player joined
     */
    @Override
    public void joinGame(UUID gameId, UUID playerId) {
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
        if (gamePlayer != null && game.getStatus() != GameStatus.WAITING_FOR_PLAYERS)
            throw new IllegalStateException("Player already in game.");
        if (gamePlayerRepository.findGamePlayersByGameId(game.getId()).size() >= game.getMaxPlayers())
            throw new MaxGameSizeException("Max game size reached!");
        gamePlayer = new GamePlayer.Builder()
                .setGameId(game.getId())
                .setPlayerId(playerId)
                .build();
        gamePlayerRepository.save(gamePlayer);
    }

    /**
     * Triggered when the player's websocket connection is closed.
     *
     * @param gameId   The UUID of the game to leave
     * @param playerId The UUID of the player leaving the game
     */
    @Override
    public void leaveGame(UUID gameId, UUID playerId, String username) {
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

        this.getPlayers(gameId).forEach(player -> {
            if (playerSessionIdMapping.containsKey(player.getPlayerId())) {
                String sessionId = playerSessionIdMapping.get(player.getPlayerId());
                simpMessagingTemplate.convertAndSendToUser(
                        sessionId,
                        "/queue/chat",
                        ChatMessage.builder()
                                .gameId(gameId)
                                .senderUsername(username)
                                .isJoined(false)
                                .senderId(playerId)
                                .type(ChatType.ACTIVITY)
                                .build(),
                        createHeaders(sessionId)
                );
            }
        });
        // TODO: broadcast to all springboot instances via kafka

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

    @Override
    public void sendChatMessage(ChatMessage chatMessage) {
        this.getPlayers(chatMessage.getGameId()).forEach(player -> {
            if (playerSessionIdMapping.containsKey(player.getPlayerId())) {
                String sessionId = playerSessionIdMapping.get(player.getPlayerId());
                simpMessagingTemplate.convertAndSendToUser(
                        sessionId,
                        "/queue/chat",
                        chatMessage,
                        createHeaders(sessionId)
                );
            }
        });
        // TODO: send to kafka
    }

    @Override
    public void connectUser(UUID playerId, String sessionId) {
        playerSessionIdMapping.put(playerId, sessionId);
    }

    @Override
    public void disconnectUser(UUID playerId, String username) {
        try {
            UUID gameId = this.getCurrentGameIdFromPlayer(playerId);
            this.leaveGame(gameId, playerId, username);
        } catch (IllegalArgumentException e) {
            // user not in any game
        }
        playerSessionIdMapping.remove(playerId);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();

    }
}
