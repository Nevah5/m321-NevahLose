package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.model.Player;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.UUID;

public interface JwtService {

    Claims extractClaims(String token);

    Boolean validateToken(String token);

    String generateToken(Player player);

    UUID extractUserId(String token);

    String extractUsername(String token);
}
