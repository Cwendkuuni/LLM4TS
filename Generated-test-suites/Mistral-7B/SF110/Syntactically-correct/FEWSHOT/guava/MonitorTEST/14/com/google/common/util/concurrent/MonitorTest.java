package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;
import org.junit.Test;

@Beta
public class MonitorTest {

    @Test
    public void testMonitorCreation() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertNull(monitor.getActiveGuards());
        assertNull(fairMonitor.getActiveGuards());
        assertFalse(monitor.isFair());
        assertTrue(fairMonitor.isFair());
    }

    @Test
    public void testEnterAndLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.lock.isLocked());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor2.enter();
        monitor1.leave();
        monitor1.enter();
        monitor1.leave(); // should throw exception
        monitor2.leave();
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.tryEnter());
        monitor.leave();
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        monitor.leave();
        monitor.enterInterruptibly();
        assertFalse(monitor.lock.tryLockInterruptibly());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        Thread.currentThread().interrupt();
        monitor.enterInterruptibly();
    }

    @Test
    public void testEnterWithGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, true);
        monitor.enterWhen(guard);
        monitor.leave();
        monitor.enterWhen(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardWithValue(monitor, true);
        Monitor monitor2 = new Monitor();
        monitor.enterWhen(guard1);
        monitor1.leave();
        monitor.enterWhen(guard2 = new GuardWithValue(monitor2, false)); // should throw exception
        monitor2.leave();
    }

    @Test
    public void testTryEnterWithGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, true);
        assertTrue(monitor.tryEnterIf(guard));
        monitor.leave();
        assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, false);
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, false);
        monitor.enter();
        Thread.currentThread().interrupt();
        monitor.waitFor(guard);
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, false);
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, false);
        long timeout = 1L;
        boolean result = monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        assertFalse(result);
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, false);
        long timeout = 1L;
        monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testAwaitNanos() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, false);
        long timeout = 1L;
        boolean result = monitor.awaitNanos(guard, timeout);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        assertFalse(result);
    }

    @Test
    public void testAwaitNanosInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, false);
        long timeout = 1L;
        monitor.enter();
        monitor.awaitNanosInterruptibly(guard, timeout);
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testAwaitNanosInterruptiblyTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithValue(monitor, false);
        long timeout = 1L;
        monitor.enter();
        Monitor monitor2 = new Monitor();
        monitor2.enter();
        monitor.awaitNanosInterruptibly(guard, timeout);
        monitor2.leave();
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.isFair());
        Monitor fairMonitor = new Monitor(true);
        assertTrue(fairMonitor.isFair());
        Monitor notFairMonitor = new Monitor(false);
        assertFalse(notFairMonitor.isFair());
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
        monitor.enter();
        assertEquals(3, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardWithValue(monitor, true);
        Guard guard2 = new GuardWithValue(monitor, true);
        monitor.enter();
        monitor.enterWhen(guard1);
        monitor.leave();
        monitor.enterWhen(guard2);
        monitor.leave();
        assertEquals(1, monitor.getQueueLength());
        monitor.enter();
        monitor.enterWhen(guard1);
        monitor.leave();
        monitor.enterWhen(guard2);
        monitor.leave();
        assertEquals(2, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardWithValue(monitor, true);
        Monitor monitor2 = new Monitor();
        monitor.enter();
        monitor.enterWhen(guard1);
        monitor.leave();
        monitor2.enter();
        monitor2.enterWhen(guard1);
        monitor2.leave();
        assertTrue(monitor.hasQueuedThreads(guard1));
        assertFalse(monitor2.hasQueuedThreads(guard1));
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Thread thread1 = new Thread(() -> {});
        Thread thread2 = new Thread(() -> {});
        monitor.enter();
        monitor.enter();
        monitor.enter();
        monitor.leave();
        monitor.enterWhen(new GuardWithThread(monitor, thread1));
        monitor.leave();
        assertTrue(monitor.hasQueuedThread(thread1));
        assertFalse(monitor.hasQueuedThread(thread2));
    }

    static class GuardWithValue extends Guard {
        private final boolean satisfied;

        public GuardWithValue(Monitor monitor, boolean satisfied) {
            super(monitor);
            this.satisfied = satisfied;
        }

        @Override
        public boolean isSatisfied() {
            return satisfied;
        }
    }

    static class GuardWithThread extends Guard {
        private final Thread thread;

        public GuardWithThread(Monitor monitor, Thread thread) {
            super(monitor);
            this.thread = thread;
        }

        @Override
        public boolean isSatisfied() {
            return thread.isInterrupted();
        }
    }
}