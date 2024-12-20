package dev.geeler.apiaces.playerservice.controller;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.model.Player;
import dev.geeler.apiaces.playerservice.service.JwtService;
import dev.geeler.apiaces.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private JwtService jwtService;

    @GetMapping("/self")
    public PlayerDto getSelf(@RequestHeader("Authorization") String authHeader){
        final String token = authHeader.substring(7);
        final String username = jwtService.extractUsername(token);
        return new PlayerDto((Player) playerService.loadUserByUsername(username));
    }
}
