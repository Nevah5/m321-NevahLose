package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.model.Player;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {

    String generateToken(Player player);

    Boolean validateToken(String token, UserDetails userDetails);

    String extractUsername(String token);

    Date extractExpiration(String token);
}
