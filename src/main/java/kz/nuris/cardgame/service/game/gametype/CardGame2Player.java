package kz.nuris.cardgame.service.game.gametype;

import kz.nuris.cardgame.service.game.CardGame;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.game.model.GameResult;

import java.util.Map;

public interface CardGame2Player extends CardGame {

    Map<Integer, GameResult> defineWinnerAndLoser(CardPlayerTurn firstPlayer,
                                                  CardPlayerTurn secondPlayer);
}
