package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    private Monitor monitor;
    private ReentrantLock lock;
    private Condition condition;
    private Guard guard;

    @Before
    public void setup() {
        monitor = new Monitor();
        lock = monitor.lock;
        condition = lock.newCondition();
        guard = new MyGuard(monitor);
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWrongMonitor() {
        Monitor anotherMonitor = new Monitor();
        anotherMonitor.enter();
        monitor.enter();
        monitor.enterWhen(guard); // should throw exception
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterInterruptiblyWithInterruption() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly();
                fail("should have been interrupted");
            } catch (InterruptedException e) {
                // expected
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterWithTimeout() throws InterruptedException, TimeoutException {
        final long timeout = 1;
        boolean entered = monitor.enterWhen(guard, timeout, TimeUnit.SECONDS);
        assertTrue(entered || monitor.isOccupied());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithTimeoutInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
                fail("should have been interrupted");
            } catch (InterruptedException e) {
                // expected
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        final long timeout = 1;
        monitor.enterInterruptibly(timeout, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithTimeoutInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly(1, TimeUnit.SECONDS);
                fail("should have been interrupted");
            } catch (InterruptedException e) {
                // expected
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testTryEnter() {
        assertFalse(monitor.tryEnter());
        monitor.enter();
        assertTrue(monitor.tryEnter());
    }

    @Test
    public void testTryEnterWrongMonitor() {
        Monitor anotherMonitor = new Monitor();
        assertFalse(anotherMonitor.tryEnter());
        monitor.enter();
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testTryEnterInterruptibly() {
        assertFalse(monitor.tryEnterInterruptibly());
        monitor.enter();
        assertTrue(monitor.tryEnterInterruptibly());
    }

    @Test
    public void testTryEnterInterruptiblyWrongMonitor() {
        Monitor anotherMonitor = new Monitor();
        assertFalse(anotherMonitor.tryEnterInterruptibly());
        monitor.enter();
        assertFalse(monitor.tryEnterInterruptibly());
    }

    @Test
    public void testTryEnterIf() {
        monitor.enter();
        assertTrue(monitor.tryEnterIf(guard));
        monitor.leave();
        assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testTryEnterIfInterruptibly() throws InterruptedException {
        monitor.enter();
        monitor.enterWhen(guard); // should block
        monitor.leave();
        assertTrue(monitor.tryEnterIfInterruptibly(guard));
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testTryEnterIfInterruptiblyWrongMonitor() throws InterruptedException {
        Monitor anotherMonitor = new Monitor();
        monitor.enter();
        monitor.enterWhen(guard); // should block
        anotherMonitor.enter();
        assertFalse(anotherMonitor.tryEnterIfInterruptibly(guard));
    }

    @Test
    public void testTryEnterIfWithTimeout() {
        monitor.enter();
        assertTrue(monitor.tryEnterIf(guard, 1, TimeUnit.MILLISECONDS));
        monitor.leave();
        assertFalse(monitor.tryEnterIf(guard, 1, TimeUnit.MILLISECONDS));
    }

    @Test(expected = TimeoutException.class)
    public void testTryEnterIfWithTimeoutTimeout() {
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.tryEnterIf(guard, 0, TimeUnit.MILLISECONDS));
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhen() throws InterruptedException {
        monitor.enterWhen(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenWrongMonitor() throws InterruptedException {
        Monitor anotherMonitor = new Monitor();
        monitor.enter();
        anotherMonitor.enter();
        anotherMonitor.enterWhen(guard); // should throw exception
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenUninterruptiblyWrongMonitor() {
        Monitor anotherMonitor = new Monitor();
        monitor.enter();
        anotherMonitor.enter();
        anotherMonitor.enterWhenUninterruptibly(guard); // should throw exception
    }

    @Test
    public void testEnterWhenWithTimeout() throws InterruptedException {
        final long timeout = 1;
        monitor.enterWhen(guard, timeout, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenWithTimeoutInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
                fail("should have been interrupted");
            } catch (InterruptedException e) {
                // expected
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeout() throws InterruptedException {
        final long timeout = 1;
        monitor.enterWhenUninterruptibly(guard, timeout, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenUninterruptiblyWithTimeoutInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.SECONDS);
                fail("should have been interrupted");
            } catch (InterruptedException e) {
                // expected
            }
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testIsFair() {
        Monitor monitorFair = new Monitor(true);
        Monitor monitorNotFair = new Monitor();
        assertEquals(monitorFair.isFair(), true);
        assertEquals(monitorNotFair.isFair(), false);
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
        assertEquals(monitor.getOccupiedDepth(), 2);
        monitor.leave();
        monitor.leave();
        assertEquals(monitor.getOccupiedDepth(), 0);
    }

    @Test
    public void testGetQueueLength() {
        monitor.enter();
        monitor.enter();
        Thread thread = new Thread(() -> {
            monitor.enter();
            monitor.enter();
        });
        thread.start();
        thread.join();
        assertEquals(monitor.getQueueLength(), 2);
        monitor.leave();
        monitor.leave();
        monitor.leave();
        assertEquals(monitor.getQueueLength(), 0);
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enter();
        monitor.enter();
        Thread thread = new Thread(() -> {
            monitor.enter();
            monitor.enter();
        });
        thread.start();
        thread.join();
        assertTrue(monitor.hasQueuedThreads(guard));
        monitor.leave();
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads(guard));
    }

    @Test
    public void testHasQueuedThread() {
        monitor.enter();
        monitor.enter();
        Thread thread = new Thread(() -> {
            monitor.enter();
            monitor.enter();
        });
        thread.start();
        thread.join();
        assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
        monitor.leave();
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        monitor.enter();
        monitor.waitFor(guard);
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptibly() {
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException, TimeoutException {
        final long timeout = 1;
        monitor.enter();
        monitor.waitFor(guard, timeout, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
    }

    @Test(expected = TimeoutException.class)
    public void testWaitForWithTimeoutTimeout() throws InterruptedException, TimeoutException {
        monitor.enter();
        monitor.waitFor(guard, 0, TimeUnit.MILLISECONDS);
        fail("should have been interrupted");
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            monitor.enter();
            monitor.waitFor(guard);
            fail("should have been interrupted");
        });
        thread.start();
        thread.join();
    }

    @Test
    public void testSignalNextWaiter() {
        monitor.enter();
        monitor.enter();
        Thread thread1 = new Thread(() -> {
            monitor.enter();
            monitor.signalNextWaiter();
            monitor.leave();
        });
        Thread thread2 = new Thread(() -> {
            monitor.enter();
            monitor.waitFor(guard);
            monitor.leave();
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        monitor.leave();
    }

    @Test
    public void testIsSatisfied() {
        guard.isSatisfied = () -> true;
        assertTrue(monitor.isSatisfied(guard));
    }

    @Test
    public void testIsSatisfiedFalse() {
        guard.isSatisfied = () -> false;
        assertFalse(monitor.isSatisfied(guard));
    }

    static class MyGuard extends Guard {
        public MyGuard(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }
    }

}