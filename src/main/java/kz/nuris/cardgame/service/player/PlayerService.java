package kz.nuris.cardgame.service.player;

import kz.nuris.cardgame.execptions.CardGameException;
import kz.nuris.cardgame.service.player.model.Player;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PlayerService {

    //TODO add pessimistic lock db
    public Player getById(Long id) {
        return players.get(id);
    }

    public synchronized Player minusToken(Long payerId, BigDecimal delta) {
        var player = getById(payerId);
        player.setTokens(player.getTokens().subtract(delta));
        updateOrBlow(player);
        return player;
    }

    public synchronized Player plusToken(Long payerId, BigDecimal delta) {
        var player = getById(payerId);
        player.setTokens(player.getTokens().add(delta));
        updateOrBlow(player);
        return player;
    }

    public void saveOrBlow(Player player) {
        //data checking ORM or DB side
        if (player == null || player.getTokens() == null || player.getId() == null || player.getName() == null) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.VALIDATION_ERROR);
        }
        //data checking ORM or DB side
        if (players.get(player.getId()) != null) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.ALREADY_EXIST);
        }

        players.put(player.getId(), player);
    }

    public void updateOrBlow(Player player) {
        //data checking ORM or DB side
        if (player == null || player.getTokens() == null || player.getId() == null || player.getName() == null) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.VALIDATION_ERROR);
        }
        //data checking ORM or DB side
        if (players.get(player.getId()) == null) {
            throw new CardGameException(CardGameException.CardGameExceptionCode.NOT_FOUND);
        }

        players.put(player.getId(), player);
    }

    //TODO db
    private Map<Long, Player> players = new HashMap<>();
}
