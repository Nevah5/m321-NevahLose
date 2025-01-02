package dev.geeler.apiaces.gameservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {
    private static final Logger LOGGER = LogManager.getLogger(JwtServiceImpl.class);

    private final Key key;

    public JwtServiceImpl(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            LOGGER.info("Invalid token with exception: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String extractTokenFromHeader(String header) {
        return header.substring(7);
    }

    @Override
    public UUID extractUserId(String token) {
        return UUID.fromString((String) extractClaims(token).get("id"));
    }

    @Override
    public UUID extractUserIdFromHeader(String header) {
        return extractUserId(extractTokenFromHeader(header));
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
}
