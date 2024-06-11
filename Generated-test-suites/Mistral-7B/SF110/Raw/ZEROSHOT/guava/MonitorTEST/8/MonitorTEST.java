package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.isOccupied());
        Monitor fairMonitor = new Monitor(true);
        assertFalse(fairMonitor.isOccupied());
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
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly();
        });
        monitor.enter();
        thread.start();
        thread.join();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterTimeout() throws TimeoutException, InterruptedException {
        Monitor monitor = new Monitor();
        long time = 1;
        long unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.enter(time, unit));
        monitor.enter();
        assertFalse(monitor.enter(time, unit));
        monitor.leave();
        monitor.enter();
        assertTrue(monitor.enter(time, unit));
        monitor.leave();
    }

    @Test
    public void testEnterInterruptiblyTimeout() throws TimeoutException, InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                fail("InterruptedException should not be thrown");
            }
        });
        monitor.enter();
        monitor.enter();
        thread.start();
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(thread.isInterrupted());
        monitor.leave();
        monitor.leave();
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        try {
            monitor.tryEnter();
            fail("Expected false");
        } catch (AssertionError e) {
            // Expected
        }
        monitor.enter();
        monitor.leave();
        assertTrue(monitor.tryEnter());
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        monitor.enterWhen(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testEnterWhenTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        long time = 1;
        long unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.enterWhen(guard, time, unit));
        monitor.enter();
        assertFalse(monitor.enterWhen(guard, time, unit));
        monitor.leave();
        monitor.enter();
        assertTrue(monitor.enterWhen(guard, time, unit));
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        long timeout = 1;
        long unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.enterWhenUninterruptibly(guard, timeout, unit));
        monitor.enter();
        monitor.enter();
        monitor.enterWhenUninterruptibly(guard, timeout, unit);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        monitor.leave();
        monitor.leave();
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        monitor.enter();
        assertTrue(monitor.enterIf(guard));
        monitor.leave();
        assertFalse(monitor.enterIf(guard));
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterIfInterruptibly(new MonitorTestGuard());
            } catch (InterruptedException e) {
                fail("InterruptedException should not be thrown");
            }
        });
        monitor.enter();
        monitor.enter();
        thread.start();
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(thread.isInterrupted());
        monitor.leave();
        monitor.leave();
    }

    @Test
    public void testEnterIfTimeout() throws TimeoutException, InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        long time = 1;
        long unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.enterIf(guard, time, unit));
        monitor.enter();
        monitor.enter();
        assertTrue(monitor.enterIf(guard, time, unit));
        monitor.leave();
        monitor.leave();
    }

    @Test
    public void testEnterIfInterruptiblyTimeout() throws TimeoutException, InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        long timeout = 1;
        long unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.enterIfInterruptibly(guard, timeout, unit));
        monitor.enter();
        monitor.enter();
        monitor.enterIfInterruptibly(guard, timeout, unit);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        monitor.leave();
        monitor.leave();
    }

    @Test
    public void testEnterIfTry() {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        monitor.enter();
        assertTrue(monitor.tryEnterIf(guard));
        monitor.leave();
        assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        monitor.enter();
        monitor.waitFor(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testWaitForTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        long time = 1;
        long unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.waitFor(guard, time, unit));
        monitor.enter();
        monitor.waitFor(guard, time, unit);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testWaitForUninterruptiblyTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MonitorTestGuard();
        long timeout = 1;
        long unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.waitForUninterruptibly(guard, timeout, unit));
        monitor.enter();
        monitor.enter();
        monitor.waitForUninterruptibly(guard, timeout, unit);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        monitor.leave();
        monitor.leave();
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
        assertTrue(monitor.isFair());
        Monitor fairMonitor = new Monitor(true);
        assertTrue(fairMonitor.isFair());
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
        MonitorTestGuard guard1 = new MonitorTestGuard();
        MonitorTestGuard guard2 = new MonitorTestGuard();
        monitor.enter();
        monitor.waitFor(guard1);
        monitor.enter();
        monitor.waitFor(guard2);
        monitor.leave();
        monitor.leave();
        assertEquals(2, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThreads(null));
        MonitorTestGuard guard1 = new MonitorTestGuard();
        MonitorTestGuard guard2 = new MonitorTestGuard();
        monitor.enter();
        monitor.waitFor(guard1);
        monitor.enter();
        monitor.waitFor(guard2);
        monitor.leave();
        monitor.leave();
        assertTrue(monitor.hasQueuedThreads(guard1));
        assertTrue(monitor.hasQueuedThreads(guard2));
    }

    @Test
    public void testIsSatisfied() {
        MonitorTestGuard guard = new MonitorTestGuard();
        assertTrue(guard.isSatisfied());
    }

    private static class MonitorTestGuard extends Monitor.Guard {
        private final Object monitor;

        public MonitorTestGuard(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }

        @Override
        public Monitor getMonitor() {
            return monitor;
        }
    }

}