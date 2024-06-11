import static org.junit.Assert.*;
import macaw.presentationLayer.MacawWorkBench;

public class MacawWorkBenchTest {

    @Test
    public void testMacawWorkBenchConstruction() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        assertNotNull(macawWorkBench.dialog);
    }
}