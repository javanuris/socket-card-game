package kz.nuris.cardgame.service.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DeckManagerTest {

    private DeckManager deckManager;

    @InjectMocks
    private DeckService deckService;

    @BeforeEach
    public void setUp() {
        deckManager = new DeckManager(deckService.getDeck());
    }

    //TODO
    @Test
    public void shuffleTest() {
        var result = deckManager.getDeck();
        deckManager.shuffle();
        //assertEquals(expected.getCards(), result.getCards());


    }
}
