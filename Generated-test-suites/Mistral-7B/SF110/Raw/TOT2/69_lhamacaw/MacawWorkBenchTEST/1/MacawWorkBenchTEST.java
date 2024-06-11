import org.junit.Test;
import static org.junit.Assert.*;
import macaw.presentationLayer.MacawWorkBench;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchConstruction() {
        SessionProperties sessionProperties = new SessionProperties(); // assuming SessionProperties is properly set up
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench.dialog);
        assertNotNull(macawWorkBench.editConstants);
        assertNotNull(macawWorkBench.editVariables);
        assertNotNull(macawWorkBench.exportVariableData);
        assertNotNull(macawWorkBench.exit);
    }
}