package lotus.core.phases.test;

import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.effect.*;
import lotus.core.phases.*;

public class PhaseTest {
    @Test
    public void testNextPhase() {
        Game.currentPhase = new UntapPhase();
        Phase.nextPhase();
        assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());

        Game.currentPhase = new UpkeepPhase();
        Phase.nextPhase();
        assertEquals(DrawPhase.class, Game.currentPhase.getClass());

        // ... continue testing all phases
    }

    @Test(expected = IllegalStateException.class)
    public void testNextPhaseWhenGameHasNoCurrentPhase() {
        Game.currentPhase = null;
        try {
            Phase.nextPhase();
        } catch (IllegalStateException e) {
            // Test passed
        }
    }

    @Test
    public void testNextPhaseWhenCurrentPhaseIsLastPhase() {
        Game.currentPhase = new CleanupPhase();
        Phase.nextPhase();
        assertEquals(PlayerChangePhase.class, Game.currentPhase.getClass());
    }

    @Test
    public void testNextPhaseWhenCurrentPhaseIsNotUntapPhase() {
        Phase.nextPhase();
        Phase p = new UntapPhase();
        assertEquals("UntapPhase", Game.currentPhase.getClass().getSimpleName());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextPhaseWhenGameHasNoCurrentPhase() {
        Game.currentPhase = null;
        try {
            Phase.nextPhase();
        } catch (IllegalStateException e) {
            // Test passed
        }
    }
}