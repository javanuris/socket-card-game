package kz.nuris.cardgame.service.game.impl;

import kz.nuris.cardgame.service.game.CardGame2PlayerAbstract;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.game.model.GameResult;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SingleCardGame extends CardGame2PlayerAbstract {

    @Override
    public Map<Integer, GameResult> defineWinnerAndLoser(CardPlayerTurn firstPlayer,
                                                         CardPlayerTurn secondPlayer) {
        return null;
    }
}
