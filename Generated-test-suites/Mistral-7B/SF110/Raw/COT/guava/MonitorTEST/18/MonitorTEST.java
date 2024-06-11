package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

    @Test
    public void testMonitorConstruction() {
        new Monitor();
        new Monitor(true);
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterTimeout() {
        Monitor monitor = new Monitor();
        boolean entered = monitor.enter(1L, TimeUnit.SECONDS);
        assertFalse(entered);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterTimeoutInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly(1L, TimeUnit.SECONDS);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testTryEnterThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enter();
        Thread.currentThread().interrupt();
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testEnterWhen() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        monitor.enterWhen(guard);
        verify(guard).monitor = monitor;
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenIllegalMonitorStateException() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.monitor).thenReturn(new Object() {
            @SuppressWarnings("unused")
            final Monitor mockMonitor = monitor;
        });
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterWhenTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        long timeoutNanos = 1L;
        monitor.enterWhen(guard, timeoutNanos);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenTimeoutInterruptibleInterruptedException() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.enterWhenInterruptibly(guard, 1L, TimeUnit.SECONDS);
    }

    @Test
    public void testEnterWhenTimeoutSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(true);
        monitor.enterWhen(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(true);
        monitor.enterIf(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfFalse() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.enterIf(guard);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.enterIf(guard, 1L, TimeUnit.SECONDS);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfTimeoutInterruptibleInterruptedException() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.enterIfInterruptibly(guard, 1L, TimeUnit.SECONDS);
    }

    @Test
    public void testEnterIfTimeoutSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(true);
        monitor.enterIf(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(true);
        monitor.tryEnterIf(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfFalse() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.tryEnterIf(guard);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        monitor.waitFor(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterruptibleInterruptedException() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        monitor.waitForInterruptibly(guard);
    }

    @Test
    public void testWaitForTrue() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(true);
        monitor.waitFor(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForFalse() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.waitFor(guard);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.waitFor(guard, 1L, TimeUnit.SECONDS);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForTimeoutInterruptibleInterruptedException() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        monitor.waitForInterruptibly(guard, 1L, TimeUnit.SECONDS);
    }

    @Test
    public void testWaitForTimeoutSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(true);
        monitor.waitFor(guard, 1L, TimeUnit.SECONDS);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        monitor.waitForUninterruptibly(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForUninterruptiblyInterruptibleInterruptedException() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        monitor.waitForUninterruptibly(guard);
        Thread.currentThread().interrupt();
    }

    @Test
    public void testWaitForUninterruptiblyTrue() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(true);
        monitor.waitForUninterruptibly(guard);
        assertTrue(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyFalse() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.waitForUninterruptibly(guard);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        when(guard.isSatisfied()).thenReturn(false);
        monitor.waitForUninterruptibly(guard, 1L, TimeUnit.SECONDS);
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testLeaveInterruptibleInterruptedException() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enter();
        Thread.currentThread().interrupt();
        monitor.leave();
        assertFalse(monitor.lock.isHeldByCurrentThread());
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.isFair());
        Monitor monitorFair = new Monitor(true);
        assertTrue(monitorFair.isFair());
    }

    @Test
    public void testIsOccupied() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.isOccupied());
        Monitor monitorOccupied = new Monitor();
        monitorOccupied.enter();
        assertTrue(monitorOccupied.isOccupied());
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.isOccupiedByCurrentThread());
        Monitor monitorOccupied = new Monitor();
        monitorOccupied.enter();
        monitorOccupied.enter();
        assertTrue(monitorOccupied.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() {
        Monitor monitor = new Monitor();
        assertEquals(0, monitor.getOccupiedDepth());
        Monitor monitorOccupied = new Monitor();
        monitorOccupied.enter();
        monitorOccupied.enter();
        assertEquals(2, monitorOccupied.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThreads(null));
        Monitor monitorWithQueuedThread = new Monitor();
        Thread thread = new Thread();
        monitorWithQueuedThread.enter();
        monitorWithQueuedThread.awaitUninterruptibly(null);
        assertTrue(monitorWithQueuedThread.hasQueuedThreads(thread.getClass()));
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.hasQueuedThread(null));
        Monitor monitorWithQueuedThread = new Monitor();
        Thread thread = new Thread();
        monitorWithQueuedThread.enter();
        monitorWithQueuedThread.awaitUninterruptibly(null);
        monitorWithQueuedThread.awaitUninterruptibly(null);
        assertTrue(monitorWithQueuedThread.hasQueuedThread(thread.getClass()));
        assertFalse(monitorWithQueuedThread.hasQueuedThread(Thread.class));
    }

    @Test
    public void testHasWaiters() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        monitor.enterWhen(guard);
        assertTrue(monitor.hasWaiters(guard));
        monitor.leave();
        assertFalse(monitor.hasWaiters(guard));
    }

    @Test
    public void testGetWaitQueueLength() {
        Monitor monitor = new Monitor();
        Guard guard = mock(Guard.class);
        monitor.enterWhen(guard);
        assertEquals(1, monitor.getWaitQueueLength(guard));
        monitor.leave();
        assertEquals(0, monitor.getWaitQueueLength(guard));
    }
}