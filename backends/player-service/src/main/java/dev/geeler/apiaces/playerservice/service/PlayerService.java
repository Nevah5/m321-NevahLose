package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.model.Player;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface PlayerService extends UserDetailsService {
    Player register(PlayerDto playerDto);

    Optional<Player> loadById(UUID playerId);
}
