package kz.nuris.cardgame.chat.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Message {
    private MessageType type;
    private String sender;
    private String time;
}
