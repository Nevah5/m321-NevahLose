package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.http.HttpResponse;

import java.util.UUID;

public interface WebsocketService {
    void sendToUserIfConnected(UUID playerId, String destination, Object payload);

    void sendToUser(UUID playerId, String destination, Object payload);

    void sendError(UUID playerId, HttpResponse response);
}
