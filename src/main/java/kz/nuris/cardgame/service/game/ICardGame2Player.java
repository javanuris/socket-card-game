package kz.nuris.cardgame.service.game;

import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.game.model.GameResult;

import java.util.Map;

public interface ICardGame2Player extends ICardGame {

    int FIRST_PLAYER = 0;
    int SECOND_PLAYER = 1;

    Map<Integer, GameResult> defineWinnerAndLoser(CardPlayerTurn firstPlayer,
                                                  CardPlayerTurn secondPlayer);
}
