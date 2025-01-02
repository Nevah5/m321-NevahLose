package dev.geeler.apiaces.gameservice.controller;

import dev.geeler.apiaces.gameservice.dto.PlayerIdDto;
import dev.geeler.apiaces.gameservice.model.Game;
import dev.geeler.apiaces.gameservice.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public Game createGame(@RequestBody PlayerIdDto playerIdDto) {
        return gameService.createGame(UUID.fromString(playerIdDto.getPlayerId()));
    }

    @PostMapping("/join/{roomId}")
    public Game joinGame(@PathVariable Long roomId, @RequestBody PlayerIdDto playerIdDto) {
        return gameService.joinGame(roomId, UUID.fromString(playerIdDto.getPlayerId()));
    }

//    @MessageMapping("/join")
//    @SendTo("/topic/game")
//    public Game createGame(@RequestBody CreateGameDto createGameDto) {
//        return gameService.createGame(UUID.fromString(createGameDto.getPlayerId()));
//    }
}
