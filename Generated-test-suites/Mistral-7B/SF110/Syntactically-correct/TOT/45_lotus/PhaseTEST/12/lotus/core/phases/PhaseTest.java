package lotus.core.phases;

import static org.junit.Assert.*;
import lotus.core.Game;
import lotus.core.effect.ChangePhase;
import org.junit.Before;
import org.junit.Test;

public class PhaseTest {
    private static Phase currentPhase;

    @Before
    public void setUp() {
        Game.currentPhase = new UntapPhase();
    }

    @Test
    public void testUntapPhaseToUpkeepPhase() {
        
        PhaseTest.nextPhase();
        assertInstanceOf(UpkeepPhase.class, Phase.class.cast(Game.currentPhase));
        
    }

    @Test
    public void testUpkeepPhaseToDrawPhase() {
        
        changePhase(new UpkeepPhase());
        PhaseTest.nextPhase();
        assertInstanceOf(DrawPhase.class, Phase.class.cast(Game.currentPhase));
        
    }

    // Add tests for other methods as needed
}