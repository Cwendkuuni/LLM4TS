package com.example.monitor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.util.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    private Monitor monitor;

    @Before
    public void setUp() {
        monitor = new Monitor();
    }

    

    // Test for the constructor with no arguments
    @Test
    public void testMonitorEmptyConstructor() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.isOccupied());
        assertNull(monitor.getActiveGuards());
    }

    // Test for the constructor with a fair lock
    @Test
    public void testMonitorFairConstructor() {
        Monitor monitor = new Monitor(true);
        Monitor monitor2 = new Monitor(true);

        monitor.enter();
        assertFalse(monitor2.tryEnter());
        assertTrue(monitor.isOccupied());
        assertFalse(monitor2.isOccupied());

        monitor.leave();
        assertTrue(monitor2.tryEnter());
        assertFalse(monitor.isOccupied());
        assertTrue(monitor2.isOccupied());
    }

    // Test the enter method
    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the enterInterruptibly method
    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly();
            fail();
        });
        monitor.enter();
        thread.start();
        thread.join();
    }

    // Test the enterInterruptibly method with timeout
    @Test
    public void testEnterInterruptiblyTimeout() {
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
            fail();
        });
        monitor.enter();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the tryEnter method
    @Test
    public void testTryEnter() {
        monitor.enter();
        assertFalse(monitor.tryEnter());
        monitor.leave();
        assertTrue(monitor.tryEnter());
    }

    // Test the enterWhen method
    @Test
    public void testEnterWhen() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enter();
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the enterWhenUninterruptibly method
    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enter();
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the enterWhen method with timeout
    @Test(expected = InterruptedException.class)
    public void testEnterWhenTimeoutInterrupted() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
        fail();
    }

    // Test the enterWhenUninterruptibly method with timeout
    @Test
    public void testEnterWhenUninterruptiblyTimeout() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the enterIf method
    @Test
    public void testEnterIf() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enter();
        assertTrue(monitor.enterIf(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the enterIfInterruptibly method
    @Test(expected = InterruptedException.class)
    public void testEnterIfInterruptiblyInterrupted() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.enterIfInterruptibly(guard);
        fail();
    }

    // Test the enterIf method with timeout
    @Test(expected = InterruptedException.class)
    public void testEnterIfTimeoutInterrupted() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
        fail();
    }

    // Test the enterIfInterruptibly method with timeout
    @Test
    public void testEnterIfUninterruptiblyTimeout() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.enterIfUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the tryEnterIf method
    @Test
    public void testTryEnterIf() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };

        monitor.enter();
        assertTrue(monitor.tryEnterIf(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the waitFor method
    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.waitFor(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    // Test the waitForUninterruptibly method
    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the waitFor method with timeout
    @Test
    public void testWaitForTimeout() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the waitForUninterruptibly method with timeout
    @Test
    public void testWaitForUninterruptiblyTimeout() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the leave method
    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the isFair method
    @Test
    public void testIsFair() {
        Monitor monitor2 = new Monitor(true);
        assertTrue(monitor2.isFair());

        Monitor monitor3 = new Monitor(false);
        assertFalse(monitor3.isFair());
    }

    // Test the isOccupied method
    @Test
    public void testIsOccupied() {
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    // Test the isOccupiedByCurrentThread method
    @Test
    public void testIsOccupiedByCurrentThread() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the getOccupiedDepth method
    @Test
    public void testGetOccupiedDepth() {
        monitor.enter();
        monitor.enter();
        int occupiedDepth = monitor.getOccupiedDepth();
        assertEquals(2, occupiedDepth);
        monitor.leave();
        monitor.leave();
        occupiedDepth = monitor.getOccupiedDepth();
        assertEquals(0, occupiedDepth);
    }

    // Test the getQueueLength method
    @Test
    public void testGetQueueLength() {
        Monitor monitor2 = new Monitor();
        monitor.enter();
        monitor.enter();
        int queueLength = monitor2.getQueueLength();
        assertEquals(0, queueLength);

        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor2.enterWhen(guard);
        queueLength = monitor2.getQueueLength();
        assertEquals(1, queueLength);
    }

    // Test the hasQueuedThreads method
    @Test
    public void testHasQueuedThreads() {
        Monitor monitor2 = new Monitor();
        monitor.enter();
        monitor.enter();
        assertFalse(monitor2.hasQueuedThreads(guard));

        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor2.enterWhen(guard);
        assertTrue(monitor2.hasQueuedThreads(guard));
    }

    // Test the waitFor method with signalBeforeWaiting true
    @Test
    public void testWaitForSignalBeforeWaiting() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.waitFor(guard, true);
        monitor.leave();

        monitor.enter();
        monitor.waitFor(guard, true);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    // Test the waitForUninterruptibly method with signalBeforeWaiting true
    @Test
    public void testWaitForUninterruptiblySignalBeforeWaiting() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.waitForUninterruptibly(guard, true);
        monitor.leave();

        monitor.enter();
        monitor.waitForUninterruptibly(guard, true);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    // Test the awaitNanos method
    @Test
    public void testAwaitNanos() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        long nanos = monitor.awaitNanos(guard, Long.MAX_VALUE);
        assertEquals(-1, nanos);
        monitor.leave();

        monitor.enter();
        nanos = monitor.awaitNanos(guard, 1, TimeUnit.MILLISECONDS);
        assertEquals(-1, nanos);
        monitor.leave();
    }

    // Test the awaitUninterruptibly method with timeout
    @Test
    public void testAwaitUninterruptiblyTimeout() {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.awaitUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the awaitNanos method with timeout and signalBeforeWaiting true
    @Test
    public void testAwaitNanosTimeoutSignalBeforeWaiting() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.awaitNanos(guard, 1, TimeUnit.MILLISECONDS, true);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    // Test the awaitNanos method with timeout and signalAllWaiters()
    @Test
    public void testAwaitNanosTimeoutSignalAllWaiters() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard1 = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Guard guard2 = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.awaitNanos(guard1, 1, TimeUnit.MILLISECONDS);
        monitor.signalAllWaiters();
        monitor.awaitNanos(guard2, 1, TimeUnit.MILLISECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    // Test the awaitNanos method with timeout and signalNextWaiter()
    @Test
    public void testAwaitNanosTimeoutSignalNextWaiter() throws InterruptedException {
        Monitor monitor2 = new Monitor();
        Guard guard1 = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Guard guard2 = new Guard(monitor2) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };

        monitor.enter();
        monitor.awaitNanos(guard1, 1, TimeUnit.MILLISECONDS);
        monitor.signalNextWaiter();
        monitor.awaitNanos(guard2, 1, TimeUnit.MILLISECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    

}