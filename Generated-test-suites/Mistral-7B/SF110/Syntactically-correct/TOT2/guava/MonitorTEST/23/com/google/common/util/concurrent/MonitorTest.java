import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorTest {
    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertFalse(monitor.isOccupiedByCurrentThread()); // Check lock is acquired
        monitor.leave();
    }
}