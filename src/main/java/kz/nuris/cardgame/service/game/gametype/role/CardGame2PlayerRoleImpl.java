package kz.nuris.cardgame.service.game.gametype.role;

import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.game.model.GameResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static kz.nuris.cardgame.service.game.CardGameConst.*;

@Service
public class CardGame2PlayerRoleImpl implements CardGame2PlayerRole {

    //TODO test
    public HashMap<Integer, GameResult> check(Card fpCard, Card spCard){
        var result = new HashMap<Integer, GameResult>();

        GameResult fpResult;
        GameResult spResult;
        if (fpCard.compareTo(spCard) > 0) {
            fpResult = GameResult.WIN;
            spResult = GameResult.LOSE;
        } else if (fpCard.compareTo(spCard) < 0) {
            fpResult = GameResult.LOSE;
            spResult = GameResult.WIN;
        } else {
            fpResult = GameResult.DRAW;
            spResult = GameResult.DRAW;
        }

        result.put(FIRST_PLAYER, fpResult);
        result.put(SECOND_PLAYER, spResult);

        return result;
    }

}
