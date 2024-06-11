package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {
    

    @Test
    public void testMonitorConstructor() {
        Monitor monitor = new Monitor();
        assertNull(monitor.activeGuards);
        assertTrue(monitor.fair);
        ReentrantLock lock = monitor.lock;
        assertFalse(lock.isLocked());
    }

    @Test
    public void testMonitorWithFairFlagConstructor() {
        Monitor monitor = new Monitor(true);
        assertNull(monitor.activeGuards);
        assertTrue(monitor.fair);
        ReentrantLock lock = monitor.lock;
        assertFalse(lock.isLocked());
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        ReentrantLock lock = monitor.lock;
        assertTrue(lock.isLocked());
        monitor.leave();
        assertFalse(lock.isLocked());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyThrowsInterruptedException() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly();
        });
        thread.start();
        thread.interrupt();
        thread.join();
    }

    @Test
    public void testEnterInterruptiblyDoesNotInterruptIfLockIsAcquired() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enter();
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly();
        });
        thread.start();
        thread.join();
        monitor.leave();
        assertFalse(thread.isInterrupted());
    }

    @Test
    public void testEnterWithTimeoutNoInterruptsIfLockIsAcquired() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertFalse(monitor.lock.tryLock(1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testEnterWithTimeoutInterruptsIfLockIsNotAcquired() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
                fail();
            } catch (InterruptedException e) {
                // expected
            }
        });
        thread.start();
        monitor.enter();
        thread.join();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenGuardMonitorMismatch() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor2.enter();
        monitor1.enterWhen(monitor2.lock);
    }

    @Test
    public void testEnterWhenGuardSatisfied() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.leave();
    }

    @Test
    public void testEnterWhenGuardNotSatisfiedBlocks() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        Thread thread = new Thread(() -> monitor.enterWhen(guard));
        thread.start();
        try {
            thread.join();
            fail();
        } catch (InterruptedException e) {
            // expected
        }
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyGuardSatisfied() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.enterWhenUninterruptibly(guard);
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyGuardNotSatisfiedBlocksAndInterrupts() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        Thread thread = new Thread(() -> monitor.enterWhenUninterruptibly(guard));
        thread.start();
        monitor.leave();
        try {
            thread.join();
            fail();
        } catch (InterruptedException e) {
            // expected
        }
    }

    @Test
    public void testEnterWhenWithTimeoutGuardSatisfied() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
        monitor.leave();
    }

    @Test
    public void testEnterWhenWithTimeoutGuardNotSatisfiedInterrupts() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Thread thread = new Thread(() -> {
            try {
                monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
                fail();
            } catch (InterruptedException e) {
                // expected
            }
        });
        monitor.enter();
        thread.start();
        monitor.leave();
        thread.join();
    }

    @Test
    public void testEnterIfGuardSatisfied() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enterIf(guard);
        monitor.leave();
    }

    @Test
    public void testEnterIfGuardNotSatisfied() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        monitor.enterIf(guard);
        monitor.leave();
    }

    @Test
    public void testEnterIfWithTimeoutGuardSatisfied() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
        monitor.leave();
    }

    @Test
    public void testEnterIfWithTimeoutGuardNotSatisfiedInterrupts() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Thread thread = new Thread(() -> {
            try {
                monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
                fail();
            } catch (InterruptedException e) {
                // expected
            }
        });
        monitor.enter();
        thread.start();
        monitor.leave();
        thread.join();
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.tryEnter());
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        assertTrue(monitor.tryEnter());
        monitor.leave();
    }

    @Test
    public void testWaitForGuardSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
    }

    @Test
    public void testWaitForGuardNotSatisfiedBlocks() throws InterruptedException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        Thread thread = new Thread(() -> monitor.waitFor(guard));
        thread.start();
        try {
            thread.join();
            fail();
        } catch (InterruptedException e) {
            // expected
        }
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptiblyGuardSatisfied() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptiblyGuardNotSatisfiedBlocksAndInterrupts() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        Thread thread = new Thread(() -> monitor.waitForUninterruptibly(guard));
        thread.start();
        monitor.leave();
        try {
            thread.join();
            fail();
        } catch (InterruptedException e) {
            // expected
        }
    }

    @Test
    public void testWaitForWithTimeoutGuardSatisfied() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enter();
        monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeoutGuardNotSatisfiedInterrupts() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Thread thread = new Thread(() -> {
            try {
                monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
                fail();
            } catch (InterruptedException e) {
                // expected
            }
        });
        monitor.enter();
        thread.start();
        monitor.leave();
        thread.join();
    }

    @Test
    public void testLeaveWhenMonitorIsNotLocked() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.isOccupied());
        monitor.leave();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testLeaveWhenMonitorIsNotHeldByCurrentThread() {
        Monitor monitor = new Monitor();
        monitor.enter();
        Thread thread = new Thread(() -> monitor.leave());
        thread.start();
        monitor.leave();
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.isFair());
        Monitor monitor2 = new Monitor(false);
        assertFalse(monitor2.isFair());
    }

    @Test
    public void testIsOccupied() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.isOccupied());
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() {
        Monitor monitor = new Monitor();
        assertEquals(0, monitor.getOccupiedDepth());
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
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreadsWithNoThreads() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThreadWithOneThread() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        monitor.enterWhen(guard);
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
    }

    @Test
    public void testHasWaitersWithNoWaiters() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasWaiters(null));
    }

    @Test
    public void testHasWaitersWithOneWaiter() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.leave();
        Monitor otherMonitor = new Monitor();
        assertTrue(monitor.hasWaiters(otherMonitor.lock));
        assertTrue(otherMonitor.hasWaiters(monitor.lock));
    }

    @Test
    public void testGetWaitQueueLengthWithNoWaiters() {
        Monitor monitor = new Monitor();
        assertEquals(0, monitor.getWaitQueueLength(null));
    }

    @Test
    public void testGetWaitQueueLengthWithOneWaiter() {
        Monitor monitor = new Monitor();
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.leave();
        Monitor otherMonitor = new Monitor();
        assertEquals(1, monitor.getWaitQueueLength(otherMonitor.lock));
        assertEquals(1, otherMonitor.getWaitQueueLength(monitor.lock));
    }

    
}