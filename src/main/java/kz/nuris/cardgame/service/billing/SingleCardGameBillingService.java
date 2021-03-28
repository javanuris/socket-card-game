package kz.nuris.cardgame.service.billing;

import kz.nuris.cardgame.service.game.model.GameResult;
import kz.nuris.cardgame.service.player.PlayerService;
import kz.nuris.cardgame.service.player.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Qualifier("SingleCardGameBillingService")
@RequiredArgsConstructor
public class SingleCardGameBillingService implements BillingService {

    private final PlayerService playerService;

    @Override
    public void calculate(Player player, GameResult result) {
        switch (result) {
            case WIN:
                playerService.plusToken(player.getId(), new BigDecimal("10"));
                break;
            case LOSE:
                playerService.minusToken(player.getId(), new BigDecimal("10"));
                break;
            case FOLD_WIN:
                playerService.plusToken(player.getId(), new BigDecimal("3"));
                break;
            case FOLD_LOSE:
                playerService.minusToken(player.getId(), new BigDecimal("3"));
                break;
        }


    }
}
