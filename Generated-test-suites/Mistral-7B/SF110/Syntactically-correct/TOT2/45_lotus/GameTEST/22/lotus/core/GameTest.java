import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import lotus.core.card.Card;
import lotus.core.card.CardCollection;
import lotus.core.interfaces.UserInterface;
import lotus.core.phases.UntapPhase;
import lotus.core.Player;
import lotus.core.stack.EmptyStack;
import lotus.core.stack.Stack;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doCallRealMethod;

public class GameTest {

    private Game game;

    @Before
    public void setUp() {
        Game game = new Game();
        this.game = game;
    }

    // Test methods getPlayingPlayer, getNonPlayingPlayer, getOtherPlayer

    @Test
    public void testGetPlayingPlayer() {/* Test code */}

    @Test
    public void testGetNonPlayingPlayer() {/* Test code */}

    @Test
    public void testGetOtherPlayer() {/* Test code */}

    // Test method init

    @Test
    public void testInit() {/* Test code */}

    // Test method givePriorityToCurrentPlayer

    @Test
    public void testGivePriorityToCurrentPlayer() {/* Test code */}

    // Test method empty stack
}