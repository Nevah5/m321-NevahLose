package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.model.Player;
import dev.geeler.apiaces.playerservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player register(PlayerDto playerDto) {
        if (playerDto.getUsername().length() > 20) {
            throw new IllegalArgumentException("Username is too long");
        }
        final Player player = new Player.Builder()
                .setUsername(playerDto.getUsername())
                .build();
        playerRepository.save(player);
        return player;
    }
}
