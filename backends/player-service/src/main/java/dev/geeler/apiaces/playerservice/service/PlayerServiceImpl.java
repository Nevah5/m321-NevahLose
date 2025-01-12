package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.model.Player;
import dev.geeler.apiaces.playerservice.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player register(PlayerDto playerDto) {
        log.info("Registering player {}", playerDto.getUsername());
        if (playerDto.getUsername().length() > 20) {
            throw new IllegalArgumentException("Username is too long");
        }
        final Player player = new Player.Builder()
                .setUsername(playerDto.getUsername())
                .build();
        playerRepository.save(player);
        return player;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public Optional<Player> loadById(UUID playerId) {
        return playerRepository.findById(playerId);
    }
}
