import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import static com.google.common.base.Preconditions.*;

public class MonitorTest {

    @Test
    public void testMonitorConstructor() {
        Monitor monitor = new Monitor();
        assertNotNull(monitor);
    }

    @Test
    public void testMonitorFairConstructor() {
        Monitor monitor = new Monitor(true);
        assertNotNull(monitor);
        assertTrue(monitor.isFair());
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly();
        });
        monitor.enter();
        thread.start();
        assertTrue(thread.isInterrupted());
    }

    @Test
    public void testEnterWithTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        long timeout = 100;
        try {
            monitor.enter(timeout, TimeUnit.MILLISECONDS);
            fail("Expected TimeoutException");
        } catch (TimeoutException e) {
            // Expected TimeoutException
        }
    }

    @Test
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly(100, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                monitor.enterInterruptibly(100, TimeUnit.MILLISECONDS);
            }
        });
        monitor.enter();
        thread.start();
        assertTrue(Thread.interrupted());
        assertTrue(thread.isLocked());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        // Try to lock the monitor without succeeding
        assertFalse(monitor.tryEnter());
        // Lock the monitor and try to lock it again
        monitor.enter();
        monitor.enter();
        assertTrue(monitor.tryEnter());
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enterWhen(guard);
        // Check if the monitor is locked
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Thread thread = new Thread(() -> {
            monitor.enterWhen(guard);
        });
        monitor.enter();
        thread.start();
        // Check if the thread is interrupted when trying to lock the monitor
        assertTrue(thread.isInterrupted());
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        // Try to lock the monitor if the guard is satisfied
        monitor.enterIf(guard);
        // Check if the monitor is locked
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Thread thread = new Thread(() -> {
            monitor.enterIf(guard);
        });
        monitor.enter();
        thread.start();
        // Check if the thread is interrupted when trying to lock the monitor
        assertTrue(thread.isInterrupted());
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.waitFor(guard);
        // Check if the monitor is still locked
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Thread thread = new Thread(() -> {
            monitor.enter();
            monitor.waitFor(guard);
        });
        monitor.enter();
        thread.start();
        // Check if the thread is interrupted when waiting for the guard
        assertTrue(thread.isInterrupted());
    }

    @Test
    public void testWaitForUninterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        // Check if the monitor is still locked
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Thread thread = new Thread(() -> {
            monitor.enter();
            monitor.waitForUninterruptibly(guard);
        });
        monitor.enter();
        thread.start();
        // Check if the thread is not interrupted when waiting for the guard uninterruptibly
        assertFalse(thread.isInterrupted());
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        // Check if the monitor is not locked
        assertFalse(monitor.isOccupiedByCurrentThread());
    }
}