import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.phases.*;

public class PhaseTest
{
    @Test
    public void testDoPhase() {
        Phase phase = null; // Your implementation of a concrete Phase class
        assertNull("doPhase should return null for abstract Phase class", phase.doPhase());
    }

    @Test
    public void testNextPhase_UntapPhase_UpkeepPhase() {
        Phase untapPhase = new UntapPhase();
        Game game = new Game();
        game.currentPhase = untapPhase;

        Phase nextPhase = Phase.nextPhase();
        UpkeepPhase upkeepPhase = (UpkeepPhase) nextPhase;

        assertEquals("Next phase should be UpkeepPhase", upkeepPhase.getClass(), nextPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase_DrawPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Game game = new Game();
        game.currentPhase = upkeepPhase;

        Phase nextPhase = Phase.nextPhase();
        DrawPhase drawPhase = (DrawPhase) nextPhase;

        assertEquals("Next phase should be DrawPhase", drawPhase.getClass(), nextPhase.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextPhase_DrawPhase_Main1Phase() {
        Phase drawPhase = new DrawPhase();
        Game game = new Game();
        game.currentPhase = drawPhase;

        Phase nextPhase = Phase.nextPhase();
        Main1Phase main1Phase = (Main1Phase) nextPhase;

        assertNotNull("Next phase should not be null", nextPhase);

        // This next line should throw an IllegalStateException because the order of phases is not correct
        Phase nextNextPhase = Phase.nextPhase();

        fail("Next phase should not be followed by another phase");
    }

    @Test(expected = IllegalStateException.class)
    public void testNextPhase_Main1Phase_CombatBeginningPhase() {
        Phase main1Phase = new Main1Phase();
        Game game = new Game();
        game.currentPhase = main1Phase;

        Phase nextPhase = Phase.nextPhase();
        CombatBeginningPhase combatBeginningPhase = (CombatBeginningPhase) nextPhase;

        assertNotNull("Next phase should not be null", nextPhase);

        // This next line should throw an IllegalStateException because the order of phases is not correct
        Phase nextNextPhase = Phase.nextPhase();

        fail("Next phase should not be followed by another phase");
    }

    // Add test cases for other methods and edge cases here

    @Test
    public void testChangePhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase combatEndPhase = new CombatEndPhase();
        Phase main2Phase = new Main2Phase();
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Phase cleanupPhase = new CleanupPhase();
        Phase playerChangePhase = new PlayerChangePhase();

        game.currentPhase = untapPhase;
        Phase.changePhase(upkeepPhase);
        assertEquals("Current phase should be UpkeepPhase after changePhase", upkeepPhase, game.currentPhase);

        game.currentPhase = upkeepPhase;
        Phase.changePhase(drawPhase);
        assertEquals("Current phase should be DrawPhase after changePhase", drawPhase, game.currentPhase);

        // Add test cases for other edge cases and exceptions here
    }
}