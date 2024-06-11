import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.MonitorMonitor;
import org.junit.Test;

public class MonitorTest {

    @Test
    public void testEnter_WhenLockIsNotHeld_ThenThrowsIllegalMonitorStateException() {
        Monitor monitor = new Monitor();
        assertThrows(IllegalMonitorStateException.class, () -> monitor.enter());
    }

    @Test
    public void testEnter_WhenLockIsHeldByCurrentThread_ThenSuccess() {
        Monitor monitor = new Monitor();
        monitor.enter();
        MonitorMonitor monitorMonitor = new MonitorMonitor();
        monitor.enterWhen(monitorMonitor);
        monitor.leave();
    }

    @Test
    public void testEnter_WhenLockIsHeldByOtherThread_ThenThrowsIllegalMonitorStateException() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        MonitorMonitor monitorMonitor1 = new MonitorMonitor();
        MonitorMonitor monitorMonitor2 = new MonitorMonitor();

        Thread thread1 = new Thread(() -> {
            monitor1.enter();
            monitorMonitor1.isSatisfied = true; // Simulate guard condition
            monitor2.enterWhen(monitorMonitor2);
            monitor1.leave();
        });

        thread1.start();
        try {
            monitor2.enter();
        } catch (IllegalMonitorStateException e) {
            assertNotNull(thread1.isAlive());
            assertFalse(monitor2.isOccupied());
            throw e;
        }
    }

    @Test
    public void testEnterInterruptibly_WhenLockIsHeldByCurrentThread_ThenSuccess() {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        MonitorMonitor monitorMonitor = new MonitorMonitor();
        monitor.enterWhen(monitorMonitor);
        monitor.leave();
    }

    @Test
    public void testEnterInterruptibly_WhenLockIsHeldByOtherThread_ThenInterruptedExceptionThrown() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        MonitorMonitor monitorMonitor1 = new MonitorMonitor();
        MonitorMonitor monitorMonitor2 = new MonitorMonitor();

        Thread thread1 = new Thread(() -> {
            monitor1.enter();
            monitorMonitor1.isSatisfied = true; // Simulate guard condition
            try {
                monitor2.enterInterruptibly();
            } catch (InterruptedException e) {
                throw new AssertionError("Unexpected exception", e);
            }
            monitor1.leave();
        });

        thread1.start();
        try {
            monitor2.enterInterruptibly();
        } catch (InterruptedException e) {
            assertNotNull(thread1.isAlive());
            assertFalse(monitor2.isOccupied());
            return;
        }

        fail("InterruptedException not thrown");
    }

    // Repeat the test cases for enterWhen, enterWhenUninterruptibly, enterWhen(long time, TimeUnit unit), enterWhenUninterruptibly(long time, TimeUnit unit), enterIf, enterIfInterruptibly, enterIf(long time, TimeUnit unit), enterIfInterruptibly(long time, TimeUnit unit), and tryEnterIf

    private static class MonitorMonitor {
        boolean isSatisfied;
        Monitor monitor;

        MonitorMonitor() {
            this.monitor = null;
        }

        public boolean isSatisfied() {
            return isSatisfied;
        }
    }
}