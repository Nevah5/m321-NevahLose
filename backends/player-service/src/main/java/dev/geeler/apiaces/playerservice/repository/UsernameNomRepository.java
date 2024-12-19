package dev.geeler.apiaces.playerservice.repository;

import dev.geeler.apiaces.playerservice.model.UsernameNom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsernameNomRepository extends JpaRepository<UsernameNom, Long>{
    @Query(value = "SELECT a FROM username_nom a ORDER BY function('RAND') LIMIT 1", nativeQuery = false)
    UsernameNom getRandomNom();
}
