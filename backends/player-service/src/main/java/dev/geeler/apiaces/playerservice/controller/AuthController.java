package dev.geeler.apiaces.playerservice.controller;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.service.AuthService;
import dev.geeler.apiaces.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthController {
    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    public void register(@RequestBody PlayerDto playerDto) {
        playerService.register(playerDto);
        // TODO: return JWT
    }
}
