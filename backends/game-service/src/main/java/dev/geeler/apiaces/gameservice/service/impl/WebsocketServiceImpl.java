package dev.geeler.apiaces.gameservice.service.impl;

import dev.geeler.apiaces.gameservice.model.http.HttpResponse;
import dev.geeler.apiaces.gameservice.service.PlayerService;
import dev.geeler.apiaces.gameservice.service.WebsocketService;
import dev.geeler.apiaces.gameservice.util.MessageHeadersUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebsocketServiceImpl implements WebsocketService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PlayerService playerService;

    @Override
    public void sendToUserIfConnected(UUID playerId, String destination, Object payload) {
        playerService.getSessionId(playerId).ifPresent(sessionId -> simpMessagingTemplate.convertAndSendToUser(
                sessionId,
                destination,
                payload,
                MessageHeadersUtil.createHeaders(sessionId)
        ));
    }

    @Override
    public void sendToUser(UUID playerId, String destination, Object payload) {
        String sessionId = playerService.getSessionId(playerId).orElseThrow(() ->
                new IllegalStateException("Player does not have a session id in this instance"));
        simpMessagingTemplate.convertAndSendToUser(
                sessionId,
                destination,
                payload,
                MessageHeadersUtil.createHeaders(sessionId)
        );
    }

    @Override
    public void sendError(UUID playerId, HttpResponse response) {
        String sessionId = playerService.getSessionId(playerId).orElseThrow(() ->
                new IllegalStateException("Player does not have a session id in this instance"));
        simpMessagingTemplate.convertAndSendToUser(
                sessionId,
                "/queue/errors",
                response,
                MessageHeadersUtil.createHeaders(sessionId)
        );
    }

    @Override
    public void sendInfo(UUID playerId, String message) {
        String sessionId = playerService.getSessionId(playerId).orElseThrow(() ->
                new IllegalStateException("Player does not have a session id in this instance"));
        simpMessagingTemplate.convertAndSendToUser(
                sessionId,
                "/queue/info",
                message,
                MessageHeadersUtil.createHeaders(sessionId)
        );
    }
}
