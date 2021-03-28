package kz.nuris.cardgame.chat.model;

import kz.nuris.cardgame.service.game.model.Decision;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PlayerDecisionMessage extends Message {
    private Decision decision;
}
