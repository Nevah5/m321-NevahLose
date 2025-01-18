package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.model.Player;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {
    private final Key key;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    public JwtServiceImpl(@Value("${application.security.jwt.secret-key}") String secret) {
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
            return false;
        }
    }

    @Override
    public String generateToken(final Player player) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", player.getId());
        claims.put("roles", player.getAuthorities());
        return createToken(claims, player.getUsername());
    }

    @Override
    public UUID extractUserId(String token) {
        return UUID.fromString((String) extractClaims(token).get("id"));
    }

    @Override
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
