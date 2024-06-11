import static org.junit.Assert.*;
import com.google.common.util.concurrent.Monitor;
import org.junit.Test;

public class MonitorTest {
    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }
}