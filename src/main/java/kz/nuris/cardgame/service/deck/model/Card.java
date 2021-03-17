package kz.nuris.cardgame.service.deck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Card implements Comparable<Card>{
    private Rank rank;
    private Suit suit;

    @Override
    public int compareTo(Card card) {
        return this.rank.getValue().compareTo(card.getRank().getValue());
    }
}
