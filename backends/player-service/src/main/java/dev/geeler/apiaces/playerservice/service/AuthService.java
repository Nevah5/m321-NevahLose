package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.model.Player;

public interface AuthService {
    String generateJwtToken(Player player);
}
