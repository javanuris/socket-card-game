package kz.nuris.cardgame.service.deck;

import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.deck.model.Deck;

import java.util.Collections;

public class DeckManager {

    private Deck deck;

    public DeckManager(Deck deck) {
        this.deck = deck;
    }

    /**
     * Shuffle deck
     */
    public void shuffle() {
        Collections.shuffle(deck.getCards());
    }

    /**
     * Take first card from the deck and if null then throw exception
     */
    public Card takeOrBlow() {
        var cards = deck.getCards();
        Card card = cards.stream().findFirst().orElseThrow();
        cards.remove(card);
        return card;
    }

    /**
     * Return card count on the deck
     * */
    public Integer count() {
        return deck.getCards().size();
    }

    public Deck getDeck() {
        return deck;
    }

}
