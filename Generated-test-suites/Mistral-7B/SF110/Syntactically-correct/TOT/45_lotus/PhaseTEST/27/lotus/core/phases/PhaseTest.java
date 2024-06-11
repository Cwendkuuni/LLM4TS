import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.Phase;
import lotus.core.phases.UntapPhase;
import lotus.core.phases.UpkeepPhase;
import lotus.core.phases.DrawPhase;
import lotus.core.phases.Main1Phase;
import lotus.core.phases.CombatBeginningPhase;
import lotus.core.phases.DeclareAttackersPhase;
import lotus.core.phases.DeclareBlockersPhase;
import lotus.core.phases.CombatDamagePhase;
import lotus.core.phases.CombatEndPhase;
import lotus.core.phases.Main2Phase;
import lotus.core.phases.EndOfTurnPhase;
import lotus.core.phases.CleanupPhase;
import lotus.core.phases.PlayerChangePhase;

public class PhaseTest {
    private Game game;
    private Phase currentPhase;

    @Before
    public void setup() {
        game = new Game();
        currentPhase = new UntapPhase();
        Phase.nextPhase();
    }

    @Test
    public void testNextPhase() {
        // Typical use case
        assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());

        // Edge case: Game starts with an untap phase
        Game newGame = new Game();
        newGame.currentPhase = new UntapPhase();
        Phase.nextPhase(newGame);
        assertEquals(UpkeepPhase.class, newGame.currentPhase.getClass());

        // Error scenario: Invalid phase type
        Phase invalidPhase = new Object() {};
        try {
            Phase.nextPhase(game, invalidPhase);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Error scenario: Call nextPhase() after cleanup phase
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        Phase.nextPhase();
        try {
            Phase.nextPhase();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}