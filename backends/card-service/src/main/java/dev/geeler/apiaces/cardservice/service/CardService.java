package dev.geeler.apiaces.cardservice.service;

import dev.geeler.apiaces.cardservice.model.Card;

import java.util.List;
import java.util.UUID;

public interface CardService {
    Card getCard(UUID cardId);
    List<Card> getCards();
    List<UUID> getShuffledDeck();
}
