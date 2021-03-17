package kz.nuris.cardgame.service.deck;

import kz.nuris.cardgame.execptions.CardGameException;
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
    //TODO test
    public void shuffle() {
        Collections.shuffle(deck.getCards());
    }

    /**
     * Take first card from the deck
     * If null throw exception
     */
    //TODO test
    public Card takeCardOrBlow() {
        var cards = deck.getCards();
        Card card = cards.stream().findFirst().orElseThrow(() -> {
            throw new CardGameException(CardGameException.CardGameExceptionCode.DECK_IS_EMPTY);
        });
        cards.remove(card);
        return card;
    }

    /**
     * Return card count on the deck
     */
    //TODO test
    public Integer count() {
        return deck.getCards().size();
    }

    public Deck getDeck() {
        return deck;
    }

}
