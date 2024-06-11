package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    private Monitor monitor;
    private ReentrantLock mockLock;
    private Condition mockCondition;
    private Guard mockGuard;

    @Before
    public void setUp() {
        monitor = new Monitor();
        mockLock = mock(ReentrantLock.class);
        mockCondition = mock(Condition.class);
        when(mockLock.newCondition()).thenReturn(mockCondition);
        mockGuard = new MyGuard(monitor);
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(mockLock.tryLock().get());
        monitor.leave();
        assertFalse(mockLock.tryLock().get());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithInvalidGuard() {
        mockGuard.monitor = new Object();
        monitor.enterWhen(mockGuard);
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(mockLock.tryLockInterruptibly(1, TimeUnit.MILLISECONDS).get());
        monitor.leave();
        assertFalse(mockLock.tryLockInterruptibly(1, TimeUnit.MILLISECONDS).get());
    }

    @Test
    public void testEnterWithTimeout() throws InterruptedException, TimeoutException {
        monitor.enterWhen(mockGuard, 1, TimeUnit.MILLISECONDS);
        assertFalse(mockLock.tryLock(1, TimeUnit.MILLISECONDS).get());
        assertTrue(mockCondition.await(1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithInterrupted() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.enterInterruptibly();
    }

    @Test
    public void testTryEnter() {
        assertTrue(monitor.tryEnter());
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testTryEnterWithGuard() {
        assertTrue(monitor.tryEnterIf(mockGuard));
        assertFalse(monitor.tryEnterIf(mockGuard));
    }

    @Test
    public void testTryEnterWithGuardAndTimeout() {
        assertTrue(monitor.tryEnterIf(mockGuard, 1, TimeUnit.MILLISECONDS));
        assertFalse(monitor.tryEnterIf(mockGuard, 1, TimeUnit.MILLISECONDS));
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithInvalidGuardAndLock() {
        monitor.enterWhen(mockGuard);
        assertFalse(mockLock.tryLock().get());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithInvalidGuardAndIsSatisfied() {
        mockGuard.isSatisfied = () -> false;
        monitor.enterWhen(mockGuard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWithInterruptedWait() throws InterruptedException {
        monitor.enterWhen(mockGuard);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        monitor.waitFor(mockGuard);
        assertTrue(mockLock.tryLock().get());
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForWithInterrupted() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.waitFor(mockGuard);
    }

    @Test
    public void testWaitForUninterruptibly() {
        monitor.waitForUninterruptibly(mockGuard);
        assertTrue(mockLock.tryLock().get());
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptiblyWithSatisfied() {
        mockGuard.isSatisfied = () -> true;
        monitor.waitForUninterruptibly(mockGuard);
    }

    @Test
    public void testWaitForUninterruptiblyWithInterrupted() {
        Thread.currentThread().interrupt();
        monitor.waitForUninterruptibly(mockGuard);
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException, TimeoutException {
        monitor.waitFor(mockGuard, 1, TimeUnit.MILLISECONDS);
        assertFalse(mockLock.tryLock(1, TimeUnit.MILLISECONDS).get());
        assertTrue(mockCondition.await(1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForWithInterruptedTimeout() throws InterruptedException, TimeoutException {
        Thread.currentThread().interrupt();
        monitor.waitFor(mockGuard, 1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        long timeoutNanos = 1000;
        monitor.waitForUninterruptibly(mockGuard, timeoutNanos);
        assertFalse(mockLock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS).get());
        assertTrue(mockCondition.awaitNanos(timeoutNanos, timeoutNanos));
        monitor.leave();
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForUninterruptiblyWithInterruptedTimeout() throws InterruptedException {
        Thread.currentThread().interrupt();
        long timeoutNanos = 1000;
        monitor.waitForUninterruptibly(mockGuard, timeoutNanos);
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(mockLock.isHeldByCurrentThread());
    }

    @Test
    public void testIsFair() {
        assertTrue(monitor.isFair());
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
        monitor.enter();
        assertEquals(3, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enter();
        Thread thread = new Thread(() -> {});
        monitor.enterWhen(new MyGuard(monitor));
        assertTrue(monitor.hasQueuedThreads(mockGuard));
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads(mockGuard));
    }

    @Test
    public void testHasQueuedThread() {
        Thread thread = new Thread(() -> {});
        monitor.enter();
        monitor.enterWhen(new MyGuard(monitor));
        assertTrue(monitor.hasQueuedThread(thread));
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testSignalNextWaiter() {
        mockGuard.waiterCount = 2;
        monitor.signalNextWaiter();
        assertEquals(1, mockGuard.waiterCount);
    }

    @Test
    public void testIsSatisfied() {
        mockGuard.isSatisfied = () -> true;
        assertTrue(monitor.isSatisfied(mockGuard));
        mockGuard.isSatisfied = () -> false;
        assertFalse(monitor.isSatisfied(mockGuard));
    }

    @Test
    public void testSignalAllWaiters() {
        mockGuard.waiterCount = 3;
        monitor.signalAllWaiters();
        assertEquals(0, mockGuard.waiterCount);
    }

    static class MyGuard extends Monitor.Guard {
        int waiterCount;
        Monitor monitor;

        public MyGuard(Monitor monitor) {
            super(monitor);
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() {
            return monitor.isOccupied();
        }
    }
}