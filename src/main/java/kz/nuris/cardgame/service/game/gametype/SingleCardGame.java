package kz.nuris.cardgame.service.game.gametype;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.game.gametype.role.CardGame2PlayerRole;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.game.model.GameResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class SingleCardGame extends CardGame2PlayerAbstract {

    private final CardGame2PlayerRole role;

    //TODO test
    @Override
    public Map<Integer, GameResult> defineWinnerAndLoser(CardPlayerTurn firstPlayer,
                                                         CardPlayerTurn secondPlayer) {

        var fpHand = firstPlayer.getHand().stream()
                .findFirst().orElseThrow(
                        () -> new CardGameException(
                                CardGameException.CardGameExceptionCode.VALIDATION_ERROR));

        var spHand = secondPlayer.getHand().stream()
                .findFirst().orElseThrow(
                        () -> new CardGameException(
                                CardGameException.CardGameExceptionCode.VALIDATION_ERROR));


        return role.check(fpHand, spHand);
    }

    @Override
    public int cardCountInHand() {
        return 1;
    }
}
