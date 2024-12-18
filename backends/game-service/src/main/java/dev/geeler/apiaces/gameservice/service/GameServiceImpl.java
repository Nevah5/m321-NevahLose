package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.Game;
import dev.geeler.apiaces.gameservice.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameRepository gameRepository;

    @Override
    public Game createGame(UUID playerId) {
        final Game game = new Game.Builder(playerId)
                .setCreatedAt()
                .build();
        gameRepository.save(game);
        return game;
    }

    @Override
    public Game getGame(UUID gameId) {
        return null;
    }

    @Override
    public Game joinGame(String roomId, UUID playerId) {
        return null;
    }
}
