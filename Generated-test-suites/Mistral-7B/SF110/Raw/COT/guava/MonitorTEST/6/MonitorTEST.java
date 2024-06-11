package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Guard;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GuardedBy;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor.Guard;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Beta
public class MonitorTest {

    private Monitor monitor;
    private Monitor.Guard guard;

    @Before
    public void setUp() {
        monitor = new Monitor();
        guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
    }

    @Test
    public void testMonitorConstructor() {
        Monitor monitor = new Monitor();
    }

    @Test
    public void testMonitorConstructorWithFairFlag() {
        Monitor monitor = new Monitor(true);
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyThrowsInterruptedExceptionWhenInterrupted() {
        Thread interruptThread = new Thread(() -> {
            monitor.enterInterruptibly();
            fail("InterruptedException not thrown");
        });
        interruptThread.start();
        monitor.enter();
        interruptThread.interrupt();
    }

    @Test
    public void testEnterWithTimeOut() {
        long timeoutNanos = 100;
        assertTrue(monitor.enter(timeoutNanos, TimeUnit.NANOSECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithTimeOutThrowsInterruptedExceptionWhenInterrupted() {
        long timeoutNanos = 100;
        Thread interruptThread = new Thread(() -> {
            monitor.enterInterruptibly(timeoutNanos, TimeUnit.NANOSECONDS);
            fail("InterruptedException not thrown");
        });
        interruptThread.start();
        monitor.enterInterruptibly(timeoutNanos, TimeUnit.NANOSECONDS);
        interruptThread.interrupt();
    }

    @Test
    public void testTryEnter() {
        assertFalse(monitor.tryEnter());
        monitor.enter();
        assertTrue(monitor.tryEnter());
        assertFalse(monitor.tryEnter());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenWithWrongGuard() {
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterWhen() {
        monitor.enter();
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenInterruptiblyThrowsInterruptedExceptionWhenInterrupted() {
        Thread interruptThread = new Thread(() -> {
            monitor.enterWhenInterruptibly(guard);
            fail("InterruptedException not thrown");
        });
        interruptThread.start();
        monitor.enterWhenInterruptibly(guard);
        interruptThread.interrupt();
    }

    @Test
    public void testEnterWhenWithTimeOut() {
        long timeoutNanos = 100;
        monitor.enter();
        monitor.enterWhen(guard, timeoutNanos, TimeUnit.NANOSECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenInterruptiblyWithTimeOutThrowsInterruptedExceptionWhenInterrupted() {
        long timeoutNanos = 100;
        Thread interruptThread = new Thread(() -> {
            monitor.enterWhenInterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
            fail("InterruptedException not thrown");
        });
        interruptThread.start();
        monitor.enterWhenInterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
        interruptThread.interrupt();
    }

    @Test
    public void testEnterIf() {
        monitor.enter();
        assertTrue(monitor.enterIf(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfWithWrongGuard() {
        monitor.enter();
        assertFalse(monitor.enterIf(new Monitor.Guard(monitor) {}));
    }

    @Test
    public void testEnterIfWithTimeOut() {
        long timeoutNanos = 100;
        monitor.enter();
        assertFalse(monitor.enterIf(guard, timeoutNanos, TimeUnit.NANOSECONDS));
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfInterruptiblyThrowsInterruptedExceptionWhenInterrupted() {
        Thread interruptThread = new Thread(() -> {
            monitor.enterIfInterruptibly(guard);
            fail("InterruptedException not thrown");
        });
        interruptThread.start();
        monitor.enterIfInterruptibly(guard);
        interruptThread.interrupt();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfInterruptiblyWithWrongGuard() {
        monitor.enter();
        assertFalse(monitor.enterIfInterruptibly(guard));
    }

    @Test
    public void testEnterIfWithTimeOutAndSatisfiedGuard() {
        monitor.enter();
        assertTrue(monitor.enterIf(guard, 100, TimeUnit.MILLISECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfInterruptiblyWithTimeOutAndNotSatisfiedGuardThrowsInterruptedException() {
        Thread interruptThread = new Thread(() -> {
            monitor.enterIfInterruptibly(guard, 100, TimeUnit.MILLISECONDS);
            fail("InterruptedException not thrown");
        });
        interruptThread.start();
        monitor.enterIfInterruptibly(guard, 100, TimeUnit.MILLISECONDS);
        interruptThread.interrupt();
    }

    @Test
    public void testTryEnterIf() {
        monitor.enter();
        assertTrue(monitor.tryEnterIf(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithNotSatisfiedGuard() {
        monitor.enter();
        assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testWaitForWithWrongMonitor() {
        Monitor otherMonitor = new Monitor();
        monitor.enter();
        try {
            otherMonitor.waitFor(guard);
            fail("IllegalMonitorStateException not thrown");
        } catch (IllegalMonitorStateException e) {
            // Expected
        }
    }

    @Test
    public void testWaitForUninterruptibly() {
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        monitor.leave();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testWaitForUninterruptiblyWithWrongMonitor() {
        Monitor otherMonitor = new Monitor();
        monitor.enter();
        try {
            otherMonitor.waitForUninterruptibly(guard);
            fail("IllegalMonitorStateException not thrown");
        } catch (IllegalMonitorStateException e) {
            // Expected
        }
    }

    @Test
    public void testWaitForWithTimeOut() throws InterruptedException {
        monitor.enter();
        long timeoutNanos = 100;
        long startTime = System.nanoTime();
        boolean waited = false;
        try {
            monitor.waitFor(guard, timeoutNanos, TimeUnit.NANOSECONDS);
            waited = true;
        } finally {
            long elapsedNanos = System.nanoTime() - startTime;
            assertEquals(timeoutNanos, elapsedNanos);
        }
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(waited);
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterruptiblyWithTimeOutThrowsInterruptedExceptionWhenInterrupted() {
        monitor.enter();
        long timeoutNanos = 100;
        Thread interruptThread = new Thread(() -> {
            monitor.waitForInterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
            fail("InterruptedException not thrown");
        });
        interruptThread.start();
        monitor.waitForInterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
        interruptThread.interrupt();
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeOut() {
        monitor.enter();
        long timeoutNanos = 100;
        monitor.waitForUninterruptibly(guard, timeoutNanos, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testLeaveWhenNotOccupied() {
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.enter();
        try {
            monitor.leave();
            fail("IllegalMonitorStateException not thrown");
        } catch (IllegalMonitorStateException e) {
            // Expected
        }
    }

    @Test
    public void testIsFair() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor(true);
        assertEquals(false, monitor1.isFair());
        assertEquals(true, monitor2.isFair());
    }

    @Test
    public void testIsOccupied() {
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() {
        monitor.enter();
        monitor.enter();
        assertEquals(2, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        monitor.enter();
        Thread thread1 = new Thread(() -> {
            monitor.enter();
        });
        Thread thread2 = new Thread(() -> {
            monitor.enter();
        });
        monitor.leave();
        monitor.waitFor(guard);
        assertEquals(2, monitor.getQueueLength());
        thread1.join();
        thread2.join();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enter();
        Thread thread1 = new Thread(() -> {
            monitor.enter();
        });
        Thread thread2 = new Thread(() -> {
            monitor.enter();
        });
        monitor.leave();
        assertTrue(monitor.hasQueuedThreads(guard));
        monitor.waitFor(guard);
        thread1.join();
        thread2.join();
        assertFalse(monitor.hasQueuedThreads(guard));
    }

    @Test
    public void testHasQueuedThread() {
        monitor.enter();
        Thread thread1 = new Thread(() -> {
            monitor.enter();
        });
        Thread thread2 = new Thread(() -> {
            monitor.enter();
        });
        monitor.leave();
        assertTrue(monitor.hasQueuedThread(thread1));
        monitor.waitFor(guard);
        thread1.join();
        thread2.join();
        assertFalse(monitor.hasQueuedThread(thread1));
    }

    @Test
    public void testSignalNextWaiter() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        Guard guard1 = new Monitor.Guard(monitor1);
        Guard guard2 = new Monitor.Guard(monitor2);
        monitor1.enter();
        monitor1.signalNextWaiter();
        monitor2.enterWhen(guard2);
        assertTrue(monitor2.isOccupiedByCurrentThread());
        monitor1.leave();
        assertFalse(monitor2.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        assertFalse(monitor.isSatisfied(guard));
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.leave();
        assertFalse(monitor.isSatisfied(guard));
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.leave();
        monitor.signalNextWaiter();
        assertTrue(monitor.isSatisfied(guard));
    }

    @Test
    public void testSignalAllWaiters() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.leave();
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.leave();
        monitor.signalAllWaiters();
        assertTrue(monitor.isOccupiedByCurrentThread());
        assertTrue(monitor.isSatisfied(guard));
    }
}