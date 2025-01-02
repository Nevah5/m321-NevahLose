package dev.geeler.apiaces.gameservice.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;
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

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedAt;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date leftAt;

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder {
        private final GamePlayer gamePlayer;

        public Builder() {
            this.gamePlayer = new GamePlayer();
            this.gamePlayer.joinedAt = new Date();
        }

        public Builder(GamePlayer gamePlayer) {
            this.gamePlayer = gamePlayer;
        }

        public Builder setGameId(final UUID gameId) {
            this.gamePlayer.gameId = gameId;
            return this;
        }

        public Builder setPlayerId(final UUID playerId) {
            this.gamePlayer.playerId = playerId;
            return this;
        }

        public Builder leave() {
            this.gamePlayer.leftAt = new Date();
            return this;
        }

        public GamePlayer build() {
            return this.gamePlayer;
        }

    }
}
