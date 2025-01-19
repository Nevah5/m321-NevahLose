package dev.geeler.apiaces.gameservice.controller;

import dev.geeler.apiaces.gameservice.dto.ChatMessageDto;
import dev.geeler.apiaces.gameservice.dto.GameIdDto;
import dev.geeler.apiaces.gameservice.dto.GamePlayerDto;
import dev.geeler.apiaces.gameservice.exception.NotFoundException;
import dev.geeler.apiaces.gameservice.model.game.ChatMessage;
import dev.geeler.apiaces.gameservice.model.game.ChatType;
import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GameStatus;
import dev.geeler.apiaces.gameservice.service.ChatService;
import dev.geeler.apiaces.gameservice.service.GameService;
import dev.geeler.apiaces.gameservice.service.JwtService;
import dev.geeler.apiaces.gameservice.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class GameController {
    private final GameService gameService;
    private final ChatService chatService;
    private final PlayerService playerService;

    private final JwtService jwtService;

    @GetMapping("/games/rooms/{roomId}")
    public Game getGame(@PathVariable Long roomId) {
        Game game = gameService.getGame(roomId)
                .orElseThrow(() -> new NotFoundException("Game with roomId " + roomId + " not found"));
        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            throw new IllegalStateException("Game is not accessible at the moment");
        }
        return game;
    }

    @GetMapping("/games/{gameId}/players")
    public List<GamePlayerDto> getPlayers(@PathVariable UUID gameId) {
        return gameService.getConnectedPlayers(gameId)
                .stream()
                .filter(player -> player.getLeftAt() == null)
                .map(GamePlayerDto::new)
                .peek(player -> player.setHost(playerService.isOwnerOfGame(player.getPlayerId(), gameId)))
                .toList();
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
                .isHost(playerService.isOwnerOfGame(playerId, joinGameDto.getGameId()))
                .senderId(playerId)
                .senderUsername(username)
                .build());
    }

    @MessageMapping("/games.sendMessage")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto, Principal principal) {
        UUID playerId = jwtService.getUserIdFromPrincipal(principal);
        String username = jwtService.getUsernameFromPrincipal(principal);
        if (chatMessageDto.message().equals("")) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        UUID gameId = playerService.getCurrentGameId(playerId)
                .orElseThrow(() -> new NotFoundException("Player not in game"));
        chatService.sendChatMessage(ChatMessage.builder()
                .message(chatMessageDto.message())
                .senderId(playerId)
                .senderUsername(username)
                .isHost(playerService.isOwnerOfGame(playerId, gameId))
                .type(ChatType.MESSAGE)
                .gameId(gameId)
                .build());
    }

    @MessageMapping("/games.startGame")
    public void startGame(@Payload GameIdDto gameIdDto, Principal principal) {
        UUID playerId = jwtService.getUserIdFromPrincipal(principal);
        gameService.startGame(gameIdDto.getGameId(), playerId);
    }
}
