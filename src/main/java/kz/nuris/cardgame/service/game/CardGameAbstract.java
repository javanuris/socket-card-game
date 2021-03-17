package kz.nuris.cardgame.service.game;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.game.model.CardPlayerResult;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.game.model.Decision;
import kz.nuris.cardgame.service.game.model.GameResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CardGameAbstract implements ICardGame {

    private final int FIRST_PLAYER = 0;
    private final int SECOND_PLAYER = 1;

    //TODO test
    @Override
    public List<CardPlayerResult> play(List<CardPlayerTurn> players) {
        if (players.size() != 2) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.CARD_GAME_ROLE);
        }
        var firstPlayer = players.get(FIRST_PLAYER);
        var secondPlayer = players.get(SECOND_PLAYER);

        var analyseDecisionResult = analysePlayersDecision(firstPlayer, secondPlayer);

        GameResult firstPlayerResult = analyseDecisionResult.get(FIRST_PLAYER);
        GameResult secondPlayerResult = analyseDecisionResult.get(SECOND_PLAYER);

        var result = new ArrayList<CardPlayerResult>();
        result.add(new CardPlayerResult(firstPlayer.getPlayer(), firstPlayerResult));
        result.add(new CardPlayerResult(secondPlayer.getPlayer(), secondPlayerResult));

        return result;
    }

    //TODO test
    private Map<Integer, GameResult> analysePlayersDecision(CardPlayerTurn firstPlayer,
                                                           CardPlayerTurn secondPlayer) {

        var result = new HashMap<Integer, GameResult>();

        GameResult firstPlayerResult;
        GameResult secondPlayerResult;

        if (firstPlayer.getDecision() == Decision.FOLD
                && secondPlayer.getDecision() == Decision.FOLD) {
            firstPlayerResult = GameResult.DRAW;
            secondPlayerResult = GameResult.DRAW;
        } else if (firstPlayer.getDecision() == Decision.FOLD
                && secondPlayer.getDecision() == Decision.PLAY) {
            firstPlayerResult = GameResult.BLIND_LOSE;
            secondPlayerResult = GameResult.BLIND_WIN;
        } else if (firstPlayer.getDecision() == Decision.PLAY
                && secondPlayer.getDecision() == Decision.FOLD) {
            firstPlayerResult = GameResult.BLIND_WIN;
            secondPlayerResult = GameResult.BLIND_LOSE;
        } else {
            var checkHandsResult = checkHands(firstPlayer, secondPlayer);
            firstPlayerResult = checkHandsResult.get(FIRST_PLAYER);
            secondPlayerResult = checkHandsResult.get(SECOND_PLAYER);
        }

        result.put(FIRST_PLAYER, firstPlayerResult);
        result.put(SECOND_PLAYER, secondPlayerResult);

        return result;
    }

    public Map<Integer, GameResult> checkHands(CardPlayerTurn firstPlayer,
                                       CardPlayerTurn secondPlayer) {
        return null;
    }

}
