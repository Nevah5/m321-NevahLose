package dev.geeler.apiaces.gameservice.model.game;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Entity(name = "game-player")
@Table(name = "game_players")
@IdClass(GamePlayerId.class)
public class GamePlayer {
    @Id
    @Getter
    protected UUID gameId;

    @Id
    @Getter
    protected UUID playerId;

    @Getter
    protected String username;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    protected Date joinedAt;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    protected Date leftAt;

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

        public Builder setUsername(final String username) {
            this.gamePlayer.username = username;
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
