package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertNull(monitor.activeGuards);
        assertNull(fairMonitor.activeGuards);
        assertTrue(monitor.isFair());
        assertFalse(fairMonitor.isFair());
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        assertTrue(lock.tryLock());
        monitor.enter();
        assertTrue(lock.isHeldByCurrentThread());
        monitor.leave();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        Thread thread = Thread.currentThread();
        thread.interrupt();
        monitor.enterInterruptibly();
    }

    @Test
    public void testEnterInterruptiblyDoesNotThrowInterruptedExceptionWhenNotInterrupted() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        monitor.enterInterruptibly();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithTimeoutThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        Thread thread = Thread.currentThread();
        thread.interrupt();
        assertFalse(lock.tryLock(1, TimeUnit.MILLISECONDS));
        monitor.enter(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testEnterWithTimeoutDoesNotThrowInterruptedExceptionWhenNotInterrupted() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        monitor.enter(1, TimeUnit.MILLISECONDS);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterWhen(guard);
        Thread thread = Thread.currentThread();
        thread.interrupt();
    }

    @Test
    public void testEnterWhenDoesNotThrowInterruptedExceptionWhenNotInterrupted() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterWhen(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenWithTimeoutThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
        Thread thread = Thread.currentThread();
        thread.interrupt();
    }

    @Test
    public void testEnterWhenWithTimeoutDoesNotThrowInterruptedExceptionWhenNotInterrupted() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenWithInvalidGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(new Object());
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterIf(guard);
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.enterIf(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfInterruptiblyThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterIfInterruptibly(guard);
        Thread thread = Thread.currentThread();
        thread.interrupt();
    }

    @Test
    public void testEnterIfInterruptiblyDoesNotThrowInterruptedExceptionWhenNotInterrupted() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterIfInterruptibly(guard);
    }

    @Test
    public void testEnterIfWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfWithTimeoutThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
        Thread thread = Thread.currentThread();
        thread.interrupt();
    }

    @Test
    public void testEnterIfWithTimeoutDoesNotThrowInterruptedExceptionWhenNotInterrupted() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfWithInvalidGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(new Object());
        monitor.enterIf(guard);
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        assertTrue(lock.tryLock());
        monitor.enter();
        assertFalse(lock.tryLock());
        monitor.leave();
        assertTrue(lock.tryLock());
    }

    @Test(expected = InterruptedException.class)
    public void testTryEnterInterruptiblyThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        Thread thread = Thread.currentThread();
        thread.interrupt();
        assertFalse(lock.tryLockInterruptibly());
    }

    @Test
    public void testTryEnterInterruptiblyDoesNotThrowInterruptedExceptionWhenNotInterrupted() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        assertTrue(lock.tryLockInterruptibly());
    }

    @Test(expected = InterruptedException.class)
    public void testTryEnterWithTimeoutThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        Thread thread = Thread.currentThread();
        thread.interrupt();
        assertFalse(lock.tryLock(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testTryEnterWithTimeoutDoesNotThrowInterruptedExceptionWhenNotInterrupted() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        assertTrue(lock.tryLock(1, TimeUnit.MILLISECONDS));
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.waitFor(guard);
        Thread thread = Thread.currentThread();
        thread.interrupt();
    }

    @Test
    public void testWaitForDoesNotWaitWhenSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.waitFor(guard);
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testWaitForUninterruptiblyDoesNotWaitWhenSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        monitor.waitForUninterruptibly(guard);
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        boolean result = monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        assertFalse(guard.isSatisfied());
        assertTrue(result);
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor(monitor);
        long deadline = System.currentTimeMillis() + 1000;
        boolean result = monitor.waitForUninterruptibly(guard, 1, TimeUnit.SECONDS);
        assertFalse(guard.isSatisfied());
        assertFalse(result);
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        assertTrue(lock.tryLock());
        monitor.enter();
        monitor.leave();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertTrue(monitor.isFair());
        assertFalse(fairMonitor.isFair());
    }

    @Test
    public void testIsOccupied() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() {
        Monitor monitor = new Monitor();
        Lock lock = monitor.lock;
        monitor.enter();
        monitor.enter();
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
        monitor.enter();
        monitor.leave();
        assertEquals(1, monitor.getOccupiedDepth());
        monitor.enter();
        monitor.leave();
        assertEquals(1, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.waitFor(guard1);
        monitor.waitFor(guard2);
        assertEquals(2, monitor.getQueueLength(guard1));
        assertEquals(2, monitor.getQueueLength(guard2));
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor(monitor);
        Guard guard2 = new GuardMonitor(monitor);
        monitor.waitFor(guard1);
        monitor.waitFor(guard2);
        assertTrue(monitor.hasQueuedThreads(guard1));
        assertTrue(monitor.hasQueuedThreads(guard2));
    }

    @Test
    public void testIsSatisfied() {
        Guard guard = new GuardMonitor(new Object());
        assertTrue(guard.isSatisfied());
    }

    static class GuardMonitor extends Monitor.Guard {
        private final Object monitor;

        GuardMonitor(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }

        @Override
        public String toString() {
            return "GuardMonitor";
        }
    }
}