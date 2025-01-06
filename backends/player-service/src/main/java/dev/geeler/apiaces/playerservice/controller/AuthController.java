package dev.geeler.apiaces.playerservice.controller;

import dev.geeler.apiaces.playerservice.dto.JwtResponseDto;
import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.model.Player;
import dev.geeler.apiaces.playerservice.service.JwtService;
import dev.geeler.apiaces.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // TODO: restrict this to the frontend URL
@RequestMapping("/auth")
@RestController
public class AuthController {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public JwtResponseDto register(@RequestBody PlayerDto playerDto, @Value("${application.security.jwt.expiration}") final long expiration) {
        final Player player = playerService.register(playerDto);
        return new JwtResponseDto(jwtService.generateToken(player), expiration / 1000);
    }
}
