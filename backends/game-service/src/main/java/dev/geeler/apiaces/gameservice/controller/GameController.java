package dev.geeler.apiaces.gameservice.controller;

import dev.geeler.apiaces.gameservice.dto.CreateGameDto;
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

    // TODO: implement the endpoints

    @PostMapping()
    public Game createGame(@RequestBody CreateGameDto createGameDto) {
        return gameService.createGame(UUID.fromString(createGameDto.getPlayerId()));
    }
}
