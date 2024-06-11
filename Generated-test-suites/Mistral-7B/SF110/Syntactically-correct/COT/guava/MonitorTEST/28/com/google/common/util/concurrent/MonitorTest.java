package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.*;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        Monitor monitorFair = new Monitor(true);
        assertNotNull(monitor);
        assertNotNull(monitorFair);
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongMonitor() throws InterruptedException {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor2.enter(); // Thread 2
        monitor1.leave();
        monitor2.enter(); // Thread 2
        monitor1.enter(); // Thread 1
        monitor1.await(new Guard() {}, true); // Thread 1
        monitor2.leave(); // Thread 2
        monitor1.leave(); // Thread 1
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread interruptThread = new Thread(() -> monitor.enterInterruptibly());
        interruptThread.start();
        Thread.sleep(100);
        monitor.interrupt();
    }

    @Test
    public void testEnterWithTime() {
        Monitor monitor = new Monitor();
        long start = System.currentTimeMillis();
        boolean result = monitor.enter(1, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(result);
        assertTrue(elapsed >= 1);
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithTimeInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread interruptThread = new Thread(() -> monitor.enter());
        interruptThread.start();
        monitor.interrupt();
        monitor.enter(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testEnterInterruptiblyWithTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        long start = System.currentTimeMillis();
        boolean result = monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(result);
        assertTrue(elapsed >= 1);
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithTimeInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread interruptThread = new Thread(() -> monitor.enter());
        interruptThread.start();
        monitor.interrupt();
        monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testTryEnterWhenSatisfied() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.enterWhen(new Guard() {}, true));
        thread.start();
        monitor.enter();
        thread.join();
        assertTrue(monitor.tryEnter());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenWithWrongMonitor() throws InterruptedException {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor1.leave();
        monitor2.enter(); // Thread 2
        monitor1.enterWhen(new Guard() {}, true); // Thread 1
        monitor2.leave(); // Thread 2
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor = new Monitor();
        monitor.enterWhenUninterruptibly(new Guard() {});
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptiblyWithSatisfiedCondition() {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        monitor.enterWhenUninterruptibly(guard);
        monitor.leave();
    }

    @Test
    public void testEnterWhenWithTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        long start = System.currentTimeMillis();
        boolean result = monitor.enterWhen(new Guard() {}, 1, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(result);
        assertTrue(elapsed >= 1);
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenWithTimeInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread interruptThread = new Thread(() -> monitor.enter());
        interruptThread.start();
        monitor.enterWhen(new Guard() {}, 1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testEnterWhenWithSatisfiedConditionAndTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };

        long start = System.currentTimeMillis();
        boolean result = monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(result);
        assertTrue(elapsed >= 1);
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenWithSatisfiedConditionAndTimeInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {};
        Thread interruptThread = new Thread(() -> monitor.enter());
        interruptThread.start();
        monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        monitor.enter();
        monitor.enterIf(guard);
        monitor.leave();
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        monitor.enter();
        monitor.enterIfInterruptibly(guard);
        monitor.leave();
    }

    @Test
    public void testEnterIfWithTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        long start = System.currentTimeMillis();
        monitor.enter();
        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        monitor.leave();
        monitor.leave();
        assertTrue(elapsed >= 1);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfWithTimeInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {};
        Thread interruptThread = new Thread(() -> monitor.enter());
        interruptThread.start();
        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testEnterIfWithSatisfiedCondition() {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        monitor.enter();
        monitor.enterIf(guard);
        monitor.leave();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfWithWrongMonitor() throws InterruptedException {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor1.leave();
        monitor1.enterIf(new Guard() {}, monitor2);
    }

    @Test
    public void testEnterIfInterruptiblyWithSatisfiedCondition() {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        monitor.enter();
        monitor.enterIfInterruptibly(guard);
        monitor.leave();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfInterruptiblyWithWrongMonitor() throws InterruptedException {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor1.leave();
        monitor1.enterIfInterruptibly(new Guard() {}, monitor2);
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
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor(true);
        assertTrue(monitor1.isFair());
        assertTrue(monitor2.isFair());
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
        Thread thread1 = new Thread(() -> monitor.enter());
        thread1.start();
        Thread thread2 = new Thread(() -> monitor.enter());
        thread2.start();
        assertEquals(2, monitor.getQueueLength());
        monitor.leave();
        monitor.leave();
        thread1.join();
        thread2.join();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Thread thread1 = new Thread(() -> monitor.enter());
        thread1.start();
        Thread thread2 = new Thread(() -> monitor.enter());
        thread2.start();
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        monitor.leave();
        thread1.join();
        thread2.join();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Thread thread1 = new Thread(() -> monitor.enter());
        thread1.start();
        Thread thread2 = new Thread(() -> monitor.enter());
        thread2.start();
        assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
        monitor.leave();
        monitor.leave();
        thread1.join();
        thread2.join();
        assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
    }

    @Test
    public void testWaitForWithSatisfiedCondition() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread interruptThread = new Thread(() -> monitor.enter());
        interruptThread.start();
        monitor.waitFor(new Guard() {});
    }

    @Test
    public void testWaitForWithSatisfiedConditionAndTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        long start = System.currentTimeMillis();
        monitor.enter();
        monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        monitor.leave();
        monitor.leave();
        assertTrue(elapsed >= 1);
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterruptedWithTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread interruptThread = new Thread(() -> monitor.enter());
        interruptThread.start();
        monitor.waitFor(new Guard() {}, 1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testWaitForUninterruptiblyWithSatisfiedCondition() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptiblyWithSatisfiedConditionAndTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new Guard() {
            boolean satisfied = true;

            @Override
            public boolean isSatisfied() {
                return satisfied;
            }
        };
        long start = System.currentTimeMillis();
        monitor.enter();
        monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        monitor.leave();
        monitor.leave();
        assertTrue(elapsed >= 1);
    }
}