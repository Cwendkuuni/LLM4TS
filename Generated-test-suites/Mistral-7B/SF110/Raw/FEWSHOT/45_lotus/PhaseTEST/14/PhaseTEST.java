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
        game.setCurrentPhase(untapPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Game game = new Game();
        Phase upkeepPhase = new UpkeepPhase();
        game.setCurrentPhase(upkeepPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(DrawPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Game game = new Game();
        Phase drawPhase = new DrawPhase();
        game.setCurrentPhase(drawPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main1Phase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Game game = new Game();
        Phase main1Phase = new Main1Phase();
        game.setCurrentPhase(main1Phase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Game game = new Game();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        game.setCurrentPhase(combatBeginningPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Game game = new Game();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        game.setCurrentPhase(declareAttackersPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Game game = new Game();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        game.setCurrentPhase(declareBlockersPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Game game = new Game();
        Phase combatDamagePhase = new CombatDamagePhase();
        game.setCurrentPhase(combatDamagePhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatEndPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Game game = new Game();
        Phase combatEndPhase = new CombatEndPhase();
        game.setCurrentPhase(combatEndPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main2Phase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Game game = new Game();
        Phase main2Phase = new Main2Phase();
        game.setCurrentPhase(main2Phase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Game game = new Game();
        Phase endOfTurnPhase = new EndOfTurnPhase();
        game.setCurrentPhase(endOfTurnPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(CleanupPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Game game = new Game();
        Phase cleanupPhase = new CleanupPhase();
        game.setCurrentPhase(cleanupPhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Game game = new Game();
        Phase playerChangePhase = new PlayerChangePhase();
        game.setCurrentPhase(playerChangePhase);

        Phase nextPhase = Phase.nextPhase();
        assertEquals(UntapPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);

        game.setPlayingPlayer(2);
        nextPhase = Phase.nextPhase();
        assertEquals(UntapPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
    }

    @Test(expected = IllegalStateException.class)
    public void testNextPhase_InvalidPhase() {
        Game game = new Game();
        Phase unknownPhase = new Object();
        game.setCurrentPhase(unknownPhase);

        Phase nextPhase = Phase.nextPhase();
        assertNull(nextPhase);
    }
}