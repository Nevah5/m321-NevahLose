package dev.geeler.apiaces.gameservice.controller;

import dev.geeler.apiaces.gameservice.dto.ChatMessageDto;
import dev.geeler.apiaces.gameservice.dto.GameIdDto;
import dev.geeler.apiaces.gameservice.exception.NotFoundException;
import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import dev.geeler.apiaces.gameservice.model.game.ChatType;
import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GamePlayer;
import dev.geeler.apiaces.gameservice.model.game.GameStatus;
import dev.geeler.apiaces.gameservice.service.ChatService;
import dev.geeler.apiaces.gameservice.service.GameService;
import dev.geeler.apiaces.gameservice.service.JwtService;
import dev.geeler.apiaces.gameservice.service.KafkaService;
import dev.geeler.apiaces.gameservice.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*") // TODO: restrict this to the frontend URL
@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final ChatService chatService;
    private final PlayerService playerService;

    private final JwtService jwtService;

    private final KafkaService kafkaProducerService;

    @GetMapping("/games/rooms/{roomId}")
    public Game getGame(@PathVariable Long roomId) {
        Game game = gameService.getGame(roomId).orElseThrow(() ->
                new NotFoundException("Game with roomId " + roomId + " not found"));
        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            throw new IllegalStateException("Game is already started");
        }
        return game;
    }

    @GetMapping("/games/{gameId}/players")
    public List<GamePlayer> getPlayers(@PathVariable UUID gameId) {
        return gameService.getConnectedPlayers(gameId);
    }

    @PostMapping("/games/create")
    public Game createGame(@RequestHeader("Authorization") String authorizationHeader) {
        UUID playerId = jwtService.extractUserIdFromHeader(authorizationHeader);
        return gameService.createGame(playerId);
    }

    @MessageMapping("/games.joinGame")
    public void joinGame(@Payload GameIdDto joinGameDto, Principal principal) {
        UUID playerId = jwtService.getUserIdFromPrincipal(principal);
        String username = jwtService.getUsernameFromPrincipal(principal);
        gameService.joinGame(joinGameDto.getGameId(), principal);
        chatService.sendChatMessage(ChatMessage.builder()
                .gameId(joinGameDto.getGameId())
                .isJoined(true)
                .type(ChatType.ACTIVITY)
                .senderId(playerId)
                .senderUsername(username)
                .build()
        );
    }

    @MessageMapping("/games.sendMessage")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto, Principal principal) {
        UUID playerId = jwtService.getUserIdFromPrincipal(principal);
        String username = jwtService.getUsernameFromPrincipal(principal);
        if (chatMessageDto.message().equals("")) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        chatService.sendChatMessage(ChatMessage.builder()
                .message(chatMessageDto.message())
                .senderId(playerId)
                .senderUsername(username)
                .type(ChatType.MESSAGE)
                .gameId(playerService.getCurrentGameId(playerId).orElseThrow(() ->
                        new NotFoundException("Player not in game")))
                .build()
        );
    }
}
