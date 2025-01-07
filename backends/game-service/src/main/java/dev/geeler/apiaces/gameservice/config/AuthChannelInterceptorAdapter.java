package dev.geeler.apiaces.gameservice.config;

import dev.geeler.apiaces.gameservice.security.JwtAuthFilter;
import dev.geeler.apiaces.gameservice.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
    private static final String TOKEN_HEADER = "token";
    private final JwtService jwtService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            assert authorizationHeader != null;
            String token = authorizationHeader.substring(7);

            if (!jwtService.validateToken(token)) {
                throw new IllegalStateException("Invalid token");
            }

            String username = jwtService.extractUsername(token);
            UUID userId = jwtService.extractUserId(token);
            var userPrincipal = new JwtAuthFilter.CustomPrincipal(userId, username);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    null,
                    jwtService.extractAuthorities(token)
            );
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            accessor.setUser(usernamePasswordAuthenticationToken);
            accessor.setUser(usernamePasswordAuthenticationToken);
        }

        return message;
    }
}
