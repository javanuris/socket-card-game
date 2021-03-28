package kz.nuris.cardgame.service.session;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.billing.BillingService;
import kz.nuris.cardgame.service.deck.DeckManager;
import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.game.CardGame;
import kz.nuris.cardgame.service.game.model.CardPlayerHand;
import kz.nuris.cardgame.service.game.model.CardPlayerResult;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.player.model.Player;
import kz.nuris.cardgame.service.session.model.CardPlayerDecision;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CardGameSession {

    private final DeckManager deckManager;
    private final CardGame cardGame;
    private final BillingService billingService;
    private final List<Player> players;

    private Map<Player, CardPlayerHand> playersHandState;

    @PostConstruct
    public void init() {
        playersHandState = new HashMap<>();
        deckManager.shuffle();
    }

    public boolean isDeckEmpty() {
        return deckManager.count() == 0;
    }

    public synchronized List<CardPlayerHand> dealCards() {
        if (isDeckEmpty()) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.SESSION_ERROR);
        }
        var playerHand = new ArrayList<CardPlayerHand>();
        players.forEach(player -> {
            if (playersHandState.get(player) != null) {
                throw new CardGameException(CardGameException.CardGameExceptionCode.SESSION_ERROR);
            } else {
                List<Card> hand = new ArrayList<>();
                for (int i = 0; i < cardGame.cardCountInHand(); i++) {
                    hand.add(deckManager.takeCardOrBlow());
                }
                var temp = new CardPlayerHand(player, hand);
                playerHand.add(temp);
                playersHandState.put(player, temp);
            }
        });
        return playerHand;
    }

    public synchronized List<CardPlayerResult> play(List<CardPlayerDecision> turns) {

        var temp = turns.stream().map(x -> {
            if (playersHandState.get(x.getPlayer()) == null) {
                throw new CardGameException(CardGameException.CardGameExceptionCode.SESSION_ERROR);
            }

            var turn = new CardPlayerTurn();
            turn.setPlayer(x.getPlayer());
            turn.setDecision(x.getDecision());
            turn.setHand(playersHandState.get(x.getPlayer()).getHand());

            //clean hand state for new deal
            playersHandState.remove(x.getPlayer());

            return turn;
        }).collect(Collectors.toList());

        var result = cardGame.play(temp);

        //strategy pattern
        result.forEach(x -> billingService.calculate(x.getPlayer(), x.getGameResult()));

        return result;
    }
}
