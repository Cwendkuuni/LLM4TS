package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    @Test
    public void testMonitorConstructor() {
        Monitor monitor = new Monitor();
        assertNull(monitor.activeGuards);
        assertEquals(false, monitor.isFair());
        ReentrantLock lock = monitor.lock;
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
        assertEquals(0, monitor.getOccupiedDepth());
        assertEquals(0, monitor.getQueueLength());
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testMonitorConstructorWithFairness() {
        Monitor monitor = new Monitor(true);
        assertNull(monitor.activeGuards);
        assertEquals(true, monitor.isFair());
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        ReentrantLock lock = monitor.lock;
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        assertEquals(1, monitor.getOccupiedDepth());
        assertFalse(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithDifferentMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor2.enter();
        monitor1.leave();
        monitor2.leaveInterruptibly();
        monitor1.enter();
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        ReentrantLock lock = monitor.lock;
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithInterruption() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread interruptThread = new Thread(() -> {
            monitor.enterInterruptibly();
            fail("InterruptedException not thrown");
        });
        interruptThread.start();
        Thread.sleep(10);
        monitor.interrupt();
    }

    @Test
    public void testEnterWithGuardAndSatisfiedCondition() {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enterWhen(guard);
        ReentrantLock lock = monitor.lock;
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        assertEquals(1, monitor.getOccupiedDepth());
        assertFalse(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithGuardAndDifferentMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor1) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor1.enterWhen(guard);
        monitor2.enterWhen(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithGuardAndTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
        ReentrantLock lock = monitor.lock;
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        Thread.sleep(2000);
        assertFalse(guard.isSatisfied());
        assertFalse(lock.tryLock());
        monitor.interrupt();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithGuardAndTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enterInterruptiblyWhen(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(guard.isSatisfied());
        assertFalse(monitor.lock.tryLock());
        Thread.sleep(2000);
        monitor.interrupt();
        monitor.enterInterruptiblyWhen(guard, 1, TimeUnit.MILLISECONDS);
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enterIf(guard);
        ReentrantLock lock = monitor.lock;
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        assertEquals(1, monitor.getOccupiedDepth());
        assertFalse(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfWithDifferentMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor1) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor1.enterIf(guard);
        monitor2.enterIf(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
        ReentrantLock lock = monitor.lock;
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
        Thread.sleep(2000);
        assertFalse(guard.isSatisfied());
        assertFalse(monitor.lock.tryLock());
        monitor.interrupt();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfInterruptiblyWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(guard.isSatisfied());
        assertFalse(monitor.lock.tryLock(1, TimeUnit.MILLISECONDS));
        Thread.sleep(2000);
        monitor.interrupt();
        monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithGuardAndSatisfiedCondition() {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enterIf(guard);
        ReentrantLock lock = monitor.lock;
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        assertEquals(1, monitor.getOccupiedDepth());
        assertFalse(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfWithGuardAndDifferentMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor1) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor1.enterIf(guard);
        monitor2.enterIf(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfWithGuardAndTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
        ReentrantLock lock = monitor.lock;
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
        Thread.sleep(2000);
        assertFalse(guard.isSatisfied());
        assertFalse(monitor.lock.tryLock());
        monitor.interrupt();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfInterruptiblyWithGuardAndTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(guard.isSatisfied());
        assertFalse(monitor.lock.tryLock(1, TimeUnit.MILLISECONDS));
        Thread.sleep(2000);
        monitor.interrupt();
        monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertTrue(lock.isLocked());
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
        assertFalse(lock.isLocked());
        assertFalse(lock.isHeldByCurrentThread());
    }
}