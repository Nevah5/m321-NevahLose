package dev.geeler.apiaces.gameservice.repository;

import dev.geeler.apiaces.gameservice.model.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    Optional<Game> findByRoomId(String roomId);

    Game findByOwnerId(UUID ownerId);
}
