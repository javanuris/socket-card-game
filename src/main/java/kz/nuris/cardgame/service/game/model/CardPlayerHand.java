package kz.nuris.cardgame.service.game.model;

import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.player.model.Player;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CardPlayerHand {
    private Player player;
    private List<Card> hand;
}
