package dev.geeler.apiaces.gameservice.service.impl;

import dev.geeler.apiaces.gameservice.client.CardServiceClient;
import dev.geeler.apiaces.gameservice.model.card.Card;
import dev.geeler.apiaces.gameservice.service.CardService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardServiceClient cardServiceClient;

    @Override
    @CircuitBreaker(name = "cardService", fallbackMethod = "fallbackGetCards")
    public List<Card> fetchCards() {
        return cardServiceClient.getCards();
    }

    public List<Card> fallbackGetCards(Throwable t) {
        return List.of();
    }
}
