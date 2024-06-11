package com.example.monitor;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.util.concurrent.Monitor;
import com.google.common.base.Preconditions;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

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

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongMonitor() {
        Monitor monitor = new Monitor();
        Monitor wrongMonitor = new Monitor();
        monitor.enter();
        wrongMonitor.enter();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.enterInterruptibly());
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWithTimeout() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enter(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                fail("Interrupted exception should not be thrown");
            }
        });
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS));
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.tryEnter());
        assertFalse(monitor.tryEnter());
        monitor.leave();
        assertTrue(monitor.tryEnter());
    }

    @Test
    public void testTryEnterWithGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithSatisfiedCondition(monitor);
        assertTrue(monitor.tryEnterWhen(guard));
        assertFalse(monitor.tryEnterWhen(guard));
        monitor.leave();
        assertTrue(monitor.tryEnterWhen(guard));
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongGuard() {
        Monitor monitor = new Monitor();
        Guard wrongGuard = new GuardWithSatisfiedCondition(new Monitor());
        monitor.enterWhen(wrongGuard);
    }

    @Test
    public void testEnterWhenUninterruptiblyWithGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithSatisfiedCondition(monitor);
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenWithTimeoutAndGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithUnsatisfiedCondition(monitor);
        boolean entered = monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(entered);
        monitor.leave();
        assertTrue(monitor.enterWhen(guard));
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeoutAndGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithUnsatisfiedCondition(monitor);
        monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.isFair());
        Monitor unfairMonitor = new Monitor(false);
        assertFalse(unfairMonitor.isFair());
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
        Thread thread1 = new Thread(() -> monitor.enter());
        Thread thread2 = new Thread(() -> monitor.enter());
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail("Threads should not be interrupted");
        }
        assertEquals(2, monitor.getQueueLength());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThreads());
        Thread thread = new Thread(() -> monitor.enter());
        monitor.waitFor(thread.getMonitorGuard());
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.enter());
        assertFalse(monitor.hasQueuedThread(thread));
        monitor.waitFor(thread.getMonitorGuard());
        assertTrue(monitor.hasQueuedThread(thread));
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.waitFor(thread.getMonitorGuard()));
        monitor.enter();
        thread.join();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.waitForUninterruptibly(thread.getMonitorGuard()));
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        thread.join();
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.waitFor(thread.getMonitorGuard(), 1, TimeUnit.MILLISECONDS));
        monitor.enter();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.waitForUninterruptibly(thread.getMonitorGuard(), 1, TimeUnit.MILLISECONDS));
        monitor.enter();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        monitor.enter();
        thread.join();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    static class GuardWithSatisfiedCondition extends Monitor.Guard {
        Monitor monitor;

        public GuardWithSatisfiedCondition(Monitor monitor) {
            super(monitor);
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }
    }

    static class GuardWithUnsatisfiedCondition extends Monitor.Guard {
        Monitor monitor;

        public GuardWithUnsatisfiedCondition(Monitor monitor) {
            super(monitor);
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }
    }
}