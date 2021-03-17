package kz.nuris.cardgame.service.deck;

import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.deck.model.Deck;
import kz.nuris.cardgame.service.deck.model.Rank;
import kz.nuris.cardgame.service.deck.model.Suit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeckService {

    public Deck getDeck() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Card(Rank.TWO, Suit.CLUB));
        cards.add(new Card(Rank.THREE, Suit.CLUB));
        cards.add(new Card(Rank.FOUR, Suit.CLUB));
        cards.add(new Card(Rank.FIVE, Suit.CLUB));
        cards.add(new Card(Rank.SIX, Suit.CLUB));
        cards.add(new Card(Rank.SEVEN, Suit.CLUB));
        cards.add(new Card(Rank.EIGHT, Suit.CLUB));
        cards.add(new Card(Rank.NINE, Suit.CLUB));
        cards.add(new Card(Rank.TEN, Suit.CLUB));
        cards.add(new Card(Rank.JACK, Suit.CLUB));
        cards.add(new Card(Rank.QUEEN, Suit.CLUB));
        cards.add(new Card(Rank.KING, Suit.CLUB));
        cards.add(new Card(Rank.ACE, Suit.CLUB));

        cards.add(new Card(Rank.TWO, Suit.DIAMOND));
        cards.add(new Card(Rank.THREE, Suit.DIAMOND));
        cards.add(new Card(Rank.FOUR, Suit.DIAMOND));
        cards.add(new Card(Rank.FIVE, Suit.DIAMOND));
        cards.add(new Card(Rank.SIX, Suit.DIAMOND));
        cards.add(new Card(Rank.SEVEN, Suit.DIAMOND));
        cards.add(new Card(Rank.EIGHT, Suit.DIAMOND));
        cards.add(new Card(Rank.NINE, Suit.DIAMOND));
        cards.add(new Card(Rank.TEN, Suit.DIAMOND));
        cards.add(new Card(Rank.JACK, Suit.DIAMOND));
        cards.add(new Card(Rank.QUEEN, Suit.DIAMOND));
        cards.add(new Card(Rank.KING, Suit.DIAMOND));
        cards.add(new Card(Rank.ACE, Suit.DIAMOND));

        cards.add(new Card(Rank.TWO, Suit.HEART));
        cards.add(new Card(Rank.THREE, Suit.HEART));
        cards.add(new Card(Rank.FOUR, Suit.HEART));
        cards.add(new Card(Rank.FIVE, Suit.HEART));
        cards.add(new Card(Rank.SIX, Suit.HEART));
        cards.add(new Card(Rank.SEVEN, Suit.HEART));
        cards.add(new Card(Rank.EIGHT, Suit.HEART));
        cards.add(new Card(Rank.NINE, Suit.HEART));
        cards.add(new Card(Rank.TEN, Suit.HEART));
        cards.add(new Card(Rank.JACK, Suit.HEART));
        cards.add(new Card(Rank.QUEEN, Suit.HEART));
        cards.add(new Card(Rank.KING, Suit.HEART));
        cards.add(new Card(Rank.ACE, Suit.HEART));

        cards.add(new Card(Rank.TWO, Suit.SPADE));
        cards.add(new Card(Rank.THREE, Suit.SPADE));
        cards.add(new Card(Rank.FOUR, Suit.SPADE));
        cards.add(new Card(Rank.FIVE, Suit.SPADE));
        cards.add(new Card(Rank.SIX, Suit.SPADE));
        cards.add(new Card(Rank.SEVEN, Suit.SPADE));
        cards.add(new Card(Rank.EIGHT, Suit.SPADE));
        cards.add(new Card(Rank.NINE, Suit.SPADE));
        cards.add(new Card(Rank.TEN, Suit.SPADE));
        cards.add(new Card(Rank.JACK, Suit.SPADE));
        cards.add(new Card(Rank.QUEEN, Suit.SPADE));
        cards.add(new Card(Rank.KING, Suit.SPADE));
        cards.add(new Card(Rank.ACE, Suit.SPADE));

        return new Deck(cards);
    }
}
