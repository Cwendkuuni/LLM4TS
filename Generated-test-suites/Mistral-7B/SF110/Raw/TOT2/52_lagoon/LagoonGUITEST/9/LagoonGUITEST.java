import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {

    @Test
    public void testLagoonGUI() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertNotNull(lagoonGUI);
    }
}