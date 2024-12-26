package dev.geeler.apiaces.playerservice.repository;

import dev.geeler.apiaces.playerservice.model.UsernameAdj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsernameAdjRepository extends JpaRepository<UsernameAdj, Long>{
    @Query(value = "SELECT a FROM username_adj a ORDER BY function('RAND') LIMIT 1", nativeQuery = false)
    UsernameAdj getRandomAdj();
}
