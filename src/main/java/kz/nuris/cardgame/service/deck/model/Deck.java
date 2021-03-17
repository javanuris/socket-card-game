package kz.nuris.cardgame.service.deck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Deck {
    private List<Card> cards = new ArrayList<>();

}
