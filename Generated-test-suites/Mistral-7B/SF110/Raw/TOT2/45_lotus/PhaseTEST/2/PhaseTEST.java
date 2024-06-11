package lotus.core.phases;

import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {
    // Test for nextPhase() with null Game object
    @Test(expected = NullPointerException.class)
    public void testNextPhaseWithNullGame() {
        Game mockGame = Mockito.mock(Game.class);
        Mockito.when(mockGame.currentPhase).thenReturn(null);
        Phase phase = new Phase() {
            @Override
            public void doPhase() {}
        };
        Phase.nextPhase();
    }

    // Test for nextPhase() with unexpected phase
    @Test(expected = IllegalStateException.class)
    public void testNextPhaseWithUnexpectedPhase() {
        Game mockGame = Mockito.mock(Game.class);
        Mockito.when(mockGame.currentPhase).thenReturn(new Object()); // Replace with an unexpected phase type
        Phase phase = new Phase() {
            @Override
            public void doPhase() {}
        };
        Phase.nextPhase();
    }

    // Test for changePhase() with null Phase object
    @Test(expected = NullPointerException.class)
    public void testChangePhaseWithNullPhase() {
        Game mockGame = Mockito.mock(Game.class);
        Mockito.when(mockGame.currentPhase).thenReturn(new UntapPhase());
        Phase phase = null;
        Phase.changePhase(phase);
    }

    // Test for infinite loop when transitioning between phases
    @Test(expected = StackOverflowError.class)
    public void testInfinitePhaseTransition() {
        Game mockGame = Mockito.mock(Game.class);
        Mockito.when(mockGame.currentPhase).thenReturn(new UntapPhase());
        Phase untapPhase = new UntapPhase() {
            @Override
            public void doPhase() {
                PhaseTest.this.changePhase(new UntapPhase());
            }
        };
        Phase.changePhase(untapPhase);
    }
}