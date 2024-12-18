package dev.geeler.apiaces.gameservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "GameTurn")
@Table(name = "game_turns")
public class GameTurn {
    @Id
    @Getter
    private UUID id;

    @Getter
    private UUID gameId;

    @Getter
    private UUID playerId;

    @Getter
    @Setter
    private UUID nextTurnId;

    @Getter
    private long playedAt;

    @Getter
    private UUID playedCardId;

    public void playTurn(final UUID playedCardId) {
        this.playedCardId = playedCardId;
        this.playedAt = System.currentTimeMillis();
    }

    public static class Builder {
        private final GameTurn gameTurn;

        public Builder() {
            this.gameTurn = new GameTurn();
        }

        public Builder setGameId(final UUID gameId) {
            this.gameTurn.gameId = gameId;
            return this;
        }

        public Builder setPlayerId(final UUID playerId) {
            this.gameTurn.playerId = playerId;
            return this;
        }

        public Builder setNextTurnId(final UUID nextTurnId) {
            this.gameTurn.nextTurnId = nextTurnId;
            return this;
        }

        public GameTurn build() {
            return this.gameTurn;
        }
    }
}
