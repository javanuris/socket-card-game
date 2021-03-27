package kz.nuris.cardgame.service.session;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.deck.DeckManager;
import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.game.CardGame;
import kz.nuris.cardgame.service.game.model.CardPlayerHand;
import kz.nuris.cardgame.service.game.model.CardPlayerResult;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;
import kz.nuris.cardgame.service.player.PlayerService;
import kz.nuris.cardgame.service.player.model.Player;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CardGameSession {
    private final PlayerService playerService;
    private final DeckManager deckManager;
    private final CardGame cardGame;
    private final List<Player> players;

    //in session
    private  Map<Player, CardPlayerTurn> turnSession;
    //in session
    private  Map<Player, CardPlayerResult> resultSession;
    //in session
    private  Map<Player, CardPlayerHand> handSession;

    @PostConstruct
    public void init(){
        turnSession = new HashMap<>();
        resultSession = new HashMap<>();
        handSession = new HashMap<>();
        deckManager.shuffle();
    }
    public boolean emptyDeck() {
        return deckManager.count() == 0;
    }

    public synchronized List<CardPlayerHand> dealCards() {
        var playerHand = new ArrayList<CardPlayerHand>();
        players.forEach(player -> {
            if (handSession.get(player) == null) {
                List<Card> hand = new ArrayList<>();
                for (int i = 0; i < cardGame.cardCountInHand(); i++) {
                    hand.add(deckManager.takeCardOrBlow());
                }
                handSession.put(player, new CardPlayerHand(player, hand));
            } else {
                throw new CardGameException(CardGameException.CardGameExceptionCode.SESSION_ERROR);
            }
        });
        return playerHand;
    }

    //todo decision
    //todo play
    //todo return result

}
