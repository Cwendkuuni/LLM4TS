import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.phases.*;

public class PhaseTest {
    private Game game;
    private Phase untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhase2;

    @Before
    public void setUp() {
        game = new Game();
        untapPhase = new UntapPhase();
        upkeepPhase = new UpkeepPhase();
        drawPhase = new DrawPhase();
        main1Phase = new Main1Phase();
        combatBeginningPhase = new CombatBeginningPhase();
        declareAttackersPhase = new DeclareAttackersPhase();
        declareBlockersPhase = new DeclareBlockersPhase();
        combatDamagePhase = new CombatDamagePhase();
        combatEndPhase = new CombatEndPhase();
        main2Phase = new Main2Phase();
        endOfTurnPhase = new EndOfTurnPhase();
        cleanupPhase = new CleanupPhase();
        playerChangePhase = new PlayerChangePhase();
        untapPhase2 = new UntapPhase();
    }

    @Test
    public void testNextPhase() {
        // Given: Initial game state
        Game.currentPhase = untapPhase;
        assertEquals(Game.Phase.Untap, Game.currentPhase.getClass());

        // When: Call nextPhase method
        PhaseTest.nextPhase();

        // Then: First player's turn ends and the next phase is reached
        assertEquals(Game.Phase.Upkeep, Game.currentPhase.getClass());

        // Repeat the test for other phases
        // ...
    }

    // Test other methods (doPhase and changePhase) and edge cases here
}