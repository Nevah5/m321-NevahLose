package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.Game;
import dev.geeler.apiaces.gameservice.model.GamePlayer;
import dev.geeler.apiaces.gameservice.repository.GamePlayerRepository;
import dev.geeler.apiaces.gameservice.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

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
        return gameRepository.findById(gameId).orElse(null);
    }

    @Override
    public Game joinGame(String roomId, UUID playerId) {
        // TODO: improve this mess
        final Game game = gameRepository.findByRoomId(roomId);
        if (game == null) {
            return null;
        }
        if (game.getOwnerId().equals(playerId)) {
            return game;
        }
        GamePlayer gamePlayer = gamePlayerRepository.findByPlayerIdAndGameId(playerId, game.getId());
        if (gamePlayer != null) {
            return null;
        }
        if (gamePlayerRepository.findByGameId(game.getId()).size() >= game.getMaxPlayers()) {
            return null;
        }
        gamePlayer = new GamePlayer.Builder()
                .setGameId(game.getId())
                .setPlayerId(playerId)
                .build();

        gamePlayerRepository.save(gamePlayer);
        gameRepository.save(game);
        return game;
    }
}
