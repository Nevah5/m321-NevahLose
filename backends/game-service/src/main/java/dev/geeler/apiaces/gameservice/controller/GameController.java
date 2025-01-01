package dev.geeler.apiaces.gameservice.controller;

import dev.geeler.apiaces.gameservice.dto.CreateGameDto;
import dev.geeler.apiaces.gameservice.model.Game;
import dev.geeler.apiaces.gameservice.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class GameController {
    @Autowired
    private GameService gameService;

    // TODO: implement the endpoints

    @MessageMapping("/join")
    @SendTo("/topic/game")
    public Game createGame(@RequestBody CreateGameDto createGameDto) {
        return gameService.createGame(UUID.fromString(createGameDto.getPlayerId()));
    }
}
