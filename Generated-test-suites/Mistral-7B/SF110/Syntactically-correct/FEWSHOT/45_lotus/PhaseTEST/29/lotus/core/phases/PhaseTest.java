package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private Game game;

    @Before
    public void setUp() {
        this.game = new Game();
    }

    @Test
    public void testNextPhase_UntapPhase() {
        Phase untapPhase = new UntapPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(nextPhase.getClass(), UpkeepPhase.class);
        assertEquals(Game.currentPhase, untapPhase);
    }

    @Test
    public void testNextPhase_UpkeepPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(nextPhase.getClass(), DrawPhase.class);
        assertEquals(Game.currentPhase, upkeepPhase);
    }

    @Test
    public void testNextPhase_DrawPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(nextPhase.getClass(), Main1Phase.class);
        assertEquals(Game.currentPhase, drawPhase);
    }

    @Test
    public void testNextPhase_Main1Phase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase main1Phase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase();

        assertEquals(nextPhase.getClass(), CombatBeginningPhase.class);
        assertEquals(Game.currentPhase, main1Phase);
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
        assertEquals(Game.currentPhase, combatBeginningPhase);
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
        assertEquals(Game.currentPhase, declareAttackersPhase);
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
        assertEquals(Game.currentPhase, declareBlockersPhase);
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
        assertEquals(Game.currentPhase, combatDamagePhase);
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
        assertEquals(Game.currentPhase, combatEndPhase);
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
        assertEquals(Game.currentPhase, main2Phase);
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

        assertNull(nextPhase);
        assertEquals(Game.currentPhase, endOfTurnPhase);
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

        assertEquals(nextPhase.getClass(), PlayerChangePhase.class);
        assertEquals(Game.currentPhase, cleanupPhase);
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

        assertEquals(Game.playingPlayer, 1);
        Phase nextPhase = Phase.nextPhase();

        assertEquals(nextPhase.getClass(), UntapPhase.class);
        assertEquals(Game.currentPhase, playerChangePhase);
        assertEquals(Game.playingPlayer, 2);
    }
}