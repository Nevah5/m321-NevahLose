package dev.geeler.apiaces.gameservice.controller;

import dev.geeler.apiaces.gameservice.model.Game;
import dev.geeler.apiaces.gameservice.service.GameService;
import dev.geeler.apiaces.gameservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public Game createGame(@RequestHeader("Authorization") String authorizationHeader) {
        UUID playerId = jwtService.extractUserIdFromHeader(authorizationHeader);
        return gameService.createGame(playerId);
    }

    @PostMapping("/join/{roomId}")
    public Game joinGame(@PathVariable Long roomId, @RequestHeader("Authorization") String authorizationHeader) {
        UUID playerId = jwtService.extractUserIdFromHeader(authorizationHeader);
        return gameService.joinGame(roomId, playerId);
    }

//    @MessageMapping("/join")
//    @SendTo("/topic/game")
//    public Game createGame(@RequestBody CreateGameDto createGameDto) {
//        return gameService.createGame(UUID.fromString(createGameDto.getPlayerId()));
//    }
}
