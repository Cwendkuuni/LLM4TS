package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;

@Beta
public final class MonitorTest {

    @Test
    public void testMonitorCreation() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertNull(monitor.activeGuards);
        assertNull(fairMonitor.activeGuards);
        assertTrue(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertFalse(monitor.isFair());
    }

    @Test
    public void testEnterAndLeave() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertNull(guard.condition.getQueue());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterInterruptibly();
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertNull(guard.condition.getQueue());
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterWhen(guard1);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertNull(guard1.condition.getQueue());
        assertNull(guard2.condition.getQueue());
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterWhenUninterruptibly(guard1);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertNull(guard1.condition.getQueue());
        assertNull(guard2.condition.getQueue());
    }

    @Test
    public void testEnterWhenWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterWhen(guard1, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertNull(guard1.condition.getQueue());
        assertNull(guard2.condition.getQueue());
        verify(guard1).waitFor(anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterWhenUninterruptibly(guard1, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertNull(guard1.condition.getQueue());
        assertNull(guard2.condition.getQueue());
        verify(guard1).awaitUninterruptibly(anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    public void testEnterIf() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterIf(guard1);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertFalse(guard1.isSatisfied());
        assertFalse(guard2.isSatisfied());
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterIfInterruptibly(guard1);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertFalse(guard1.isSatisfied());
        assertFalse(guard2.isSatisfied());
    }

    @Test
    public void testEnterIfWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterIf(guard1, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertFalse(guard1.isSatisfied());
        assertFalse(guard2.isSatisfied());
        verify(guard1).waitFor(anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    public void testEnterIfUninterruptiblyWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterIfUninterruptibly(guard1, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertFalse(guard1.isSatisfied());
        assertFalse(guard2.isSatisfied());
        verify(guard1).awaitUninterruptibly(anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    public void testEnterIfWithSatisfiedGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard satisfiedGuard = new SatisfiedGuardMonitor(monitor);
        monitor.enter();
        monitor.enterIf(satisfiedGuard);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(satisfiedGuard.isSatisfied());
    }

    @Test
    public void testTryEnter() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        assertFalse(monitor.tryEnter());
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertFalse(guard1.isSatisfied());
        assertFalse(guard2.isSatisfied());
    }

    @Test
    public void testTryEnterWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        assertFalse(monitor.tryEnter(1, TimeUnit.SECONDS));
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertFalse(guard1.isSatisfied());
        assertFalse(guard2.isSatisfied());
    }

    @Test
    public void testAwaitNanos() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        long timeoutNanos = 10L;
        monitor.enter();
        monitor.awaitNanos(guard, timeoutNanos, true);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(guard.condition.getQueue().isEmpty());
        verify(guard.condition, times(1)).awaitNanos(eq(timeoutNanos));
    }

    @Test
    public void testAwaitNanosInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        long timeoutNanos = 10L;
        monitor.enter();
        monitor.awaitNanos(guard, timeoutNanos, false);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(guard.condition.getQueue().isEmpty());
        verify(guard.condition, times(1)).awaitNanos(eq(timeoutNanos));
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        long timeoutMilliseconds = 100L;
        monitor.enter();
        monitor.waitFor(guard, timeoutMilliseconds, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(guard.condition.getQueue().isEmpty());
        verify(guard.condition, times(1)).await(eq(timeoutMilliseconds), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        long timeoutMilliseconds = 100L;
        monitor.enter();
        monitor.waitForUninterruptibly(guard, timeoutMilliseconds, true);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(guard.condition.getQueue().isEmpty());
        verify(guard.condition, times(1)).awaitUninterruptibly(eq(timeoutMilliseconds), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testSignalNextWaiter() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.await(guard1, 1, TimeUnit.SECONDS);
        monitor.signalNextWaiter();
        monitor.await(guard2, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertFalse(guard1.isSatisfied());
        assertTrue(guard2.isSatisfied());
    }

    @Test
    public void testIsFair() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor(true);
        assertTrue(monitor1.isFair());
        assertFalse(monitor2.isFair());
    }

    @Test
    public void testIsOccupied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enter();
        assertTrue(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsOccupiedByCurrentThread() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enter();
        Thread thread = Thread.currentThread();
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupied());
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.enterWhen(guard1);
        monitor.enterWhen(guard2);
        monitor.leave();
        monitor.leave();
        assertEquals(2, monitor.getOccupiedDepth());
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetQueueLength() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.await(guard1, 1, TimeUnit.SECONDS);
        monitor.signalNextWaiter();
        monitor.await(guard2, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertEquals(1, monitor.getQueueLength(guard1));
        assertEquals(0, monitor.getQueueLength(guard2));
    }

    @Test
    public void testHasQueuedThreads() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.await(guard1, 1, TimeUnit.SECONDS);
        monitor.signalNextWaiter();
        monitor.await(guard2, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertTrue(monitor.hasQueuedThreads(guard1));
        assertFalse(monitor.hasQueuedThreads(guard2));
    }

    @Test
    public void testHasQueuedThread() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.enter();
        monitor.await(guard1, 1, TimeUnit.SECONDS);
        monitor.signalNextWaiter();
        monitor.await(guard2, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
        assertFalse(monitor.hasQueuedThread(new Thread("DummyThread")));
    }

    @Test
    public void testWaitForConditionWithSatisfiedGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new SatisfiedGuardMonitor(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
        assertFalse(monitor.isOccupied());
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(guard.isSatisfied());
    }

    private static class GuardMonitor implements Guard {
        private final Monitor monitor;

        public GuardMonitor(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }

        @Override
        public Condition condition() {
            return this.monitor.lock.newCondition();
        }
    }

    private static class SatisfiedGuardMonitor extends GuardMonitor {
        public SatisfiedGuardMonitor(Monitor monitor) {
            super(monitor);
            this.isSatisfied = true;
        }

        private boolean isSatisfied;

        @Override
        public boolean isSatisfied() {
            return this.isSatisfied;
        }
    }
}