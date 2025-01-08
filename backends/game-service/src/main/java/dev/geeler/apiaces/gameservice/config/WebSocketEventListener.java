package dev.geeler.apiaces.gameservice.config;

import dev.geeler.apiaces.gameservice.model.security.UserPrincipal;
import dev.geeler.apiaces.gameservice.service.PlayerService;
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
    private final PlayerService playerService;

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getUser();
        UserPrincipal principal = (UserPrincipal) token.getPrincipal();

        playerService.connectPlayer(principal.getId(), sessionId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getUser();
        UserPrincipal principal = (UserPrincipal) token.getPrincipal();

        playerService.disconnectPlayer(principal);
    }
}
