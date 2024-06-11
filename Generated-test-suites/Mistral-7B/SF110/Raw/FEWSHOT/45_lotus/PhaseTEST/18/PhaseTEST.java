package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private static Game game = new Game();

    @Test
    public void testNextPhase_UntapPhase() {
        Phase untapPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, untapPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DrawPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, upkeepPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase drawPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main1Phase.class, nextPhase.getClass());
        assertSame(game.currentPhase, drawPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase main1Phase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, main1Phase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, combatBeginningPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, declareAttackersPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatDamagePhase.class, nextPhase.class);
        assertSame(game.currentPhase, declareBlockersPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatEndPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, combatDamagePhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase combatEndPhase = new CombatEndPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main2Phase.class, nextPhase.getClass());
        assertSame(game.currentPhase, combatEndPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Phase main2Phase = new Main2Phase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, main2Phase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CleanupPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, endOfTurnPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Phase cleanupPhase = new CleanupPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, cleanupPhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Phase playerChangePhase = new PlayerChangePhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(UntapPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, playerChangePhase);
        assertSame(game.currentPhase, nextPhase);
    }

    @Test
    public void testNextPhase_UntapPhase_Twice() {
        Phase untapPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, untapPhase);
        assertSame(game.currentPhase, nextPhase);

        nextPhase = Phase.nextPhase();
        assertEquals(DrawPhase.class, nextPhase.getClass());
        assertSame(game.currentPhase, untapPhase);
        assertSame(game.currentPhase, nextPhase);
    }
}