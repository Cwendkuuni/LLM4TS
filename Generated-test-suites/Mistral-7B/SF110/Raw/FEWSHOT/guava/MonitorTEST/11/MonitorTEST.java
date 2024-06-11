package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;

@Beta
public final class MonitorTest {

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertNull(monitor.activeGuards);
        assertNull(fairMonitor.activeGuards);
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.lock.isHeldByCurrentThread());
        monitor.leave();
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly();
        });
        thread.start();
        thread.join();
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enterWhen(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenUnsatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterWhenWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        boolean result = monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertFalse(result);
    }

    @Test
    public void testEnterWhenWithTimeoutAndSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        boolean result = monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertTrue(result);
    }

    @Test
    public void testEnterWhenUninterruptiblyWithSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenUninterruptiblyWithUnsatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterWhenUninterruptibly(guard);
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeoutAndSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            monitor.tryEnter();
        });
        thread.start();
        thread.join();
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.tryEnterIf(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfUnsatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.tryEnterIf(guard);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.tryEnterIf(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithTimeoutAndSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.tryEnterIf(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            monitor.tryEnterIfInterruptibly(new Monitor.Guard(monitor) {
                @Override
                public boolean isSatisfied() {
                    return false;
                }
            });
        });
        thread.start();
        thread.join();
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfInterruptiblyWithSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.tryEnterIfInterruptibly(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfInterruptiblyWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.tryEnterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfInterruptiblyWithTimeoutAndSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.tryEnterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.waitFor(guard);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.waitForUninterruptibly(guard);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.waitFor(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Long timeout = 1L;
        monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithTimeoutAndSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        Long timeout = 1L;
        monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        monitor.waitForUninterruptibly(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Long timeout = 1L;
        monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeoutAndSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
        Long timeout = 1L;
        monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
        assertTrue(monitor.lock.isHeldByCurrentThread());
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
        assertEquals(0, monitor.getQueueLength());
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.waitFor(guard);
        assertEquals(1, monitor.getQueueLength());
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThreads());
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.waitFor(guard);
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThread(null));
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        Thread thread = new Thread(() -> {
            monitor.waitFor(guard);
        });
        thread.start();
        assertFalse(monitor.hasQueuedThread(thread));
        monitor.leave();
        thread.join();
        assertFalse(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testHasWaiters() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasWaiters(null));
        Monitor.Guard guard = new Monitor.Guard(monitor) {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        };
        monitor.waitFor(guard);
        assertTrue(monitor.hasWaiters(guard));
        monitor.leave();
        assertFalse(monitor.hasWaiters(guard));
    }

}