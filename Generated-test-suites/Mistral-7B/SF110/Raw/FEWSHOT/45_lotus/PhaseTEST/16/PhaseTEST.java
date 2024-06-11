package lotus.core.phases;

import static org.junit.Assert.*;

import lotus.core.Game;
import lotus.core.*;
import lotus.core.effect.ChangePhase;

public class PhaseTest {

    private static final Game GAME = new Game();

    @Test
    public void testNextPhase_UntapPhase() {
        Phase untapPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(UpkeepPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DrawPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase drawPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(Main1Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase main1Phase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatEndPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase combatEndPhase = new CombatEndPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(Main2Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Phase main2Phase = new Main2Phase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CleanupPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Phase cleanupPhase = new CleanupPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Phase playerChangePhase = new PlayerChangePhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(UntapPhase.class, nextPhase.getClass());
    }

    @Test
    public void testChangePhase_Null() {
        assertNull(Phase.nextPhase(null));
    }

    @Test
    public void testChangePhase_InvalidPhase() {
        Phase unknownPhase = new Object() {};
        assertNull(Phase.nextPhase(unknownPhase));
    }
}