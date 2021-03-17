package kz.nuris.cardgame.service.game.impl;

import kz.nuris.cardgame.service.game.CardGameAbstract;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.game.model.GameResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DoubleCardGame extends CardGameAbstract {

    @Override
    public Map<Integer, GameResult> checkHands(CardPlayerTurn firstPlayer, CardPlayerTurn secondPlayer) {
        return super.checkHands(firstPlayer, secondPlayer);
    }
}
