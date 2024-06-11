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
    @Test
    public void testNextPhase() {
        // Initialize game with UntapPhase
        Game game = new Game();
        Phase currentPhase = game.getPhase();
        assertEquals(currentPhase.getClass(), UntapPhase.class);

        // Call nextPhase() and check if phase is changed to UpkeepPhase
        PhaseTestHelper.nextPhase(game);
        currentPhase = game.getPhase();
        assertEquals(currentPhase.getClass(), UpkeepPhase.class);

        // Test all phases and check if phase is changed correctly
        for (Phase phase : new Phase[] {
            new DrawPhase(), new Main1Phase(), new CombatBeginningPhase(),
            new DeclareAttackersPhase(), new DeclareBlockersPhase(),
            new CombatDamagePhase(), new CombatEndPhase(), new Main2Phase(),
            new EndOfTurnPhase(), new CleanupPhase(), new PlayerChangePhase(),
            new UntapPhase() }) {
            currentPhase = game.getPhase();
            assertEquals(currentPhase.getClass(), game.getCurrentPhase().getClass());
            PhaseTestHelper.nextPhase(game);
            currentPhase = game.getPhase();
            assertEquals(currentPhase.getClass(), phase.getClass());
        }

        // Test an invalid phase and check if an exception is thrown
        Phase invalidPhase = new Object() {};
        try {
            PhaseTestHelper.nextPhase(game, invalidPhase);
            fail("Expected an exception to be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}

// Helper class to simplify testing
class PhaseTestHelper {
    static void nextPhase(Game game) {
        game.nextPhase();
    }

    static void nextPhase(Game game, Phase phase) {
        Phase currentPhase = game.getPhase();
        game.changePhase(phase);
    }
}