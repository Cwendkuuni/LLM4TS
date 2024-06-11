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
        Phase currentPhase = new UntapPhase();
        assertNull(Game.currentPhase);
        Phase previousPhase = game.currentPhase;
        game.currentPhase = currentPhase;
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(previousPhase, currentPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Game game = new Game();
        Phase currentPhase = new UpkeepPhase();
        Phase previousPhase = new UntapPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(DrawPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Game game = new Game();
        Phase currentPhase = new DrawPhase();
        Phase previousPhase = new UpkeepPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(Main1Phase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Game game = new Game();
        Phase currentPhase = new Main1Phase();
        Phase previousPhase = new DrawPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Game game = new Game();
        Phase currentPhase = new CombatBeginningPhase();
        Phase previousPhase = new Main1Phase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Game game = new Game();
        Phase currentPhase = new DeclareAttackersPhase();
        Phase previousPhase = new CombatBeginningPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Game game = new Game();
        Phase currentPhase = new DeclareBlockersPhase();
        Phase previousPhase = new DeclareAttackersPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Game game = new Game();
        Phase currentPhase = new CombatDamagePhase();
        Phase previousPhase = new DeclareBlockersPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(CombatEndPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Game game = new Game();
        Phase currentPhase = new CombatEndPhase();
        Phase previousPhase = new CombatDamagePhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(Main2Phase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Game game = new Game();
        Phase currentPhase = new Main2Phase();
        Phase previousPhase = new CombatEndPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Game game = new Game();
        Phase currentPhase = new EndOfTurnPhase();
        Phase previousPhase = new Main2Phase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(CleanupPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Game game = new Game();
        Phase currentPhase = new CleanupPhase();
        Phase previousPhase = new EndOfTurnPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Game game = new Game();
        Phase currentPhase = new PlayerChangePhase();
        Phase previousPhase = new CleanupPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(UntapPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, previousPhase);
    }

    @Test
    public void testNextPhase_LastPhase() {
        Game game = new Game();
        Phase currentPhase = new UntapPhase();
        game.currentPhase = currentPhase;
        currentPhase.doPhase();
        Phase nextPhase = game.currentPhase;
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
        assertNotNull(nextPhase);
        assertEquals(currentPhase, game.currentPhase);
        try {
            nextPhase.doPhase();
            fail("Should throw an exception as there is no next phase");
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testChangePhase() {
        Game game = new Game();
        Phase previousPhase = new UntapPhase();
        game.currentPhase = previousPhase;
        previousPhase.doPhase();

        Phase phaseToChangeTo = new UpkeepPhase();
        Phase newPhase = Phase.changePhase(phaseToChangeTo);
        assertEquals(UpkeepPhase.class, newPhase.getClass());

        Phase currentPhase = game.currentPhase;
        assertEquals(UpkeepPhase.class, currentPhase.getClass());
    }
}