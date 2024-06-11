package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor.Guard;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    private Monitor monitor;
    private ReentrantLock lock;
    private Condition condition;

    @Before
    public void setUp() {
        monitor = new Monitor();
        lock = monitor.lock;
        condition = monitor.lock.newCondition();
    }

    @Test
    public void testConstructor() {
        Monitor monitor = new Monitor();
        assertNull(monitor.activeGuards);
        assertTrue(monitor.isFair());
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithTimeout() throws InterruptedException, TimeoutException {
        long startTime = System.nanoTime();
        boolean entered = monitor.enter(1, TimeUnit.NANOSECONDS);
        long duration = System.nanoTime() - startTime;
        assertTrue(entered);
        assertTrue(duration < 1);
        monitor.leave();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        monitor.enterInterruptibly(1, TimeUnit.NANOSECONDS);
    }

    @Test
    public void testTryEnter() {
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        assertFalse(monitor.tryEnterIf(guard));
        monitor.enter();
        assertTrue(monitor.tryEnterIf(guard));
        monitor.leave();
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterWhen(guard);
        monitor.awaitUninterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptibly() throws InterruptedException {
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterWhenUninterruptibly(guard);
        monitor.awaitUninterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterWhenWithTimeout() throws InterruptedException, TimeoutException {
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        Thread.sleep(1500); // give some time for the thread to wait
        assertTrue(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterIf() {
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enterIf(guard);
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfWithInvalidGuard() {
        Monitor anotherMonitor = new Monitor();
        Guard guard = new Guard(anotherMonitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterIf(guard);
    }

    @Test
    public void testIsFair() {
        Monitor fairMonitor = new Monitor(true);
        Monitor unfairMonitor = new Monitor();
        assertTrue(fairMonitor.isFair());
        assertFalse(unfairMonitor.isFair());
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
        monitor.enter();
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enterWhen(guard);
        assertEquals(1, monitor.getQueueLength());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterWhen(guard);
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Guard guard1 = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Guard guard2 = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterWhen(guard1);
        monitor.enterWhen(guard2);
        assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
    }
}