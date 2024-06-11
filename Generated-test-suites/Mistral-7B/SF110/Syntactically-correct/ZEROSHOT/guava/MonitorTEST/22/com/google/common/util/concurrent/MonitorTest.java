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
        mockLock = mock(ReentrantLock.class);
        mockCondition = mock(Condition.class);
        monitor = new Monitor() {
            @Override
            ReentrantLock lock() {
                return mockLock;
            }

            @Override
            Condition condition() {
                return mockCondition;
            }
        };
        mockGuard = mockGuard();
    }

    @Test
    public void testEnter() {
        monitor.enter();
        verify(mockLock).lock();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithDifferentMonitor() {
        Monitor anotherMonitor = new Monitor();
        monitor.enter();
        anotherMonitor.enter();
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        verify(mockLock).lockInterruptibly();
    }

    @Test
    public void testEnterWhenGuardSatisfied() throws InterruptedException {
        monitor.enterWhen(mockGuard);
        verify(mockLock).lockInterruptibly();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testEnterWhenGuardNotSatisfied() throws InterruptedException {
        when(mockGuard.isSatisfied()).thenReturn(false);
        monitor.enterWhen(mockGuard);
        verify(mockLock).lockInterruptibly();
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testEnterWhenUninterruptiblyGuardSatisfied() {
        monitor.enterWhenUninterruptibly(mockGuard);
        verify(mockLock).lock();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testEnterWhenUninterruptiblyGuardNotSatisfied() {
        when(mockGuard.isSatisfied()).thenReturn(false);
        monitor.enterWhenUninterruptibly(mockGuard);
        verify(mockLock).lock();
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testEnterWhenWithTimeoutGuardSatisfied() throws InterruptedException {
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enterWhen(mockGuard, timeout, timeUnit);
        verify(mockLock).tryLock(timeout, timeUnit);
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testEnterWhenWithTimeoutGuardNotSatisfied() throws InterruptedException {
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        when(mockGuard.isSatisfied()).thenReturn(false);
        monitor.enterWhen(mockGuard, timeout, timeUnit);
        verify(mockLock).tryLock(timeout, timeUnit);
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testTryEnter() {
        try {
            monitor.tryEnter();
            fail("Expected ReentrantLock$NonreentrantLockException");
        } catch (ReentrantLock$NonreentrantLockException e) {
            // Expected
        }
        verifyZeroInteractions(mockLock);
    }

    @Test
    public void testTryEnterWhenGuardSatisfied() {
        monitor.tryEnterIf(mockGuard);
        verifyZeroInteractions(mockLock);
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testTryEnterWhenGuardNotSatisfied() {
        when(mockGuard.isSatisfied()).thenReturn(false);
        monitor.tryEnterIf(mockGuard);
        verifyZeroInteractions(mockLock);
        verifyZeroInteractions(mockCondition);
    }

    @Test
    public void testTryEnterWithTimeoutGuardSatisfied() {
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        assertTrue(monitor.tryEnterIf(mockGuard, timeout, timeUnit));
        verifyZeroInteractions(mockLock);
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testTryEnterWithTimeoutGuardNotSatisfied() {
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        when(mockGuard.isSatisfied()).thenReturn(false);
        assertFalse(monitor.tryEnterIf(mockGuard, timeout, timeUnit));
        verifyZeroInteractions(mockLock);
        verifyZeroInteractions(mockCondition);
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testWaitForWithDifferentMonitor() throws InterruptedException {
        monitor.wait();
        Monitor anotherMonitor = new Monitor();
        anotherMonitor.wait();
    }

    @Test
    public void testWaitForUninterruptibly() throws InterruptedException {
        monitor.waitForUninterruptibly();
        verify(mockCondition).awaitUninterruptibly();
    }

    @Test
    public void testWaitForWithGuardSatisfied() throws InterruptedException {
        monitor.waitFor(mockGuard);
        verify(mockLock).isHeldByCurrentThread();
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testWaitForWithTimeoutAndGuardSatisfied() throws InterruptedException {
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.waitFor(mockGuard, timeout, timeUnit);
        verify(mockLock).isHeldByCurrentThread();
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testWaitForWithTimeoutAndGuardNotSatisfied() throws InterruptedException {
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        when(mockCondition.await(timeout, timeUnit)).thenReturn(false);
        when(mockCondition.await()).thenThrow(new InterruptedException());
        monitor.waitFor(mockGuard, timeout, timeUnit);
        verify(mockLock).isHeldByCurrentThread();
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testWaitForInterruptiblyWithGuardSatisfied() throws InterruptedException {
        monitor.waitForInterruptibly(mockGuard);
        verify(mockLock).isHeldByCurrentThread();
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testWaitForInterruptiblyWithTimeoutAndGuardSatisfied() throws InterruptedException {
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.waitFor(mockGuard, timeout, timeUnit);
        verify(mockLock).isHeldByCurrentThread();
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testWaitForInterruptiblyWithTimeoutAndGuardNotSatisfied() throws InterruptedException {
        long timeout = 1;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        when(mockCondition.await(timeout, timeUnit)).thenReturn(false);
        when(mockCondition.await()).thenThrow(new InterruptedException());
        monitor.waitFor(mockGuard, timeout, timeUnit);
        verify(mockLock).isHeldByCurrentThread();
        verify(mockCondition).await();
        verify(mockGuard).isSatisfied();
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsOccupied() {
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testIsFair() {
        Monitor fairMonitor = new Monitor(true);
        Monitor unfairMonitor = new Monitor();
        assertTrue(fairMonitor.isFair());
        assertFalse(unfairMonitor.isFair());
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        verify(mockLock).unlock();
    }

    @Test
    public void testLeaveWithInterruptedThread() {
        monitor.enter();
        monitor.interrupt();
        monitor.leave();
        verify(mockLock).unlock();
        verify(mockLock, atLeastOnce()).isHeldByCurrentThread();
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
        monitor.enter();
        monitor.enter();
        assertEquals(1, monitor.getQueueLength());
        monitor.leave();
        monitor.enter();
        assertEquals(1, monitor.getQueueLength());
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enter();
        monitor.enter();
        assertTrue(monitor.hasQueuedThreads(mockGuard));
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads(mockGuard));
    }

    @Test
    public void testHasQueuedThread() {
        monitor.enter();
        Thread thread = new Thread();
        monitor.enter();
        assertTrue(monitor.hasQueuedThread(thread));
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(thread));
    }

    private Guard mockGuard() {
        return mock(Guard.class);
    }
}