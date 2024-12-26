package dev.geeler.apiaces.gameservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Entity(name = "game-player")
@Table(name = "game_players")
@IdClass(GamePlayerId.class)
public class GamePlayer {
    @Id
    @Getter
    private UUID gameId;

    @Id
    @Getter
    private UUID playerId;

    public static class Builder {
        private final GamePlayer gamePlayer;

        public Builder() {
            this.gamePlayer = new GamePlayer();
        }

        public Builder setGameId(final UUID gameId) {
            this.gamePlayer.gameId = gameId;
            return this;
        }

        public Builder setPlayerId(final UUID playerId) {
            this.gamePlayer.playerId = playerId;
            return this;
        }

        public GamePlayer build() {
            return this.gamePlayer;
        }

    }
}
