package dev.geeler.apiaces.cardservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity(name = "card")
@Table(name = "cards")
public class Card {
    @Id
    @Getter
    private String id;

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    private String image;

    @Getter
    private CardType cardType;

    @Getter
    private Integer amountInDeck;

    public static class Builder {
        private final Card card = new Card();

        public void setId(String id) {
            card.id = id;
        }

        public void setName(String name) {
            card.name = name;
        }

        public void setDescription(String description) {
            card.description = description;
        }

        public void setImage(String image) {
            card.image = image;
        }

        public void setCardType(CardType cardType) {
            card.cardType = cardType;
        }

        public void setAmountInDeck(Integer amountInDeck) {
            card.amountInDeck = amountInDeck;
        }

        public Card build() {
            return card;
        }
    }
}
