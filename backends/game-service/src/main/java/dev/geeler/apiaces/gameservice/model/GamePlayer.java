package dev.geeler.apiaces.gameservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Entity(name = "GamePlayer")
@Table(name = "game_players")
public class GamePlayer {
    @Id
    @Getter
    private UUID gameId;

    @Id
    @Getter
    private UUID playerId;

    public GamePlayer(final UUID gameId, final UUID playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
