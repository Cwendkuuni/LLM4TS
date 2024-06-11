import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorTest {

    @Test
    public void testMonitorConstructor() {
        Monitor monitor = new Monitor();
        assertNotNull(monitor);
    }

    @Test
    public void testMonitorConstructorWithFairFlag() {
        Monitor monitor = new Monitor(true);
        assertNotNull(monitor);
    }

    @Test
    public void testEnterMethod() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyMethodInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread interrupterThread = new Thread(() -> monitor.enterInterruptibly());
        interrupterThread.start();
        monitor.enterInterruptibly();
        interrupterThread.join();
    }

    @Test
    public void testEnterMethodWithTimeLimit() throws InterruptedException {
        Monitor monitor = new Monitor();
        boolean entered = monitor.enter(1, TimeUnit.SECONDS);
        assertTrue(entered);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the remaining methods with similar test cases, checking their functionality
}