package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private Game game = new Game();

    @Test
    public void testNextPhase_UntapPhase_UpkeepPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();

        game.currentPhase = untapPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(upkeepPhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase_DrawPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();

        game.currentPhase = upkeepPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(drawPhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_DrawPhase_Main1Phase() {
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();

        game.currentPhase = drawPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(main1Phase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_Main1Phase_CombatBeginningPhase() {
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();

        game.currentPhase = main1Phase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(combatBeginningPhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase_DeclareAttackersPhase() {
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();

        game.currentPhase = combatBeginningPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(declareAttackersPhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase_DeclareBlockersPhase() {
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();

        game.currentPhase = declareAttackersPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(declareBlockersPhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase_CombatDamagePhase() {
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();

        game.currentPhase = declareBlockersPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(combatDamagePhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatDamagePhase_CombatEndPhase() {
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase combatEndPhase = new CombatEndPhase();

        game.currentPhase = combatDamagePhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(combatEndPhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_CombatEndPhase_Main2Phase() {
        Phase combatEndPhase = new CombatEndPhase();
        Phase main2Phase = new Main2Phase();

        game.currentPhase = combatEndPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(main2Phase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_Main2Phase_EndOfTurnPhase() {
        Phase main2Phase = new Main2Phase();
        Phase endOfTurnPhase = new EndOfTurnPhase();

        game.currentPhase = main2Phase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(endOfTurnPhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase_CleanupPhase() {
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Phase cleanupPhase = new CleanupPhase();

        game.currentPhase = endOfTurnPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(cleanupPhase.getClass(), game.currentPhase.class);
    }

    @Test
    public void testNextPhase_CleanupPhase_PlayerChangePhase() {
        Phase cleanupPhase = new CleanupPhase();
        Phase playerChangePhase = new PlayerChangePhase();

        game.currentPhase = cleanupPhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(playerChangePhase.getClass(), game.currentPhase.getClass());
    }

    @Test
    public void testNextPhase_PlayerChangePhase_UntapPhase() {
        Phase playerChangePhase = new PlayerChangePhase();
        Phase untapPhase = new UntapPhase();

        game.currentPhase = playerChangePhase;
        Phase.nextPhase();

        assertNotNull(game.currentPhase);
        assertEquals(untapPhase.getClass(), game.currentPhase.getClass());
    }
}