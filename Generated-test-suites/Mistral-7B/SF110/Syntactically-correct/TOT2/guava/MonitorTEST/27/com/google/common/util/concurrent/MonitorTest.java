import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class MonitorTest {
    @Test
    public void testMonitorConstructor() {
        Monitor monitor = new Monitor();
        assertTrue(monitor instanceof Monitor);
    }
}