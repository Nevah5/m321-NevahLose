package dev.geeler.apiaces.playerservice.service;

import dev.geeler.apiaces.playerservice.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private PlayerService playerService;

    @Override
    public String generateJwtToken(Player player) {
        return null;
    }
}
