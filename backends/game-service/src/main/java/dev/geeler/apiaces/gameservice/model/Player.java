package dev.geeler.apiaces.gameservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public class Player {
    @Getter
    private final UUID id;

    @Getter
    private final String username;
}
