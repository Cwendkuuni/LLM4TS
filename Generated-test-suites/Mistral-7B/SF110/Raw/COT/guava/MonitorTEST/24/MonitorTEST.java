package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    private Monitor monitor;
    private Condition condition;
    private Guard guard;
    private Thread thread;

    @Before
    public void setUp() {
        monitor = new Monitor();
        condition = monitor.lock.newCondition();
        guard = new Monitor.Guard(monitor);
        thread = new Thread();
    }

    @Test
    public void testMonitorConstructor() {
        Monitor monitor = new Monitor();
        assertNull(monitor.activeGuards);
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testMonitorEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testMonitorEnterInterruptiblyInterrupted() throws InterruptedException {
        monitor.enterInterruptibly();
        Thread.currentThread().interrupt();
    }

    @Test
    public void testMonitorEnterTimeout() {
        monitor.enter();
        monitor.enterInterruptibly();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterGuardSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testMonitorEnterGuardUnsatisfied() throws InterruptedException {
        monitor.enterWhen(guard);
        fail("Expected IllegalMonitorStateException");
    }

    @Test
    public void testMonitorEnterInterruptiblyGuardSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterWhenInterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testMonitorEnterInterruptiblyGuardUnsatisfied() throws InterruptedException {
        monitor.enterWhenInterruptibly(guard);
        fail("Expected IllegalMonitorStateException");
    }

    @Test
    public void testMonitorEnterGuardTimeout() throws InterruptedException {
        long time = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        monitor.enterWhen(guard, time, unit);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterGuardTimeoutSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterInterruptiblyGuardTimeout() throws InterruptedException {
        long time = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        monitor.enterWhenInterruptibly(guard, time, unit);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterInterruptiblyGuardTimeoutSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterWhenInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIf() {
        monitor.enterIf(guard);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfGuardSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterIf(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfTimeout() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfTimeoutSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfInterruptibly() throws InterruptedException {
        monitor.enterIfInterruptibly(guard);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfInterruptiblyGuardSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterIfInterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfInterruptiblyTimeout() throws InterruptedException {
        monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfInterruptiblyTimeoutSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfInterruptiblyGuardTimeout() throws InterruptedException {
        monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfInterruptiblyGuardTimeoutSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorEnterIfInterruptiblyGuardTimeoutInterrupted() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(Thread.currentThread().isInterrupted());
    }

    @Test
    public void testMonitorWaitFor() throws InterruptedException {
        guard.condition.signalAll();
        monitor.waitFor(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorWaitForUninterruptibly() throws InterruptedException {
        monitor.waitForUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorWaitForUninterruptiblyInterrupted() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.waitForUninterruptibly(guard);
        assertTrue(Thread.currentThread().isInterrupted());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorWaitForTimeout() throws InterruptedException {
        monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorWaitForTimeoutSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorWaitForUninterruptiblyTimeout() {
        long time = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        monitor.waitForUninterruptibly(guard, time, unit);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorWaitForUninterruptiblyTimeoutSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.waitForUninterruptibly(guard, time, unit);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorWaitForTimeoutInterrupted() throws InterruptedException {
        monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        Thread.currentThread().interrupt();
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(Thread.currentThread().isInterrupted());
    }

    @Test
    public void testMonitorWaitForInterruptiblyTimeoutInterrupted() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.waitForInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(Thread.currentThread().isInterrupted());
    }

    @Test
    public void testMonitorWaitForInterruptiblyTimeoutSatisfied() throws InterruptedException {
        guard.condition.signalAll();
        monitor.waitForInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorIsFair() {
        Monitor monitor1 = new Monitor(true);
        Monitor monitor2 = new Monitor();
        assertFalse(monitor1.isFair());
        assertTrue(monitor2.isFair());
    }

    @Test
    public void testMonitorIsOccupied() {
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testMonitorIsOccupiedByCurrentThread() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testMonitorGetOccupiedDepth() {
        monitor.enter();
        monitor.enter();
        assertEquals(2, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testMonitorGetQueueLength() {
        monitor.enter();
        monitor.enter();
        Condition condition = monitor.lock.newCondition();
        monitor.await(condition, 1, TimeUnit.SECONDS);
        assertEquals(2, monitor.getQueueLength());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testMonitorHasQueuedThreads() {
        monitor.enter();
        Condition condition = monitor.lock.newCondition();
        monitor.await(condition, 1, TimeUnit.SECONDS);
        assertTrue(monitor.hasQueuedThreads(condition));
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads(condition));
    }

    @Test
    public void testMonitorHasQueuedThread() {
        monitor.enter();
        Condition condition = monitor.lock.newCondition();
        monitor.await(condition, 1, TimeUnit.SECONDS);
        thread.start();
        monitor.await(condition, 1, TimeUnit.SECONDS);
        assertTrue(monitor.hasQueuedThread(thread));
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(thread));
    }
}