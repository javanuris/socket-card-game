package kz.nuris.cardgame.service.game.gametype;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.game.gametype.role.CardGame2PlayerRole;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.game.model.GameResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier(GameType.DOUBLE_CARD_GAME)
public class DoubleCardGame extends CardGame2PlayerAbstract {

    private final CardGame2PlayerRole role;

    //template method
    //TODO test
    @Override
    public Map<Integer, GameResult> defineWinnerAndLoser(CardPlayerTurn firstPlayer,
                                                         CardPlayerTurn secondPlayer) {
        var result = new HashMap<Integer, GameResult>();

        var fpHand = firstPlayer.getHand();
        var spHand = secondPlayer.getHand();
        if (fpHand.size() != spHand.size()) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.VALIDATION_ERROR);
        }

        //Sort: the cards with the highest rank from each hand are compared.
        fpHand = fpHand.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        spHand = spHand.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        for (int i = 0; i < fpHand.size(); i++) {
            var fpCard = fpHand.get(i);
            var spCard = spHand.get(i);

            result = role.check(fpCard, spCard);

            //already defined who winner and loser
            if (fpCard.compareTo(spCard) != 0) {
                break;
            }

        }

        return result;
    }

    @Override
    public int cardCountInHand() {
        return 2;
    }
}
