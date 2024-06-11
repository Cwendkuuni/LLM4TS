package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static lotus.core.phases.Phase.Game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PhaseTest {

    @Before
    public void setUp() {
        Game.currentPhase = new UntapPhase();
        Game.playingPlayer = 1;
    }

    @After
    public void tearDown() {
        Game.currentPhase = null;
    }

    @Test
    public void testNextPhaseUntapPhase() {
        Phase nextPhase = Phase.nextPhase();
        assertEquals(UpkeepPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseUpkeepPhase() {
        Game.currentPhase = new UpkeepPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DrawPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseDrawPhase() {
        Game.currentPhase = new DrawPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main1Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseMain1Phase() {
        Game.currentPhase = new Main1Phase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatBeginningPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseCombatBeginningPhase() {
        Game.currentPhase = new CombatBeginningPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareAttackersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseDeclareAttackersPhase() {
        Game.currentPhase = new DeclareAttackersPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(DeclareBlockersPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseDeclareBlockersPhase() {
        Game.currentPhase = new DeclareBlockersPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatDamagePhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseCombatDamagePhase() {
        Game.currentPhase = new CombatDamagePhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CombatEndPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseCombatEndPhase() {
        Game.currentPhase = new CombatEndPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(Main2Phase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseMain2Phase() {
        Game.currentPhase = new Main2Phase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(EndOfTurnPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseEndOfTurnPhase() {
        Game.currentPhase = new EndOfTurnPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(CleanupPhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhaseCleanupPhase() {
        Game.currentPhase = new CleanupPhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(PlayerChangePhase.class, nextPhase.getClass());
    }

    @Test
    public void testNextPhasePlayerChangePhase() {
        Game.currentPhase = new PlayerChangePhase();
        Phase nextPhase = Phase.nextPhase();
        assertEquals(UntapPhase.class, nextPhase.getClass());
    }
}