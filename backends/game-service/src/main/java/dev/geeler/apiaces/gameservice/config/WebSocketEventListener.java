package dev.geeler.apiaces.gameservice.config;

import dev.geeler.apiaces.gameservice.security.JwtAuthFilter;
import dev.geeler.apiaces.gameservice.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final GameService gameService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getUser();
        JwtAuthFilter.CustomPrincipal principal = (JwtAuthFilter.CustomPrincipal) token.getPrincipal();

        gameService.disconnectUser(principal.id(), principal.username());
    }

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getUser();
        JwtAuthFilter.CustomPrincipal principal = (JwtAuthFilter.CustomPrincipal) token.getPrincipal();
        gameService.connectUser(principal.id(), sessionId);
    }
}
