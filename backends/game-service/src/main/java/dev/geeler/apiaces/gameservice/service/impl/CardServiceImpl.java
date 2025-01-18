package dev.geeler.apiaces.gameservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.geeler.apiaces.gameservice.client.CardServiceClient;
import dev.geeler.apiaces.gameservice.model.card.Card;
import dev.geeler.apiaces.gameservice.service.CardService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {
    private static final String FALLBACK_CARD_DATA_URL = "https://raw.githubusercontent.com/Nevah5/m321-api-aces/refs/heads/develop/resources/circuit_breaker/cards.json";

    private final CardServiceClient cardServiceClient;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @CircuitBreaker(name = "cardService", fallbackMethod = "fallbackGetCards")
    public List<Card> fetchCards() {
        return cardServiceClient.getCards();
    }

    public List<Card> fallbackGetCards(Throwable throwable) {
        try {
            log.warn("Fallback triggered: " + throwable.getMessage());
            String jsonResponse = restTemplate.getForObject(FALLBACK_CARD_DATA_URL, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return Arrays.asList(objectMapper.readValue(jsonResponse, Card[].class));
        } catch (Exception e) {
            log.error("Error in fallbackGetCards: " + e.getMessage());
            throw new RuntimeException("Fallback failed", e);
        }
    }

    @Override
    public List<UUID> getShuffledDeck() {
        return cardServiceClient.getShuffledDeck();
    }
}
