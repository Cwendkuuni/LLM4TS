import org.junit.jupitermark.junit4.TestMethod;
import org.junit.jupitermark.junit4.TestReporting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lotus.core.Game;
import lotus.core.phases.*;

@TestReporting(disableActions = {TestReporting.Actions.DISPLAY.SUMMARY})
public class PhaseTest {

    @Test
    public void testChangePhase() {
        Phase currentPhase = new UntapPhase();
        Phase newPhase = new UpkeepPhase();
        Phase expectedPhase = new UpkeepPhase();

        Phase.changePhase(currentPhase);
        Phase actualPhase = Game.currentPhase;

        Assertions.assertDoesNotThrow(() -> Phase.changePhase(null));
        Assertions.assertEquals(expectedPhase, actualPhase);

        currentPhase = new EndOfTurnPhase();
        newPhase = new UntapPhase();

        Phase.changePhase(currentPhase);
        actualPhase = Game.currentPhase;

        Assertions.assertEquals(newPhase, actualPhase);
    }

    @Test
    public void testDoPhase() {
        // Cannot test an abstract method directly
    }

    @Test
    public void testNextPhase() {
        // Test nextPhase() method instead
    }
}