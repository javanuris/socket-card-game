package kz.nuris.cardgame.service.game.model;

import kz.nuris.cardgame.service.player.model.Player;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CardPlayerResult {
    private Player player;
    private GameResult gameResult;
}
