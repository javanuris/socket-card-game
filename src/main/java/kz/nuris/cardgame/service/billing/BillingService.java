package kz.nuris.cardgame.service.billing;

import kz.nuris.cardgame.service.game.model.GameResult;
import kz.nuris.cardgame.service.player.model.Player;

public interface BillingService {

    void calculate(Player player, GameResult result);

}
