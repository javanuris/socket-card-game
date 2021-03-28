package kz.nuris.cardgame.service.player.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
public class Player {
    private Long id;
    private String name;
    private BigDecimal tokens;

    public Player(){

    }

    public Player(String name) {
        this.name = name;
    }

    public Player(Long id, String name, BigDecimal tokens) {
        this.id = id;
        this.name = name;
        this.tokens = tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
