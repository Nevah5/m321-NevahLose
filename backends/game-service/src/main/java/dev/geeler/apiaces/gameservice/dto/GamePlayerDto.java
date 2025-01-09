package dev.geeler.apiaces.gameservice.dto;

import dev.geeler.apiaces.gameservice.model.game.GamePlayer;
import lombok.Getter;
import lombok.Setter;

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
