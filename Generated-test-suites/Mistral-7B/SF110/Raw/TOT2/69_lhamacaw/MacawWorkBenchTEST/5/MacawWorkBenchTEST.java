import org.junit.Test;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {
    @Test
    public void testMacawWorkBenchConstructor() {
        MacawWorkBench macawWorkBench = new MacawWorkBench(new SessionProperties());
        assertNotNull(macawWorkBench);
    }
}