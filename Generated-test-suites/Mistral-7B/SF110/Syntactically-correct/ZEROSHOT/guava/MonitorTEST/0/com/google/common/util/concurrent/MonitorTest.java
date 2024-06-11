package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    private Monitor monitor;
    private Guard guard;

    @Before
    public void setUp() {
        monitor = new Monitor();
        guard = new MyGuard(monitor);
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterInterruptiblyInterrupted() {
        monitor.enterInterruptibly();
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterInterruptiblyNotInterrupted() {
        monitor.enterInterruptibly();
        assertFalse(Thread.interrupted());
    }

    @Test
    public void testEnterWithTimeout() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithTimeoutInterrupted() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterWithTimeoutNotInterruptedAndNotSatisfied() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterWithTimeoutNotInterruptedAndSatisfied() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        guard.setSatisfied(true);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithTimeoutInterrupted() {
        monitor.enterInterruptibly(1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterInterruptiblyWithTimeoutNotInterrupted() {
        monitor.enterInterruptibly(1, TimeUnit.SECONDS);
        assertFalse(Thread.interrupted());
    }

    @Test
    public void testEnterInterruptiblyWithTimeoutNotSatisfied() {
        monitor.enterInterruptibly(1, TimeUnit.SECONDS);
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterInterruptiblyWithTimeoutSatisfied() {
        monitor.enterInterruptibly(1, TimeUnit.SECONDS);
        guard.setSatisfied(true);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testEnterWithGuardAndTimeout() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithGuardAndTimeoutInterrupted() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterWithGuardAndTimeoutNotSatisfiedAndNotInterrupted() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterWithGuardAndTimeoutSatisfiedAndNotInterrupted() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        guard.setSatisfied(true);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithGuardAndTimeoutInterruptedAndSatisfied() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        guard.setSatisfied(true);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterWithGuardAndTimeoutNotSatisfiedAndInterrupted() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterWithGuardAndTimeoutSatisfiedAndInterrupted() {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
        guard.setSatisfied(true);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testEnterIf() {
        monitor.enterIf(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfWithDifferentMonitor() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enterIf(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfWithDifferentMonitorInterrupted() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enterIf(guard);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterIfUninterruptibly() {
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfUninterruptiblyWithDifferentMonitor() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enterWhenUninterruptibly(guard);
    }

    @Test
    public void testEnterIfWithTimeout() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfWithTimeoutInterrupted() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterIfWithTimeoutNotSatisfied() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterIfWithTimeoutSatisfied() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        guard.setSatisfied(true);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testEnterIfWithTimeoutInterruptedAndSatisfied() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        guard.setSatisfied(true);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterIfWithTimeoutNotSatisfiedAndInterrupted() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterIfWithTimeoutSatisfiedAndInterrupted() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
        guard.setSatisfied(true);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testEnterIfWithTimeoutNotInterrupted() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        assertFalse(Thread.interrupted());
    }

    @Test
    public void testEnterIfWithTimeoutInterruptedButSatisfied() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
        guard.setSatisfied(true);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testEnterIfWithTimeoutNotSatisfiedAndNotInterrupted() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterIfWithTimeoutNotInterruptedButSatisfied() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        guard.setSatisfied(true);
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testIsFair() {
        Monitor mon1 = new Monitor();
        Monitor mon2 = new Monitor(true);
        assertFalse(mon1.isFair());
        assertTrue(mon2.isFair());
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
        assertEquals(2, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        monitor.enterWhen(guard);
        assertEquals(1, monitor.getQueueLength());
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enterWhen(guard);
        assertTrue(monitor.hasQueuedThreads(guard));
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads(guard));
    }

    @Test
    public void testHasQueuedThread() {
        Thread thread = new Thread(() -> {});
        monitor.enterWhen(guard);
        monitor.awaitUninterruptibly(guard, true);
        assertTrue(monitor.hasQueuedThread(thread));
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testWaitFor() {
        monitor.enter();
        monitor.waitFor(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterrupted() {
        monitor.enter();
        monitor.waitFor(guard);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testWaitForUninterruptibly() {
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeout() {
        monitor.enter();
        boolean result = monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        assertTrue(result || guard.isSatisfied());
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForWithTimeoutInterrupted() {
        monitor.enter();
        monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        monitor.enter();
        boolean result = monitor.waitForUninterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(result || guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    static class MyGuard extends Guard {
        public MyGuard(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }

        public void setSatisfied(boolean satisfied) {
            this.waiterCount = 0;
            super.isSatisfied = () -> satisfied;
        }
    }
}