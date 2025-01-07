package dev.geeler.apiaces.gameservice.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface JwtService {
    Claims extractClaims(String token);

    Boolean validateToken(String token);

    UUID extractUserId(String token);

    String extractUsername(String token);

    String extractTokenFromHeader(String header);

    UUID extractUserIdFromHeader(String header);

    List<GrantedAuthority> extractAuthorities(String token);

    UUID getUserIdFromSecurityContext();

    UUID getUserIdFromPrincipal(Principal principal);

    String getUsernameFromPrincipal(Principal principal);
}
