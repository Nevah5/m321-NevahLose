package dev.geeler.apiaces.gameservice.controller;

import dev.geeler.apiaces.gameservice.model.Game;
import dev.geeler.apiaces.gameservice.model.GameStatus;
import dev.geeler.apiaces.gameservice.service.GameService;
import dev.geeler.apiaces.gameservice.service.JwtService;
import dev.geeler.apiaces.gameservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    private final JwtService jwtService;

    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/games/rooms/{roomId}")
    public Game getGame(@PathVariable Long roomId) {
        Game game = gameService.getGame(roomId);
        if (game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            throw new IllegalStateException("Game is already started");
        }
        return game;
    }

    @PostMapping("/games/create")
    public Game createGame(@RequestHeader("Authorization") String authorizationHeader) {
        UUID playerId = jwtService.extractUserIdFromHeader(authorizationHeader);
        return gameService.createGame(playerId);
    }

    @MessageMapping("/games/{gameId}/join")
    public Game joinGame(@DestinationVariable Long gameId, @RequestHeader("Authorization") String authorizationHeader) {
        UUID playerId = jwtService.extractUserIdFromHeader(authorizationHeader);
        final Game game = gameService.joinGame(UUID.fromString(gameId.toString()), playerId);
        kafkaProducerService.sendMessage(game.getId(), playerId + " joined the game. (" + game.getId()+ ")");
        return game;
    }
}
