import org.junit.Test;
import org.junit.Assert.*;
import lotus.core.phases.Phase;

public class PhaseTest {

    @Test
    public void testNextPhase_UntapPhase_UpkeepPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase = new UpkeepPhase();
        Phase.nextPhase();
        Phase currentPhase = Game.currentPhase;
        assertEquals(upkeepPhase.getClass(), currentPhase.getClass());
    }

    @Test
    public void testNextPhase_UpkeepPhase_DrawPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase.nextPhase();
        Phase currentPhase = Game.currentPhase;
        assertEquals(drawPhase.getClass(), currentPhase.getClass());
    }

    @Test (expected = IllegalStateException.class)
    public void testNextPhase_NullCurrentPhase() {
        Game.currentPhase = null;
        Phase.nextPhase();
    }

    @Test (expected = IllegalStateException.class)
    public void testNextPhase_NullNextPhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Game.currentPhase = upkeepPhase;
        Game.phases = null;
        Phase.nextPhase();
    }

    @Test
    public void testNextPhase_SamePhase() {
        Phase upkeepPhase = new UpkeepPhase();
        Phase.nextPhase();
        Phase currentPhase = Game.currentPhase;
        assertEquals(upkeepPhase.getClass(), currentPhase.getClass());
    }

    @Test
    public void testNextPhase_UntapPhase_UpkeepPhase_UpkeepPhase_DrawPhase() {
        Phase untapPhase = new UntapPhase();
        Phase upkeepPhase1 = new UpkeepPhase();
        Phase upkeepPhase2 = new UpkeepPhase();
        Phase drawPhase = new DrawPhase();
        Phase.nextPhase(); // UntapPhase to UpkeepPhase1
        Phase.nextPhase(); // UpkeepPhase1 to UpkeepPhase2
        Phase.nextPhase(); // UpkeepPhase2 to DrawPhase
        Phase currentPhase = Game.currentPhase;
        assertEquals(drawPhase.getClass(), currentPhase.getClass());
    }

    // Add test cases for other methods and edge cases
}