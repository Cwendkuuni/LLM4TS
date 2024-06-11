package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    private Monitor monitor;
    private ReentrantLock lock;
    private Guard guard;

    @Before
    public void setUp() {
        monitor = new Monitor();
        lock = new ReentrantLock();
        guard = new Monitor.Guard() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        };
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongMonitor() {
        Monitor otherMonitor = new Monitor();
        monitor.enter();
        otherMonitor.enter();
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterInterruptiblyWithInterruptedThread() throws InterruptedException {
        Thread thread = Thread.currentThread();
        monitor.enterInterruptibly();
        thread.interrupt();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndSatisfiedCondition() throws InterruptedException {
        monitor.enterWhen(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithGuardAndUnsatisfiedCondition() throws InterruptedException {
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterInterruptiblyWithGuardAndSatisfiedCondition() throws InterruptedException {
        monitor.enterWhenInterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterInterruptiblyWithGuardAndUnsatisfiedCondition() throws InterruptedException {
        monitor.enterWhenInterruptibly(guard);
        monitor.enterWhenInterruptibly(guard);
    }

    @Test
    public void testEnterWithGuardAndTimeout() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enterWhen(guard, timeout, timeUnit);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndTimeoutAndUnsatisfiedCondition() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enterWhen(guard, timeout, timeUnit);
        monitor.enterWhen(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndTimeoutAndInterruptedThread() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Thread thread = Thread.currentThread();
        monitor.enterWhen(guard, timeout, timeUnit);
        thread.interrupt();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndUninterruptiblyAndSatisfiedCondition() {
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndUninterruptiblyAndUnsatisfiedCondition() {
        monitor.enterWhenUninterruptibly(guard);
        monitor.enterWhenUninterruptibly(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndUninterruptiblyAndTimeout() {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enterWhenUninterruptibly(guard, timeout, timeUnit);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndUninterruptiblyAndTimeoutAndUnsatisfiedCondition() {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enterWhenUninterruptibly(guard, timeout, timeUnit);
        monitor.enterWhenUninterruptibly(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndTimeoutAndInterruptedThread() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Thread thread = Thread.currentThread();
        monitor.enterWhen(guard, timeout, timeUnit);
        thread.interrupt();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithGuardAndUninterruptiblyAndTimeoutAndInterruptedThread() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Thread thread = Thread.currentThread();
        monitor.enterWhenUninterruptibly(guard, timeout, timeUnit);
        thread.interrupt();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        assertTrue(monitor.tryEnter());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterWithOccupiedLock() {
        monitor.enter();
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testTryEnterWithGuardAndSatisfiedCondition() {
        assertTrue(monitor.tryEnterIf(guard));
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterWithGuardAndUnsatisfiedCondition() {
        assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testTryEnterWithGuardAndTimeout() {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        assertTrue(monitor.tryEnterIf(guard, timeout, timeUnit));
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterWithGuardAndTimeoutAndUnsatisfiedCondition() {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.tryEnterIf(guard, timeout, timeUnit));
    }

    @Test
    public void testTryEnterWithGuardAndTimeoutAndInterruptedThread() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Thread thread = Thread.currentThread();
        monitor.enterWhen(guard);
        assertFalse(monitor.tryEnterIf(guard, timeout, timeUnit));
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        monitor.enter();
        monitor.waitFor(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithUnsatisfiedCondition() throws InterruptedException {
        monitor.enter();
        monitor.waitFor(guard);
        monitor.enter();
        monitor.waitFor(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testWaitForWithWrongMonitor() throws InterruptedException {
        Monitor otherMonitor = new Monitor();
        monitor.enter();
        otherMonitor.enter();
        otherMonitor.waitFor(guard);
    }

    @Test
    public void testWaitForUninterruptibly() {
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithUnsatisfiedCondition() {
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithWrongMonitor() {
        Monitor otherMonitor = new Monitor();
        monitor.enter();
        otherMonitor.enter();
        otherMonitor.waitForUninterruptibly(guard);
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enter();
        monitor.waitFor(guard, timeout, timeUnit);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithTimeoutAndUnsatisfiedCondition() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enter();
        monitor.waitFor(guard, timeout, timeUnit);
        monitor.enter();
        monitor.waitFor(guard, timeout, timeUnit);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithTimeoutAndInterruptedThread() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Thread thread = Thread.currentThread();
        monitor.enter();
        monitor.waitFor(guard, timeout, timeUnit);
        thread.interrupt();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enter();
        monitor.waitForUninterruptibly(guard, timeout, timeUnit);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeoutAndUnsatisfiedCondition() {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        monitor.enter();
        monitor.waitForUninterruptibly(guard, timeout, timeUnit);
        monitor.enter();
        monitor.waitForUninterruptibly(guard, timeout, timeUnit);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeoutAndInterruptedThread() throws InterruptedException {
        long timeout = 1L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Thread thread = Thread.currentThread();
        monitor.enter();
        monitor.waitForUninterruptibly(guard, timeout, timeUnit);
        thread.interrupt();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testLeaveWithUnheldLock() {
        monitor.leave();
    }

    @Test
    public void testIsFair() {
        Monitor fairMonitor = new Monitor(true);
        Monitor unfairMonitor = new Monitor();
        assertTrue(fairMonitor.isFair());
        assertFalse(unfairMonitor.isFair());
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
        assertEquals(1, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        monitor.enter();
        monitor.enter();
        Thread thread = new Thread(() -> {});
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
        assertEquals(5, monitor.getQueueLength());
        monitor.leave();
        monitor.leave();
        monitor.leave();
        monitor.leave();
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enter();
        monitor.enter();
        Thread thread = new Thread(() -> {});
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
        monitor.enterWhen(guard);
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        monitor.leave();
        monitor.leave();
        monitor.leave();
        monitor.leave();
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads());
    }
}