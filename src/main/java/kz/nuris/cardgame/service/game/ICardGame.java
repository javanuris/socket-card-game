package kz.nuris.cardgame.service.game;

import kz.nuris.cardgame.service.game.model.CardPlayerResult;
import kz.nuris.cardgame.service.game.model.CardPlayerTurn;

import java.util.List;

public interface ICardGame {
    List<CardPlayerResult> play(List<CardPlayerTurn> players);
}
