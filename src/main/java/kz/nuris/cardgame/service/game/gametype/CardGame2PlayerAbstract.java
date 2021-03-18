package kz.nuris.cardgame.service.game.gametype;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.game.model.CardPlayerResult;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.game.model.Decision;
import kz.nuris.cardgame.service.game.model.GameResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kz.nuris.cardgame.service.game.CardGameConst.*;

public abstract class CardGame2PlayerAbstract implements CardGame2Player {

    //TODO test
    @Override
    public List<CardPlayerResult> play(List<CardPlayerTurn> players) {
        if (players.size() != PLAYER_COUNT) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.CARD_GAME_ROLE);
        }
        var firstPlayer = players.get(FIRST_PLAYER);
        var secondPlayer = players.get(SECOND_PLAYER);

        var analyseDecisionResult = analyseDecisions(firstPlayer, secondPlayer);

        GameResult fpResult = analyseDecisionResult.get(FIRST_PLAYER);
        GameResult spResult = analyseDecisionResult.get(SECOND_PLAYER);

        var result = new ArrayList<CardPlayerResult>();
        result.add(new CardPlayerResult(firstPlayer.getPlayer(), fpResult));
        result.add(new CardPlayerResult(secondPlayer.getPlayer(), spResult));

        return result;
    }

    //TODO test
    private Map<Integer, GameResult> analyseDecisions(CardPlayerTurn firstPlayer,
                                                      CardPlayerTurn secondPlayer) {

        var result = new HashMap<Integer, GameResult>();

        GameResult fpResult;
        GameResult spResult;

        if (firstPlayer.getDecision() == Decision.FOLD
                && secondPlayer.getDecision() == Decision.FOLD) {
            fpResult = GameResult.FOLD_DRAW;
            spResult = GameResult.FOLD_DRAW;
        } else if (firstPlayer.getDecision() == Decision.FOLD
                && secondPlayer.getDecision() == Decision.PLAY) {
            fpResult = GameResult.FOLD_LOSE;
            spResult = GameResult.FOLD_WIN;
        } else if (firstPlayer.getDecision() == Decision.PLAY
                && secondPlayer.getDecision() == Decision.FOLD) {
            fpResult = GameResult.FOLD_WIN;
            spResult = GameResult.FOLD_LOSE;
        } else {

            var winnerAndLoser = defineWinnerAndLoser(firstPlayer, secondPlayer);

            fpResult = winnerAndLoser.get(FIRST_PLAYER);
            spResult = winnerAndLoser.get(SECOND_PLAYER);
        }

        result.put(FIRST_PLAYER, fpResult);
        result.put(SECOND_PLAYER, spResult);

        return result;
    }


}
