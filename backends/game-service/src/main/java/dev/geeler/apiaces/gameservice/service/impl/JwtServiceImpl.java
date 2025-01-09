package dev.geeler.apiaces.gameservice.service.impl;

import dev.geeler.apiaces.gameservice.model.security.UserPrincipal;
import dev.geeler.apiaces.gameservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public List<GrantedAuthority> extractAuthorities(String token) {
        return ((List<String>) extractClaims(token).get("roles", List.class))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public UUID getUserIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authentication in security context");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserPrincipal)) {
            throw new RuntimeException("Invalid principal type");
        }
        return ((UserPrincipal) principal).getId();
    }

    @Override
    public UUID getUserIdFromPrincipal(Principal principal) {
        var authToken = (UsernamePasswordAuthenticationToken) principal;
        var userPrincipal = (UserPrincipal) authToken.getPrincipal();
        return userPrincipal.getId();
    }

    @Override
    public String getUsernameFromPrincipal(Principal principal) {
        var authToken = (UsernamePasswordAuthenticationToken) principal;
        var userPrincipal = (UserPrincipal) authToken.getPrincipal();
        return userPrincipal.getUsername();
    }
}
