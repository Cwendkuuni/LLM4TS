package lotus.core.phases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import lotus.core.*;
import lotus.core.effect.*;

public class PhaseTest {

    private static Phase getPhaseByName(String phaseName) {
        try {
            Class<?> clazz = Class.forName("lotus.core.phases." + phaseName.toLowerCase());
            return (Phase) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNextPhaseOrder() {
        Phase[] phases = new Phase[]{
                new UntapPhase(),
                new UpkeepPhase(),
                new DrawPhase(),
                new Main1Phase(),
                new CombatBeginningPhase(),
                new DeclareAttackersPhase(),
                new DeclareBlockersPhase(),
                new CombatDamagePhase(),
                new CombatEndPhase(),
                new Main2Phase(),
                new EndOfTurnPhase(),
                new CleanupPhase(),
                new PlayerChangePhase()
        };

        Phase currentPhase = Game.currentPhase = phases[0];

        for (int i = 1; i < phases.length; i++) {
            Phase nextPhase = phases[i];
            Phase expectedNextPhase = currentPhase.getClass().getDeclaredMethod("nextPhase").invoke(currentPhase);
            assertEquals("Unexpected phase transition. Expected " + nextPhase.getClass().getSimpleName() + ", but got " + expectedNextPhase.getClass().getSimpleName(), nextPhase.getClass(), expectedNextPhase.getClass());
            currentPhase = nextPhase;
        }
    }

    @Test
    public void testChangePhase() {
        Phase untapPhase = getPhaseByName("UntapPhase");
        Phase upkeepPhase = getPhaseByName("UpkeepPhase");
        Phase currentPhase = Game.currentPhase = untapPhase;

        Phase expectedNextPhase = upkeepPhase;
        Phase actualNextPhase = Phase.nextPhase();

        assertEquals("Unexpected next phase", expectedNextPhase, actualNextPhase);
    }

    @Test
    public void testChangePhaseWithNullCurrentPhase() {
        assertNull("Current phase should be null before the first phase", Game.currentPhase);

        Phase untapPhase = getPhaseByName("UntapPhase");
        Phase actualNextPhase = Phase.nextPhase();

        assertNotNull("Unexpected null next phase", actualNextPhase);
        assertEquals("Unexpected next phase", untapPhase.getClass(), actualNextPhase.getClass());

        Game.currentPhase = untapPhase;
        Phase expectedNextPhase2 = upkeepPhase;
        Phase actualNextPhase2 = Phase.nextPhase();

        assertEquals("Unexpected next phase", expectedNextPhase2, actualNextPhase2);
    }
}