package lotus.core.phases.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import lotus.core.Game;
import lotus.core.Phases.Phase;
import lotus.core.Phases.CombatBeginningPhase;
import lotus.core.Phases.CombatDamagePhase;
import lotus.core.Phases.CombatEndPhase;
import lotus.core.Phases.DeclareAttackersPhase;
import lotus.core.Phases.DeclareBlockersPhase;
import lotus.core.Phases.DrawPhase;
import lotus.core.Phases.EndOfTurnPhase;
import lotus.core.Phases.Main1Phase;
import lotus.core.Phases.Main2Phase;
import lotus.core.Phases.PlayerChangePhase;
import lotus.core.Phases.UntapPhase;

public class PhaseTest
{
    private Phase currentPhase;

    @Before
    public void setUp()
    {
        Game.initializeGame();
        currentPhase = Game.currentPhase;
    }

    @Test
    public void testDoPhaseAbstractMethod()
    {
        assertNull(currentPhase.doPhase());
    }

    @Test
    public void testChangePhase()
    {
        Phase phase;

        // Test changing phase to each valid phase
        phase = new UntapPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof UntapPhase);

        phase = new UpkeepPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof UpkeepPhase);

        phase = new DrawPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof DrawPhase);

        phase = new Main1Phase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof Main1Phase);

        phase = new CombatBeginningPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof CombatBeginningPhase);

        phase = new DeclareAttackersPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof DeclareAttackersPhase);

        phase = new DeclareBlockersPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof DeclareBlockersPhase);

        phase = new CombatDamagePhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof CombatDamagePhase);

        phase = new CombatEndPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof CombatEndPhase);

        phase = new Main2Phase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof Main2Phase);

        phase = new EndOfTurnPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof EndOfTurnPhase);

        phase = new CleanupPhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof CleanupPhase);

        phase = new PlayerChangePhase();
        PhaseTest.changePhase(phase);
        assertNotNull(Game.currentPhase);
        assertTrue(Game.currentPhase instanceof PlayerChangePhase);

        // Test changing phase to invalid phase
        phase = new Phase();
        assertFalse(PhaseTest.changePhase(phase));
        assertNotNull(Game.currentPhase);
        assertSame(Game.currentPhase, currentPhase);
    }

    @Test(expected = IllegalStateException.class)
    public void testChangePhaseToSamePhase()
    {
        PhaseTest.changePhase(currentPhase);
    }

    @Test
    public void testNextPhase()
    {
        // Test next phase in valid order
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();
        PhaseTest.nextPhase();

        // Test next phase in invalid order
        Game.currentPhase = new UntapPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new UpkeepPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new DrawPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new Main1Phase();
        PhaseTest.nextPhase();
        Game.currentPhase = new CombatBeginningPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new DeclareAttackersPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new DeclareBlockersPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new CombatDamagePhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new CombatEndPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new Main2Phase();
        PhaseTest.nextPhase();
        Game.currentPhase = new EndOfTurnPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new CleanupPhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new PlayerChangePhase();
        PhaseTest.nextPhase();
        Game.currentPhase = new UntapPhase(); // This should be a loop
    }

    private static void changePhase(final Phase p) {
        Game.currentPhase = p;
        final ChangePhase effect = new ChangePhase(p);
        effect.resolve();
    }
}