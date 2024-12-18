package dev.geeler.apiaces.gameservice.repository;

import dev.geeler.apiaces.gameservice.model.GameTurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameTurnRepository extends JpaRepository<GameTurn, UUID> {
}
