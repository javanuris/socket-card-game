package kz.nuris.cardgame.service.deck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Card {
    private Rank rank;
    private Suit suit;
}
