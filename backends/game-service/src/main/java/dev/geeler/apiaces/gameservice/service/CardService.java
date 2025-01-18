package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.card.Card;

import java.util.List;
import java.util.UUID;

public interface CardService {
    List<Card> fetchCards();

    List<UUID> getShuffledDeck();
}
