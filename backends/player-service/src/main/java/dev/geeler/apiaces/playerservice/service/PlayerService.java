package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.model.Player;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface PlayerService extends UserDetailsService {
    Player register(PlayerDto playerDto);
}
