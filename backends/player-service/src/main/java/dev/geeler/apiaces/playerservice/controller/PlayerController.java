package dev.geeler.apiaces.playerservice.controller;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.exception.NotFoundException;
import dev.geeler.apiaces.playerservice.service.JwtService;
import dev.geeler.apiaces.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/self")
    public PlayerDto getSelf(@RequestHeader("Authorization") String authHeader) {
        final String token = authHeader.substring(7);
        final UUID playerId = jwtService.extractUserId(token);
        return new PlayerDto(
                playerService.loadById(playerId)
                        .orElseThrow(() -> new NotFoundException("Player not found")));
    }
}
