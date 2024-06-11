package lotus.core.phases;

import static org.junit.Assert.*;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private Game game = new Game();

    @Test
    public void testNextPhase_UntapPhase() {
        Phase untapPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(UpkeepPhase.class, nextPhase.getClass());
        Phase.changePhase(untapPhase);
        assertEquals(untapPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DrawPhase.class, nextPhase.getClass());
        Phase.changePhase(upkeepPhase);
        assertEquals(upkeepPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase drawPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(Main1Phase.class, nextPhase.getClass());
        Phase.changePhase(drawPhase);
        assertEquals(drawPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase main1Phase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
        Phase.changePhase(main1Phase);
        assertEquals(main1Phase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
        Phase.changePhase(combatBeginningPhase);
        assertEquals(combatBeginningPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
        Phase.changePhase(declareAttackersPhase);
        assertEquals(declareAttackersPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
        Phase.changePhase(declareBlockersPhase);
        assertEquals(declareBlockersPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CombatEndPhase.class, nextPhase.getClass());
        Phase.changePhase(combatDamagePhase);
        assertEquals(combatDamagePhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase combatEndPhase = new CombatEndPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(Main2Phase.class, nextPhase.getClass());
        Phase.changePhase(combatEndPhase);
        assertEquals(combatEndPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_Main2Phase() {
        Phase main2Phase = new Main2Phase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
        Phase.changePhase(main2Phase);
        assertEquals(main2Phase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
        Phase endOfTurnPhase = new EndOfTurnPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(CleanupPhase.class, nextPhase.getClass());
        Phase.changePhase(endOfTurnPhase);
        assertEquals(endOfTurnPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_CleanupPhase() {
        Phase cleanupPhase = new CleanupPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
        Phase.changePhase(cleanupPhase);
        assertEquals(cleanupPhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
        Phase playerChangePhase = new PlayerChangePhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(UntapPhase.class, nextPhase.getClass());
        Phase.changePhase(playerChangePhase);
        assertEquals(playerChangePhase, game.getGamePhase());
    }

    @Test
    public void testNextPhase_GameOver() {
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
        Phase untapPhase2 = new UntapPhase();

        for (Phase phase : new Phase[]{untapPhase, upkeepPhase, drawPhase, main1Phase, combatBeginningPhase, declareAttackersPhase, declareBlockersPhase, combatDamagePhase, combatEndPhase, main2Phase, endOfTurnPhase, cleanupPhase, playerChangePhase, untapPhase2}) {
            Phase.changePhase(phase);
            assertEquals(game.getGamePhase(), phase);
            if (phase == untapPhase2) {
                assertNull(game.getGamePhase());
                break;
            }
        }
    }
}