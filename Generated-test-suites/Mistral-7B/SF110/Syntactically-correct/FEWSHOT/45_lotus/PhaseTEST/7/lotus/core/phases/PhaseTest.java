package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testNextPhase_UntapPhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(UpkeepPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Game game = new Game();
        Phase upkeepPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DrawPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Game game = new Game();
        Phase drawPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(Main1Phase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Game game = new Game();
        Phase main1Phase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Game game = new Game();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Game game = new Game();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Game game = new Game();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Game game = new Game();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatEndPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Game game = new Game();
        Phase combatEndPhase = new CombatEndPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(Main2Phase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Game game = new Game();
        Phase main2Phase = new Main2Phase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Game game = new Game();
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CleanupPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Game game = new Game();
        Phase cleanupPhase = new CleanupPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Game game = new Game();
        Phase playerChangePhase = new PlayerChangePhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(UntapPhase.class, nextPhase.getClass());
        assertNotNull(Game.currentPhase);
    }

    @Test(expected = RuntimeException.class)
    public void testNextPhase_InvalidPhase() {
        Game game = new Game();
        Phase invalidPhase = new Object();
        Phase nextPhase = Phase.nextPhase();

        assertNull(nextPhase);
        Phase.nextPhase(); // Trigger the exception
    }

    @Test
    public void testChangePhase() {
        Game game = new Game();
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();

        assertEquals(UntapPhase.class, Game.currentPhase.getClass());
        Phase.changePhase(upkeepPhase);
        assertEquals(UpkeepPhase.class, Game.currentPhase.getClass());
        Phase.changePhase(drawPhase);
        assertEquals(DrawPhase.class, Game.currentPhase.getClass());

        // Test that changing phase to the same phase has no effect
        Phase.changePhase(drawPhase);
        assertEquals(DrawPhase.class, Game.currentPhase.getClass());
    }
}