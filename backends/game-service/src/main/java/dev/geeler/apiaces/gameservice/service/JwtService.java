package dev.geeler.apiaces.gameservice.service;

import io.jsonwebtoken.Claims;

import java.util.UUID;

public interface JwtService {
    Claims extractClaims(String token);

    Boolean validateToken(String token);

    UUID extractUserId(String token);

    String extractUsername(String token);

    String extractTokenFromHeader(String header);

    UUID extractUserIdFromHeader(String header);

    UUID getUserIdFromSecurityContext();
}
