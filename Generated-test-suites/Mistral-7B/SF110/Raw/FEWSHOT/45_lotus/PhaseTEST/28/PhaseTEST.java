package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    @Test
    public void testNextPhase_UntapPhase() {
        Phase untapPhase = new UntapPhase();
        Game game = new Game();
        game.setCurrentPhase(untapPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Game game = new Game();
        game.setCurrentPhase(upkeepPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DrawPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase drawPhase = new DrawPhase();
        Game game = new Game();
        game.setCurrentPhase(drawPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main1Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase main1Phase = new Main1Phase();
        Game game = new Game();
        game.setCurrentPhase(main1Phase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Game game = new Game();
        game.setCurrentPhase(combatBeginningPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Game game = new Game();
        game.setCurrentPhase(declareAttackersPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Game game = new Game();
        game.setCurrentPhase(declareBlockersPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatDamagePhase.class, nextPhase.class);
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase combatDamagePhase = new CombatDamagePhase();
        Game game = new Game();
        game.setCurrentPhase(combatDamagePhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatEndPhase.class, nextPhase.class);
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase combatEndPhase = new CombatEndPhase();
        Game game = new Game();
        game.setCurrentPhase(combatEndPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main2Phase.class, nextPhase.class);
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Phase main2Phase = new Main2Phase();
        Game game = new Game();
        game.setCurrentPhase(main2Phase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(EndOfTurnPhase.class, nextPhase.class);
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Game game = new Game();
        game.setCurrentPhase(endOfTurnPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CleanupPhase.class, nextPhase.class);
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Phase cleanupPhase = new CleanupPhase();
        Game game = new Game();
        game.setCurrentPhase(cleanupPhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(PlayerChangePhase.class, nextPhase.class);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Phase playerChangePhase = new PlayerChangePhase();
        Game game = new Game();
        game.setCurrentPhase(playerChangePhase);
        Phase nextPhase = Phase.nextPhase();
        assertEquals(UntapPhase.class, nextPhase.getClass());
        assertEquals(1, game.getPlayingPlayer());
    }

    @Test
    public void testNextPhase_NullCurrentPhase() {
        Game game = new Game();
        Phase nextPhase = Phase.nextPhase();
        assertNull(nextPhase);
    }
}