package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.dto.PlayerDto;
import dev.geeler.apiaces.playerservice.model.Player;
import dev.geeler.apiaces.playerservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player register(PlayerDto playerDto) {
        System.out.println("Registering player");
        System.out.println(playerDto.getUsername());
        System.out.println(playerDto.getUsername().length());
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
        return Optional.of(playerRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
