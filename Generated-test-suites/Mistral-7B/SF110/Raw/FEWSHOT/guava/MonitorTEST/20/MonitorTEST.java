package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class MonitorTest {

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertNotNull(monitor);
        assertNotNull(fairMonitor);
        assertEquals(false, monitor.isFair());
        assertEquals(true, fairMonitor.isFair());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMonitorNullMonitor() {
        Monitor monitor = null;
        monitor.enter();
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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                monitor.enterInterruptibly();
            }
        });
        monitor.enter();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWithGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enterWhen(guard);
        monitor.signalAllWaiters();
        monitor.leave();
    }

    @Test
    public void testEnterUninterruptiblyWithGuard() {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enterWhenUninterruptibly(guard);
        monitor.signalAllWaiters();
        monitor.leave();
    }

    @Test
    public void testEnterWhenWithGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
        condition.signal();
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyWithGuardAndTimeout() {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enterWhenUninterruptibly(guard, 10, TimeUnit.MILLISECONDS);
        condition.signal();
        monitor.leave();
    }

    @Test
    public void testEnterIfWithGuard() {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enterIf(guard);
        monitor.signalAllWaiters();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyWithGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enterIfInterruptibly(guard);
        condition.signal();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithGuardAndTimeout() {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enterIf(guard, 10, TimeUnit.MILLISECONDS);
        condition.signal();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyWithGuardAndTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                monitor.enterIfInterruptibly(guard, 10, TimeUnit.MILLISECONDS);
            }
        });
        monitor.enter();
        condition.signal();
        thread.join();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithGuard() {
        Monitor monitor = new Monitor();
        final Guard guard = new Guard(monitor);

        monitor.lock.lock();
        assertFalse(monitor.tryEnterIf(guard));
        monitor.lock.unlock();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testWaitForWithGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.waitFor(guard);
        condition.signal();
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptiblyWithGuard() {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.waitForUninterruptibly(guard);
        condition.signal();
        monitor.leave();
    }

    @Test
    public void testWaitForWithGuardAndTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.waitFor(guard, 10, TimeUnit.MILLISECONDS);
        condition.signal();
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptiblyWithGuardAndTimeout() {
        Monitor monitor = new Monitor();
        final Condition condition = monitor.lock.newCondition();

        final Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        boolean interrupted = false;
        try {
            monitor.waitForUninterruptibly(guard, 10, TimeUnit.MILLISECONDS);
            assertFalse(guard.isSatisfied());
            Thread.interrupted();
            interrupted = true;
        } catch (InterruptedException e) {
            // expected
        } finally {
            monitor.leave();
            assertFalse(monitor.isOccupiedByCurrentThread());
            assertFalse(interrupted);
        }
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertEquals(false, monitor.isFair());
        assertEquals(true, fairMonitor.isFair());
    }

    @Test
    public void testIsOccupied() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.enter();
        assertEquals(2, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();

        assertEquals(0, monitor.getQueueLength());

        Thread thread = new Thread(() -> monitor.waitFor(guard));
        monitor.enter();
        condition.signal();
        thread.join();
        monitor.leave();

        assertEquals(0, monitor.getQueueLength());

        monitor.enterWhen(guard);
        monitor.waitForUninterruptibly(guard);
        monitor.leave();

        assertEquals(1, monitor.getQueueLength());

        monitor.enterWhen(guard);
        monitor.waitForUninterruptibly(guard);
        monitor.leave();

        assertEquals(1, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();

        assertFalse(monitor.hasQueuedThreads(guard));

        Thread thread = new Thread(() -> monitor.waitFor(guard));
        monitor.enter();
        condition.signal();
        thread.join();
        monitor.leave();

        assertFalse(monitor.hasQueuedThreads(guard));

        monitor.enterWhen(guard);
        monitor.waitForUninterruptibly(guard);
        monitor.leave();

        assertTrue(monitor.hasQueuedThreads(guard));

        monitor.enterWhen(guard);
        monitor.waitForUninterruptibly(guard);
        monitor.leave();

        assertTrue(monitor.hasQueuedThreads(guard));
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Condition condition = monitor.lock.newCondition();
        Guard guard = new Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return condition.isSignaled();
            }
        };

        Thread thread1 = new Thread(() -> monitor.waitFor(guard));
        monitor.enter();
        condition.signal();
        thread1.join();
        monitor.leave();

        Thread thread2 = new Thread(() -> monitor.waitFor(guard));
        monitor.enter();
        condition.signal();
        thread2.join();
        monitor.leave();

        assertFalse(monitor.hasQueuedThread(thread1));
        assertFalse(monitor.hasQueuedThread(thread2));

        monitor.enterWhen(guard);
        monitor.waitForUninterruptibly(guard);
        monitor.leave();

        assertTrue(monitor.hasQueuedThread(thread1));
        assertTrue(monitor.hasQueuedThread(thread2));
    }
}