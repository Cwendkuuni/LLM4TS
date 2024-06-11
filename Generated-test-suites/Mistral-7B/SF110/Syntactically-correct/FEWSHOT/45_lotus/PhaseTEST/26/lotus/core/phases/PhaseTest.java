package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import lotus.core.Game;

import org.junit.Before;
import org.junit.Test;

public class PhaseTest {

    private Game game;

    @Before
    public void setup() {
        game = new Game();
    }

    @Test
    public void testNextPhase_UntapPhase() {
        Phase untapPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), UpkeepPhase.class);
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), DrawPhase.class);
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), Main1Phase.class);
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), CombatBeginningPhase.class);
    }

    @Test
    public void testNextPhase_CombatBeginningPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), DeclareAttackersPhase.class);
    }

    @Test
    public void testNextPhase_DeclareAttackersPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), DeclareBlockersPhase.class);
    }

    @Test
    public void testNextPhase_DeclareBlockersPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), CombatDamagePhase.class);
    }

    @Test
    public void testNextPhase_CombatDamagePhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), CombatEndPhase.class);
    }

    @Test
    public void testNextPhase_CombatEndPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase combatBeginningPhase = new CombatBeginningPhase();
        Phase declareAttackersPhase = new DeclareAttackersPhase();
        Phase declareBlockersPhase = new DeclareBlockersPhase();
        Phase combatDamagePhase = new CombatDamagePhase();
        Phase combatEndPhase = new CombatEndPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), Main2Phase.class);
    }

    @Test
    public void testNextPhase_Main2Phase() {
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
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), EndOfTurnPhase.class);
    }

    @Test
    public void testNextPhase_EndOfTurnPhase() {
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
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), CleanupPhase.class);
    }

    @Test
    public void testNextPhase_CleanupPhase() {
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
        Phase nextPhase = Phase.nextPhase();
        assertNull(nextPhase);
    }

    @Test
    public void testNextPhase_PlayerChangePhase() {
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
        Phase nextPhase = Phase.nextPhase();
        assertEquals(nextPhase.getClass(), UntapPhase.class);
    }
}