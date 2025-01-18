package dev.geeler.apiaces.gameservice.service.impl;

import dev.geeler.apiaces.gameservice.exception.MaxGameSizeException;
import dev.geeler.apiaces.gameservice.exception.NotFoundException;
import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import dev.geeler.apiaces.gameservice.model.game.ChatType;
import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GamePlayer;
import dev.geeler.apiaces.gameservice.model.game.GameStatus;
import dev.geeler.apiaces.gameservice.model.security.UserPrincipal;
import dev.geeler.apiaces.gameservice.repository.GamePlayerRepository;
import dev.geeler.apiaces.gameservice.repository.GameRepository;
import dev.geeler.apiaces.gameservice.service.ChatService;
import dev.geeler.apiaces.gameservice.service.GameService;
import dev.geeler.apiaces.gameservice.service.KafkaService;
import dev.geeler.apiaces.gameservice.service.PlayerService;
import dev.geeler.apiaces.gameservice.service.WebsocketService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final ChatService chatService;
    private final PlayerService playerService;
    private final KafkaService kafkaService;
    private final WebsocketService websocketService;

    public GameServiceImpl(
            GameRepository gameRepository,
            GamePlayerRepository gamePlayerRepository,
            ChatService chatService,
            @Lazy PlayerService playerService,
            KafkaService kafkaService,
            @Lazy WebsocketService websocketService) {
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.chatService = chatService;
        this.playerService = playerService;
        this.kafkaService = kafkaService;
        this.websocketService = websocketService;
    }

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
    public void joinGame(UUID gameId, Principal principal) {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        final UserPrincipal userPrincipal = (UserPrincipal) token.getPrincipal();
        final Game game = this.getGame(gameId).orElseThrow(() -> new NotFoundException("The game was not found"));

        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            if (game.getOwnerId().equals(userPrincipal.getId()) && game.getStatus() == GameStatus.INITIALIZING) {
                this.updateGameStatus(game, GameStatus.WAITING_FOR_PLAYERS);
            } else {
                throw new IllegalStateException("This game is possibly not accessible anymore.");
            }
        }

        gamePlayerRepository.findByPlayerIdAndGameId(userPrincipal.getId(), game.getId())
                .ifPresent(gamePlayer -> {
                    if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS)
                        throw new IllegalStateException("Player already in game.");
                });

        if (getCurrentGamePlayerCount(gameId) >= game.getMaxPlayers()) {
            throw new MaxGameSizeException("Max game size reached!");
        }

        gamePlayerRepository.save(new GamePlayer.Builder()
                .setGameId(game.getId())
                .setPlayerId(userPrincipal.getId())
                .setUsername(userPrincipal.getUsername())
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
                .isHost(playerService.isOwnerOfGame(playerId, gameId))
                .isJoined(false)
                .type(ChatType.ACTIVITY)
                .build());


        if (game.getOwnerId().equals(playerId) && game.getStatus() != GameStatus.FINISHED) {
            updateGameStatus(game, GameStatus.OWNER_LEFT);
            kafkaService.sendMessage(
                    "games.terminate",
                    gameId
            );
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
