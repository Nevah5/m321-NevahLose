package dev.geeler.apiaces.gameservice.dto;

import dev.geeler.apiaces.gameservice.model.game.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
public class GamePlayerDto extends GamePlayer {
    @Setter
    @Getter
    private boolean isHost;

    public GamePlayerDto(GamePlayer gamePlayer) {
        this.gameId = gamePlayer.getGameId();
        this.playerId = gamePlayer.getPlayerId();
        this.username = gamePlayer.getUsername();
        this.joinedAt = gamePlayer.getJoinedAt();
        this.leftAt = gamePlayer.getLeftAt();
    }
}
