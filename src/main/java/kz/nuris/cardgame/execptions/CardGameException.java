package kz.nuris.cardgame.execptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardGameException extends RuntimeException {
    private String message;
    private CardGameExceptionCode code;

    public CardGameException(CardGameExceptionCode code) {
        this.code = code;
    }

    public CardGameException(String message, CardGameExceptionCode code) {
        super("message: " + message + " code:" + code);
        this.message = message;
        this.code = code;
    }

    public enum CardGameExceptionCode {
        SYSTEM_EXCEPTION,
        DECK_IS_EMPTY,
        VALIDATION_ERROR,
        CARD_GAME_ROLE;
    }

}
