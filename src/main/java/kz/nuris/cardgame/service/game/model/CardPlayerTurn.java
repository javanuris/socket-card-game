package kz.nuris.cardgame.service.game.model;

import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.player.model.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CardPlayerTurn {
    private Player player;
    private List<Card> hand;
    private Decision decision;
}
