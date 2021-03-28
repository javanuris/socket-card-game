package kz.nuris.cardgame.chat.model;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RegisterPlayerMessage extends Message {
    private String gameType;
    private BigDecimal tokens;
}
