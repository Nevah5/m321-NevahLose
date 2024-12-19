package dev.geeler.apiaces.playerservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Entity(name = "player")
@Table(name = "players")
public class Player {
    @Id
    @Getter
    private UUID id;

    @Getter
    private String username;

    public static class Builder {
        private final Player player;

        public Builder() {
            player = new Player();
            player.id = UUID.randomUUID();
        }

        public Builder setUsername(String username) {
            player.username = username;
            return this;
        }

        public Player build() {
            return player;
        }
    }
}
