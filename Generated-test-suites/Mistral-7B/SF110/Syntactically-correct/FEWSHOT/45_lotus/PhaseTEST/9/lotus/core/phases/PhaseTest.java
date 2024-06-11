package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private Game game = new Game();

    @Test
    public void testNextPhase_UntapPhase() {
        Phase untapPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase(untapPhase);
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase(upkeepPhase);
        assertEquals(DrawPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase drawPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase(drawPhase);
        assertEquals(Main1Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase main1Phase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase(main1Phase);
        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase nextPhase = Phase.nextPhase(combatBeginningPhase);
        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase nextPhase = Phase.nextPhase(declareAttackersPhase);
        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase nextPhase = Phase.nextPhase(declareBlockersPhase);
        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase nextPhase = Phase.nextPhase(combatDamagePhase);
        assertEquals(CombatEndPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase combatEndPhase = new CombatEndPhase();
        Phase nextPhase = Phase.nextPhase(combatEndPhase);
        assertEquals(Main2Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Phase main2Phase = new Main2Phase();
        Phase nextPhase = Phase.nextPhase(main2Phase);
        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Phase nextPhase = Phase.nextPhase(endOfTurnPhase);
        assertEquals(CleanupPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Phase cleanupPhase = new CleanupPhase();
        Phase nextPhase = Phase.nextPhase(cleanupPhase);
        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Phase playerChangePhase = new PlayerChangePhase();
        Phase nextPhase = Phase.nextPhase(playerChangePhase);
        assertEquals(UntapPhase.class, nextPhase.getClass());
    }

    @Test
    public void testChangePhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase gamePhase = Game.currentPhase;

        Phase.changePhase(untapPhase);
        assertEquals(upkeepPhase, Game.currentPhase);

        Phase.changePhase(upkeepPhase);
        assertEquals(drawPhase, Game.currentPhase);
    }
}