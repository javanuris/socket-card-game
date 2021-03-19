package kz.nuris.cardgame.service.player;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.player.model.Player;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PlayerService {

    //TODO add pessimistic lock db
    public Player getByIdOrBlow(Long id) {
        var player = players.get(id);
        //data checking ORM or DB side
        if (player == null) {
            throw new CardGameException("player id: " + id, CardGameException.CardGameExceptionCode.NOT_FOUND);
        }
        return player;
    }

    public synchronized Player minusToken(Long payerId, BigDecimal delta) {
        var player = getByIdOrBlow(payerId);
        player.setTokens(player.getTokens().subtract(delta));
        updateOrBlow(player);
        return player;
    }

    public synchronized Player plusToken(Long payerId, BigDecimal delta) {
        var player = getByIdOrBlow(payerId);
        player.setTokens(player.getTokens().add(delta));
        updateOrBlow(player);
        return player;
    }

    public void saveOrBlow(Player player) {
        if (player == null || player.getTokens() == null || player.getId() == null || player.getName() == null) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.VALIDATION_ERROR);
        }
        if (players.get(player.getId()) != null) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.ALREADY_EXIST);
        }

        players.put(player.getId(), player);
    }

    public void updateOrBlow(Player player) {
        if (player == null || player.getTokens() == null || player.getId() == null || player.getName() == null) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.VALIDATION_ERROR);
        }

        getByIdOrBlow(player.getId());

        players.put(player.getId(), player);
    }

    //TODO db
    private Map<Long, Player> players = new HashMap<>();
}
