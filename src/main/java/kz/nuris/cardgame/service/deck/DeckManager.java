package kz.nuris.cardgame.service.deck;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.deck.model.Deck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class DeckManager {

    private final DeckService deckService;

    private Deck deck;

    @PostConstruct
    private void init() {
        deck = deckService.getDeck();
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
