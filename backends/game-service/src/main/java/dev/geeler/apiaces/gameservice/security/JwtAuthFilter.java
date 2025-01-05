package dev.geeler.apiaces.gameservice.security;

import dev.geeler.apiaces.gameservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = request.getParameter("token");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token != null && jwtService.validateToken(token)) {
            String userName = jwtService.extractUsername(token);
            UUID userId = jwtService.extractUserId(token);

            if (userName != null && userId != null) {
                var customPrincipal = new CustomPrincipal(userId, userName);

                var authToken = new UsernamePasswordAuthenticationToken(
                        customPrincipal,
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    public record CustomPrincipal(UUID id, String username) {
    }
}
