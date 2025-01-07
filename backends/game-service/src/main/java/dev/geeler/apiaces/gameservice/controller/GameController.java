package dev.geeler.apiaces.gameservice.controller;

import dev.geeler.apiaces.gameservice.model.game.Game;
import dev.geeler.apiaces.gameservice.model.game.GameStatus;
import dev.geeler.apiaces.gameservice.model.game.dto.GameIdDto;
import dev.geeler.apiaces.gameservice.service.GameService;
import dev.geeler.apiaces.gameservice.service.JwtService;
import dev.geeler.apiaces.gameservice.service.KafkaProducerService;
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
import java.util.UUID;

@CrossOrigin(origins = "*") // TODO: restrict this to the frontend URL
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

    @MessageMapping("/games.joinGame")
    public void joinGame(@Payload GameIdDto joinGameDto, Principal principal) {
        UUID playerId = jwtService.getUserIdFromPrincipal(principal);
        gameService.joinGame(joinGameDto.getGameId(), playerId);
        kafkaProducerService.sendMessage(joinGameDto.getGameId(), playerId + " joined the game. (" + joinGameDto.getGameId() + ")");
    }

    @MessageMapping("/games.leaveGame")
    public void leaveGame(@Payload GameIdDto leaveGameDto, Principal principal) {
        UUID playerId = jwtService.getUserIdFromPrincipal(principal);
        gameService.leaveGame(leaveGameDto.getGameId(), playerId);
        kafkaProducerService.sendMessage(leaveGameDto.getGameId(), playerId + " left the game. (" + leaveGameDto.getGameId() + ")");
    }
}
