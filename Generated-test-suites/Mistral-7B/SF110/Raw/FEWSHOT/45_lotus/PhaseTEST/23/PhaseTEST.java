package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private Game game = new Game();

    @Test
    public void testNextPhase_UntapPhase() {
        Phase untapPhase = new UntapPhase();
        game.setCurrentPhase(untapPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(UpkeepPhase.class, nextPhase.getClass());
        assertEquals(UpkeepPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        game.setCurrentPhase(upkeepPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DrawPhase.class, nextPhase.getClass());
        assertEquals(DrawPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase drawPhase = new DrawPhase();
        game.setCurrentPhase(drawPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(Main1Phase.class, nextPhase.getClass());
        assertEquals(Main1Phase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase main1Phase = new Main1Phase();
        game.setCurrentPhase(main1Phase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
        assertEquals(CombatBeginningPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase combatBeginningPhase = new CombatBeginningPhase();
        game.setCurrentPhase(combatBeginningPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
        assertEquals(DeclareAttackersPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        game.setCurrentPhase(declareAttackersPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
        assertEquals(DeclareBlockersPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        game.setCurrentPhase(declareBlockersPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
        assertEquals(CombatDamagePhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase combatDamagePhase = new CombatDamagePhase();
        game.setCurrentPhase(combatDamagePhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatEndPhase.class, nextPhase.getClass());
        assertEquals(CombatEndPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase combatEndPhase = new CombatEndPhase();
        game.setCurrentPhase(combatEndPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(Main2Phase.class, nextPhase.getClass());
        assertEquals(Main2Phase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Phase main2Phase = new Main2Phase();
        game.setCurrentPhase(main2Phase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
        assertEquals(EndOfTurnPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Phase endOfTurnPhase = new EndOfTurnPhase();
        game.setCurrentPhase(endOfTurnPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CleanupPhase.class, nextPhase.getClass());
        assertEquals(CleanupPhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Phase cleanupPhase = new CleanupPhase();
        game.setCurrentPhase(cleanupPhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
        assertEquals(PlayerChangePhase.class, game.getCurrentPhase().getClass());
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Phase playerChangePhase = new PlayerChangePhase();
        game.setCurrentPhase(playerChangePhase);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(UntapPhase.class, nextPhase.getClass());
        assertEquals(UntapPhase.class, game.getCurrentPhase().getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextPhase_InvalidPhase() {
        Phase phase = new Object(); // An invalid phase
        game.setCurrentPhase(phase);
        Phase nextPhase = Phase.nextPhase();
    }
}