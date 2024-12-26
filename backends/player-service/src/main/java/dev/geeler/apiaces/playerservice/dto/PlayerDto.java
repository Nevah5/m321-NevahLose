package dev.geeler.apiaces.playerservice.dto;

import dev.geeler.apiaces.playerservice.model.Player;
import lombok.*;

import java.util.UUID;

public class PlayerDto {
    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    private String username;

    public PlayerDto() {}

    public PlayerDto(Player player) {
        this.id = player.getId();
        this.username = player.getUsername();
    }
}
