package dev.geeler.apiaces.gameservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Random;
import java.util.UUID;

@Entity(name = "game")
@Table(name = "games")
public class Game {
    @Id
    @Getter
    private UUID id;

    @Getter
    private String roomId;

    @Getter
    private UUID ownerId;

    @Getter
    private GameStatus status;

    @Getter
    private int maxPlayers = 4;

    @Getter
    private long createdAt;

    @Getter
    private Long startedAt;

    @Getter
    private UUID currentTurnId;

    @Getter
    private UUID winnerId;

    public Builder builder() {
        return new Builder(this);
    }

    public static class Builder {
        private final Game game;

        public Builder(final Game game) {
            this.game = game;
        }

        public Builder() {
            Random random = new Random();
            this.game = new Game();
            this.game.id = UUID.randomUUID();
            int randomNumber = random.nextInt(1000000);
            this.game.roomId = String.format("%06d", randomNumber);

            this.game.status = GameStatus.INITIALIZING;
            this.game.createdAt = System.currentTimeMillis();
        }

        public Builder setOwnerId(final UUID ownerId) {
            this.game.ownerId = ownerId;
            return this;
        }

        public Builder setStatus(final GameStatus status) {
            this.game.status = status;
            return this;
        }

        public Builder setMaxPlayers(final int maxPlayers) {
            this.game.maxPlayers = maxPlayers;
            return this;
        }

        public Builder setCreatedAt() {
            this.game.createdAt = System.currentTimeMillis();
            return this;
        }

        public Builder setCreatedAt(final long createdAt) {
            this.game.createdAt = createdAt;
            return this;
        }

        public Builder setStartedAt() {
            this.game.startedAt = System.currentTimeMillis();
            return this;
        }

        public Builder setStartedAt(final long startedAt) {
            this.game.startedAt = startedAt;
            return this;
        }

        public Builder setCurrentTurnId(final UUID currentTurnId) {
            this.game.currentTurnId = currentTurnId;
            return this;
        }

        public Builder setWinnerId(final UUID winnerId) {
            this.game.winnerId = winnerId;
            return this;
        }

        public Game build() {
            return this.game;
        }
    }
}
