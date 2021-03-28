package kz.nuris.cardgame.configs;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.billing.BillingService;
import kz.nuris.cardgame.service.deck.DeckManager;
import kz.nuris.cardgame.service.game.CardGame;
import kz.nuris.cardgame.service.game.gametype.GameType;
import kz.nuris.cardgame.service.player.model.Player;
import kz.nuris.cardgame.service.session.CardGameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
public class BeanConfig {

    @Autowired
    private DeckManager deckManager;

    @Autowired
    @Qualifier(GameType.SINGLE_CARD_GAME)
    private CardGame cardGameSingle;

    @Autowired
    @Qualifier(GameType.DOUBLE_CARD_GAME)
    private CardGame cardGameDouble;

    @Autowired
    @Qualifier("SingleCardGameBillingService")
    private BillingService singleCardGameBillingService;

    @Autowired
    @Qualifier("DoubleCardGameBillingService")
    private BillingService doubleCardGameBillingService;

    //fabric method
    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public CardGameSession factoryMethod(String arg, List<Player> players) {
        if (arg.equals("single")) {
            return new CardGameSession(deckManager, cardGameSingle, singleCardGameBillingService, players);
        } else if (arg.equals("double")) {
            return new CardGameSession(deckManager, cardGameDouble, doubleCardGameBillingService, players);
        } else {
            throw new CardGameException(CardGameException.CardGameExceptionCode.SYSTEM_EXCEPTION);
        }
    }
}
