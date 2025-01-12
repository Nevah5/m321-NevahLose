package dev.geeler.apiaces.gameservice.service;

import dev.geeler.apiaces.gameservice.model.card.Card;

import java.util.List;

public interface CardService {
    List<Card> fetchCards();
}
