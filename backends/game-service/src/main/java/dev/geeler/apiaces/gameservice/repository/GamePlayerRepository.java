package dev.geeler.apiaces.gameservice.repository;

import dev.geeler.apiaces.gameservice.model.game.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GamePlayerRepository extends JpaRepository<GamePlayer, UUID> {
    Optional<GamePlayer> findByPlayerIdAndGameId(UUID playerId, UUID gameId);

    List<GamePlayer> findGamePlayersByGameId(UUID gameId);

    List<GamePlayer> findByGameId(UUID gameId);
}
