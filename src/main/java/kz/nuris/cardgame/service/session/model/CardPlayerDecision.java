package kz.nuris.cardgame.service.session.model;

import kz.nuris.cardgame.service.game.model.Decision;
import kz.nuris.cardgame.service.player.model.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CardPlayerDecision {
    private Player player;
    private Decision decision;
}
