package kz.nuris.cardgame.service.game.gametype.role;

import kz.nuris.cardgame.service.deck.model.Card;
import kz.nuris.cardgame.service.game.model.GameResult;

import java.util.HashMap;

public interface CardGame2PlayerRole {
    HashMap<Integer, GameResult> check(Card fpCard, Card spCard);
}
