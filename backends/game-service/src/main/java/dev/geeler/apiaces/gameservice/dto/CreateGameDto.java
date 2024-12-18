package dev.geeler.apiaces.gameservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class CreateGameDto {
    @Getter
    @Setter
    private String playerId;
}
