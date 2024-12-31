package dev.geeler.apiaces.gameservice.service;

import io.jsonwebtoken.Claims;

import java.util.UUID;

public interface JwtService {
    Claims extractClaims(String token);

    Boolean validateToken(String token);

    UUID extractUserId(String token);

    String extractUsername(String token);

}
