package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;
import org.junit.*;
import org.mockito.*;

@Beta
public final class MonitorTest {

    private Monitor monitor;
    private ReentrantLock lockMock;
    private Condition conditionMock;
    private Guard guard;

    @Before
    public void setUp() {
        lockMock = mock(ReentrantLock.class);
        conditionMock = mock(Condition.class);
        monitor = new Monitor() {{
            this.lock = lockMock;
            this.condition = conditionMock;
        }};
        guard = mock(Guard.class);
    }

    @Test
    public void testEnter() {
        monitor.enter();
        verify(lockMock, times(1)).lock();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongMonitor() {
        monitor.enter();
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        when(guard.monitor).thenReturn(otherMonitor);
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        verify(lockMock, times(1)).lockInterruptibly();
    }

    @Test
    public void testEnterWithTimeout() throws InterruptedException {
        long timeoutNanos = 100;
        when(lockMock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS)).thenReturn(true);
        monitor.enterWhen(guard, timeoutNanos, TimeUnit.NANOSECONDS);
        verify(lockMock, times(1)).tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyWithInterruption() throws InterruptedException {
        monitor.enterInterruptibly();
        Thread.currentThread().interrupt();
        monitor.enterWhen(guard);
    }

    @Test
    public void testTryEnter() {
        monitor.tryEnter();
        verify(lockMock, times(1)).tryLock();
    }

    @Test
    public void testEnterWhenWithSatisfiedCondition() throws InterruptedException {
        monitor.enterWhen(guard);
        verify(guard).isSatisfied();
        verify(conditionMock, times(0)).await();
        verifyNoMoreInteractions(lockMock);
    }

    @Test
    public void testEnterWhenWithUnsatisfiedCondition() throws InterruptedException {
        when(guard.isSatisfied()).thenReturn(false);
        monitor.enterWhen(guard);
        verify(conditionMock, times(1)).await();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenWithWrongMonitor() throws InterruptedException {
        monitor.enterWhen(guard);
        Monitor otherMonitor = new Monitor();
        when(guard.monitor).thenReturn(otherMonitor);
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterWhenUninterruptiblyWithSatisfiedCondition() {
        monitor.enterWhenUninterruptibly(guard);
        verify(guard).isSatisfied();
        verifyNoMoreInteractions(lockMock);
    }

    @Test
    public void testEnterWhenUninterruptiblyWithUnsatisfiedCondition() {
        when(guard.isSatisfied()).thenReturn(false);
        monitor.enterWhenUninterruptibly(guard);
        verify(conditionMock, times(1)).await();
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeout() {
        long timeoutNanos = 100;
        when(conditionMock.await(timeoutNanos, TimeUnit.NANOSECONDS)).thenReturn(true);
        monitor.enterWhenUninterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
        verify(conditionMock, times(1)).await(timeoutNanos, TimeUnit.NANOSECONDS);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenInterruptiblyWithInterruption() throws InterruptedException {
        monitor.enterWhenInterruptibly(guard);
        Thread.currentThread().interrupt();
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterIf() {
        when(guard.isSatisfied()).thenReturn(true);
        monitor.enterIf(guard);
        verifyNoMoreInteractions(lockMock);
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfWithWrongMonitor() {
        monitor.enterIf(guard);
        Monitor otherMonitor = new Monitor();
        when(guard.monitor).thenReturn(otherMonitor);
        monitor.enterIf(guard);
    }

    @Test
    public void testEnterIfUninterruptibly() {
        monitor.enterIfUninterruptibly(guard);
        verifyNoMoreInteractions(lockMock);
    }

    @Test
    public void testEnterIfWithTimeout() {
        long time = 100;
        long unit = TimeUnit.MILLISECONDS;
        when(lockMock.tryLock(time, unit)).thenReturn(true);
        monitor.enterIf(guard, time, unit);
        verify(lockMock, times(1)).tryLock(time, unit);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfInterruptibly() throws InterruptedException {
        monitor.enterIfInterruptibly(guard);
        Thread.currentThread().interrupt();
        monitor.enterIf(guard);
    }

    @Test
    public void testEnterIfWithTimeoutUnsatisfied() {
        long time = 100;
        long unit = TimeUnit.MILLISECONDS;
        when(guard.isSatisfied()).thenReturn(false);
        monitor.enterIf(guard, time, unit);
        verify(conditionMock, times(1)).await(anyLong(), eq(unit));
    }

    @Test
    public void testTryEnterIf() {
        when(guard.isSatisfied()).thenReturn(true);
        monitor.tryEnterIf(guard);
        verifyNoMoreInteractions(lockMock);
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        monitor.waitFor(guard);
        verify(guard).isSatisfied();
        verify(conditionMock, times(1)).await();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testWaitForWithWrongMonitor() throws InterruptedException {
        monitor.waitFor(guard);
        Monitor otherMonitor = new Monitor();
        when(guard.monitor).thenReturn(otherMonitor);
        monitor.waitFor(guard);
    }

    @Test
    public void testWaitForUninterruptibly() {
        monitor.waitForUninterruptibly(guard);
        verify(guard).isSatisfied();
        verifyNoMoreInteractions(conditionMock);
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        long time = 100;
        long unit = TimeUnit.MILLISECONDS;
        when(guard.isSatisfied()).thenReturn(false);
        monitor.waitFor(guard, time, unit);
        verify(conditionMock, times(1)).await(eq(time), eq(unit));
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterruptibly() throws InterruptedException {
        monitor.waitForInterruptibly(guard);
        Thread.currentThread().interrupt();
        monitor.waitFor(guard);
    }

    @Test
    public void testWaitForWithTimeoutSatisfied() throws InterruptedException {
        monitor.waitFor(guard, 100, TimeUnit.MILLISECONDS);
        verify(guard, times(1)).isSatisfied();
        verifyNoMoreInteractions(conditionMock);
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        long time = 100;
        long unit = TimeUnit.MILLISECONDS;
        when(conditionMock.await(time, unit)).thenReturn(true);
        monitor.waitForUninterruptibly(guard, time, unit);
        verify(conditionMock, times(1)).await(time, unit);
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterruptiblyWithInterruption() throws InterruptedException {
        monitor.waitForInterruptibly(guard);
        Thread.currentThread().interrupt();
        monitor.waitForUninterruptibly(guard);
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeoutSatisfied() {
        long time = 100;
        long unit = TimeUnit.MILLISECONDS;
        when(guard.isSatisfied()).thenReturn(true);
        monitor.waitForUninterruptibly(guard, time, unit);
        verifyNoMoreInteractions(conditionMock);
    }

    @Test
    public void testLeave() {
        monitor.leave();
        verify(lockMock, times(1)).unlock();
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
        assertEquals(2, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        monitor.enter();
        assertEquals(0, monitor.getQueueLength());
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
}