package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import org.junit.Assume;
import org.junit.Test;

import static org.junit.Assert.*;

public class MonitorTest {

    @Test
    public void testEnterAndLeave() {
        Monitor monitor = new Monitor();
        try {
            monitor.enter();
            monitor.leave();
            assertEquals(0, monitor.getOccupiedDepth());
        } finally {
            monitor.leave();
        }
    }

    @Test
    public void testEnterAndLeaveInterruptibly() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly();
                fail("Expected InterruptedException");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        monitor.enter();
        thread.start();
        monitor.leave();
        assertTrue(thread.isInterrupted());
    }

    @Test
    public void testEnterWithTimeout() {
        Monitor monitor = new Monitor();
        long timeoutNanos = 1;
        boolean acquired = monitor.enter(timeoutNanos, TimeUnit.NANOSECONDS);
        assertFalse(acquired);
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testEnterWithTimeoutInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeoutNanos = 1;
        assertTrue(monitor.enterInterruptibly(timeoutNanos, TimeUnit.NANOSECONDS));
        assertEquals(1, monitor.getOccupiedDepth());
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        boolean acquired = monitor.tryEnter();
        assertFalse(acquired);
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterWhenConditionIsSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        boolean acquired = monitor.enterWhen(guard);
        assertTrue(acquired);
        assertEquals(1, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterWhenConditionIsNotSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = notExpectedCondition();
        boolean acquired = monitor.enterWhen(guard);
        assertFalse(acquired);
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        boolean acquired = monitor.enterWhen(guard, 1, TimeUnit.NANOSECONDS);
        assertFalse(acquired);
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterWithTimeoutInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        boolean acquired = monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.NANOSECONDS);
        assertTrue(acquired);
        assertEquals(1, monitor.getOccupiedDepth());
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        boolean acquired = monitor.enterIf(guard);
        assertTrue(acquired);
        assertEquals(1, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterIfWithConditionNotSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = notExpectedCondition();
        boolean acquired = monitor.enterIf(guard);
        assertFalse(acquired);
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterIfWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        boolean acquired = monitor.enterIf(guard, 1, TimeUnit.NANOSECONDS);
        assertFalse(acquired);
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        boolean acquired = monitor.enterIfInterruptibly(guard);
        assertTrue(acquired);
        assertEquals(1, monitor.getOccupiedDepth());
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testTryEnterIfInterruptiblyWithConditionNotSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = notExpectedCondition();
        boolean acquired = monitor.enterIfInterruptibly(guard);
        assertFalse(acquired);
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testWaitForCondition() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testWaitForConditionUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        monitor.enter();
        Monitor monitor2 = new Monitor();
        monitor2.enter();
        monitor.waitForUninterruptibly(guard);
        monitor2.leave();
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testWaitForConditionWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        boolean waited = monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertTrue(waited || guard.isSatisfied());
    }

    @Test
    public void testWaitForConditionWithTimeoutUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = expectedCondition();
        boolean waited = monitor.waitForUninterruptibly(guard, 1, TimeUnit.SECONDS);
        monitor.leave();
        assertTrue(waited && guard.isSatisfied());
    }

    @Test
    public void testLeaveWhenNotLocked() {
        Monitor monitor = new Monitor();
        try {
            monitor.leave();
            fail("Expected IllegalMonitorStateException");
        } catch (IllegalMonitorStateException e) {
            // Expected
        }
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor(true);
        Monitor monitor2 = new Monitor(false);
        assertTrue(monitor.isFair());
        assertFalse(monitor2.isFair());
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
        Monitor monitor2 = new Monitor();
        monitor.enter();
        monitor2.enter();
        assertEquals(0, monitor.getQueueLength());
        Guard guard = new MonitorGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
        assertEquals(1, monitor2.getQueueLength());
        monitor2.leave();
        assertEquals(0, monitor2.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor.enter();
        monitor2.enter();
        Guard guard = new MonitorGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
        assertTrue(monitor2.hasQueuedThreads(guard));
        monitor2.leave();
        assertFalse(monitor2.hasQueuedThreads(guard));
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor.enter();
        monitor2.enter();
        Guard guard = new MonitorGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
        assertTrue(monitor2.hasQueuedThread(Thread.currentThread()));
        monitor2.leave();
        assertFalse(monitor2.hasQueuedThread(Thread.currentThread()));
    }

    private static Guard expectedCondition() {
        return new MonitorGuard(true);
    }

    private static Guard notExpectedCondition() {
        return new MonitorGuard(false);
    }

    private static class MonitorGuard extends Guard {
        private final boolean expected;

        public MonitorGuard(boolean expected) {
            super(null);
            this.expected = expected;
        }

        @Override
        public boolean isSatisfied() {
            Assume.assumeTrue(expected);
            return true;
        }
    }
}