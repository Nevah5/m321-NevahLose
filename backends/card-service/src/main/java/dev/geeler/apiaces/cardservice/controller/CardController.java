package dev.geeler.apiaces.cardservice.controller;

import dev.geeler.apiaces.cardservice.model.Card;
import dev.geeler.apiaces.cardservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    private CardService cardService;

    @GetMapping("/shuffled")
    public List<UUID> getShuffledDeck() {
        return cardService.getShuffledDeck();
    }

    @GetMapping()
    public List<Card> getCards() {
        return cardService.getCards();
    }

    @GetMapping("/{id}")
    public Card getCard(@PathVariable("id") UUID id) {
        return cardService.getCard(id);
    }

    @GetMapping("/random")
    public List<Card> getRandom(@RequestParam("amount") int amount) {
        if (amount <= 0) {
            amount = 1;
        }
        if (amount > cardService.getCards().size()) {
            throw new IllegalArgumentException("Amount is greater than the number of cards in the deck");
        }
        List<Card> cards = cardService.getCards();
        List<Card> selected = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Card card = cards.get((int) (Math.random() * cards.size()));
            while (selected.contains(card)) {
                card = cards.get((int) (Math.random() * cards.size()));
            }
            selected.add(card);
        }
        return selected;
    }
}
