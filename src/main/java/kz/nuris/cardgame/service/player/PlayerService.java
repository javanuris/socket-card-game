package kz.nuris.cardgame.service.player;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.player.model.Player;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerService {

    public Player getById(Long id) {
//        var player = new Player();
//        return players.indexOf(player);
        return null;
    }

    public Player minusToken(Long payerId, BigDecimal delta) {
        var player = getById(payerId);
        player.setTokens(player.getTokens().subtract(delta));
        updateOrBlow(player);
        return player;
    }

    public Player plusToken(Long payerId, BigDecimal delta) {
        var player = getById(payerId);
        player.setTokens(player.getTokens().add(delta));
        updateOrBlow(player);
        return player;
    }

    public void saveOrBlow(Player player) {

        if (player == null || player.getTokens() == null || player.getId() == null || player.getName() == null) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.VALIDATION_ERROR);
        }

        players.add(player);
    }

    public void updateOrBlow(Player player){

    }

    //TODO db
    private List<Player> players = new ArrayList<>();
}
