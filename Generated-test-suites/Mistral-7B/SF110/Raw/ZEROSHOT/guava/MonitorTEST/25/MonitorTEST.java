package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;

public class MonitorTest {

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        assertNull(monitor.activeGuards);
        assertTrue(monitor.isFair());
        Monitor fairMonitor = new Monitor(true);
        assertTrue(fairMonitor.isFair());
        Monitor unfairMonitor = new Monitor(false);
        assertFalse(unfairMonitor.isFair());
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        try {
            monitor.enter();
            assertTrue(monitor.isOccupiedByCurrentThread());
        } finally {
            monitor.leave();
            assertFalse(monitor.isOccupiedByCurrentThread());
        }
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterLockByAnotherThread() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.enter());
        monitor.enter();
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        try {
            monitor.enterInterruptibly();
            assertTrue(monitor.isOccupiedByCurrentThread());
        } finally {
            monitor.leave();
            assertFalse(monitor.isOccupiedByCurrentThread());
        }
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.enterInterruptibly());
        monitor.enter();
        monitor.interrupt();
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterWithTimeOut() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        boolean entered = monitor.enter(1, TimeUnit.SECONDS);
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testEnterWithTimeOutInterrupted() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.enter(1, TimeUnit.SECONDS));
        monitor.enter();
        monitor.interrupt();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithTimeOutInterruptedDuringEntry() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        monitor.enter();
        Thread thread = new Thread(() -> monitor.enter(1, TimeUnit.SECONDS));
        monitor.interrupt();
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterWithGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithGuardFromAnotherThread() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        Thread thread = new Thread(() -> monitor.enterWhen(guard));
        monitor.enter();
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterWithGuardUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndTimeOut() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        boolean entered = monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testEnterWithGuardAndTimeOutInterrupted() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        Thread thread = new Thread(() -> monitor.enterWhen(guard, 1, TimeUnit.SECONDS));
        monitor.enter();
        monitor.interrupt();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enterIf(guard);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfFromAnotherThread() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        Thread thread = new Thread(() -> monitor.enterIf(guard));
        monitor.enter();
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterIfUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enterIfUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeOut() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        boolean entered = monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testEnterIfWithTimeOutInterrupted() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        Thread thread = new Thread(() -> monitor.enterIf(guard, 1, TimeUnit.SECONDS));
        monitor.enter();
        monitor.interrupt();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeOutInterruptedDuringEntry() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enter();
        Thread thread = new Thread(() -> monitor.enterIf(guard, 1, TimeUnit.SECONDS));
        monitor.interrupt();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        boolean entered = monitor.tryEnter();
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testTryEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        boolean entered = monitor.tryEnterInterruptibly();
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testTryEnterWithTimeOut() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        boolean entered = monitor.tryEnter(1, TimeUnit.SECONDS);
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testTryEnterWithTimeOutInterrupted() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.tryEnter(1, TimeUnit.SECONDS));
        monitor.enter();
        monitor.interrupt();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterWithTimeOutInterruptedDuringEntry() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        monitor.enter();
        Thread thread = new Thread(() -> monitor.tryEnter(1, TimeUnit.SECONDS));
        monitor.interrupt();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        boolean entered = monitor.tryEnterIf(guard);
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testTryEnterIfUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        boolean entered = monitor.tryEnterIfUninterruptibly(guard);
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testTryEnterIfWithTimeOut() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        boolean entered = monitor.tryEnterIf(guard, 1, TimeUnit.SECONDS);
        assertFalse(entered);
        monitor.leave();
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enter();
        monitor.waitFor(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testWaitForFromAnotherThread() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        Thread thread = new Thread(() -> monitor.waitFor(guard));
        monitor.enter();
        thread.start();
        thread.join();
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeOut() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        long time = 100;
        boolean waited = monitor.waitFor(guard, time, TimeUnit.MILLISECONDS);
        assertFalse(waited);
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeOutInterrupted() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enter();
        monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        monitor.interrupt();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeOutInterruptedDuringWait() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enter();
        Thread thread = new Thread(() -> monitor.waitFor(guard, 1, TimeUnit.SECONDS));
        monitor.interrupt();
        thread.start();
        thread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
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
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        assertEquals(0, monitor.getQueueLength());
        Monitor monitorWithThreads = new Monitor();
        new Thread(() -> monitorWithThreads.enter()).start();
        new Thread(() -> monitorWithThreads.enter()).start();
        assertEquals(2, monitorWithThreads.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThreads());
        Monitor monitorWithThreads = new Monitor();
        new Thread(() -> monitorWithThreads.enter()).start();
        new Thread(() -> monitorWithThreads.enter()).start();
        assertTrue(monitorWithThreads.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
        Monitor monitorWithThreads = new Monitor();
        new Thread(() -> monitorWithThreads.enter()).start();
        new Thread(() -> monitorWithThreads.enter()).start();
        Thread thread = monitorWithThreads.enter();
        assertTrue(monitorWithThreads.hasQueuedThread(thread));
    }

    @Test
    public void testHasWaiters() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard();
        monitor.enter();
        monitor.waitFor(guard);
        assertTrue(monitor.hasWaiters(guard));
        monitor.leave();
        assertFalse(monitor.hasWaiters(guard));
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.isFair());
        Monitor fairMonitor = new Monitor(true);
        assertTrue(fairMonitor.isFair());
        Monitor unfairMonitor = new Monitor(false);
        assertFalse(unfairMonitor.isFair());
    }

    static class MyGuard implements Guard {
        @Override
        public boolean isSatisfied() {
            return false;
        }
    }
}