package lotus.core.phases;

import static org.junit.Assert.*;

import org.junit.Test;

import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import lotus.core.phases.CombatBeginningPhase;
import lotus.core.phases.CombatDamagePhase;
import lotus.core.phases.CombatEndPhase;
import lotus.core.phases.DeclareAttackersPhase;
import lotus.core.phases.DeclareBlockersPhase;
import lotus.core.phases.DrawPhase;
import lotus.core.phases.EndOfTurnPhase;
import lotus.core.phases.Main1Phase;
import lotus.core.phases.Main2Phase;
import lotus.core.phases.PlayerChangePhase;
import lotus.core.phases.UntapPhase;

public class PhaseTest {

    @Test
    public void testNextPhase() {
        Game game = new Game();
        Phase[] phases = {new UntapPhase(), new UpkeepPhase(), new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(), new DeclareAttackersPhase(), new DeclareBlockersPhase(), new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(), new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase(), new UntapPhase()};
        int initialPhaseIndex = 0;

        for (int i = 0; i < 12; i++) {
            Phase currentPhase = game.getCurrentPhase();
            assertEquals(phases[initialPhaseIndex++], currentPhase);
            game.nextPhase();
        }

        // Test wrapping back to the first phase
        for (int i = 0; i < 7; i++) {
            Phase currentPhase = game.getCurrentPhase();
            assertEquals(phases[initialPhaseIndex++], currentPhase);
            game.nextPhase();
        }
        assertEquals(phases[0], game.getCurrentPhase());
    }

    @Test
    public void testChangePhase() {
        Game game = new Game();
        Phase currentPhase;
        Phase newPhase = new Main1Phase();

        // Test changing to a valid phase
        Phase oldPhase = game.getCurrentPhase();
        Phase expectedNewPhase = newPhase;
        game.changePhase(newPhase);
        currentPhase = game.getCurrentPhase();
        assertEquals(expectedNewPhase, currentPhase);

        // Test changing to the same phase (no effect)
        game.changePhase(oldPhase);
        currentPhase = game.getCurrentPhase();
        assertEquals(expectedNewPhase, currentPhase);

        // Test changing to an invalid phase
        Phase invalidPhase = new Object();
        assertThrows(IllegalArgumentException.class, () -> game.changePhase(invalidPhase));

        // Test changing to a previously visited phase (wraps back to the first phase)
        for (int i = 0; i < 7; i++) {
            game.nextPhase();
        }
        game.changePhase(newPhase);
        currentPhase = game.getCurrentPhase();
        assertEquals(newPhase, currentPhase);
        game.nextPhase();
        assertEquals(phases[0], game.getCurrentPhase());
    }
}