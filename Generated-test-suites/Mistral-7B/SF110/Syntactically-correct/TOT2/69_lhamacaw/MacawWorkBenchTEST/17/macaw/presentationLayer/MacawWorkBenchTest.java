import org.junit.Test;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {
    @Test
    public void testMacawWorkBenchConstructor() {
        SessionProperties sessionProperties = new SessionProperties();
        MacawWorkBench macawWorkBench = new MacawWorkBench(sessionProperties);
        assertNotNull(macawWorkBench);
    }
}