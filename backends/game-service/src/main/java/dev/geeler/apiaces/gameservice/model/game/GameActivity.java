package dev.geeler.apiaces.gameservice.model.game;

import dev.geeler.apiaces.gameservice.dto.GamePlayerDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class GameActivity {
    private GameActivityType type;

    private UUID playerId;

    private UUID cardId;

    private UUID gameId;

    private String message;
    
    private List<GamePlayerDto> turnOrder;
}
