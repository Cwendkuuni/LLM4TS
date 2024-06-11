package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

public class MonitorTest {

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertNull(monitor.activeGuards);
        assertFalse(monitor.isFair());
        assertTrue(fairMonitor.isFair());
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
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor2.enter();
        monitor1.leave();
        monitor1.enter(); // should fail
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptiblyWithInterruption() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly();
            fail();
        });
        monitor.enter();
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterWithTime() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        long time = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.enter(time, unit));
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.enter();
        monitor.leave();
        monitor.enter(time, unit);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptiblyWithTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        long time = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enterInterruptibly(time, unit);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.tryEnter());
        monitor.enter();
        monitor.leave();
        assertTrue(monitor.tryEnter());
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        monitor.enterWhen(condition);
        assertFalse(condition.isSignaled());
        condition.signal();
        monitor.leave();
        assertTrue(condition.isSignaled());
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        monitor.enterWhenUninterruptibly(condition);
        assertFalse(condition.isSignaled());
        condition.signal();
        monitor.leave();
        assertTrue(condition.isSignaled());
    }

    @Test
    public void testEnterWhenWithTime() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        long time = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        assertFalse(monitor.enterWhen(condition, time, unit));
        monitor.enter();
        monitor.leave();
        monitor.enterWhen(condition, time, unit);
        assertTrue(condition.isSignaled());
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTime() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        long time = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enterWhenUninterruptibly(condition, time, unit);
        assertFalse(condition.isSignaled());
        condition.signal();
        monitor.leave();
        monitor.enterWhenUninterruptibly(condition, time, unit);
        assertTrue(condition.isSignaled());
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        assertTrue(monitor.enterIf(condition));
        assertFalse(condition.isSatisfied());
        condition.signal();
        monitor.leave();
        assertTrue(condition.isSatisfied());
        assertFalse(monitor.enterIf(condition));
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        assertTrue(monitor.enterIfInterruptibly(condition));
        assertFalse(condition.isSatisfied());
        condition.signal();
        monitor.leave();
        assertTrue(condition.isSatisfied());
        assertFalse(monitor.enterIfInterruptibly(condition));
    }

    @Test
    public void testEnterIfWithTime() {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        assertFalse(monitor.enterIf(condition, 100, TimeUnit.MILLISECONDS));
        monitor.enter();
        monitor.leave();
        assertTrue(monitor.enterIf(condition, 100, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testEnterIfInterruptiblyWithTime() throws InterruptedException {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        assertFalse(monitor.enterIfInterruptibly(condition, 100, TimeUnit.MILLISECONDS));
        monitor.enter();
        monitor.leave();
        assertTrue(monitor.enterIfInterruptibly(condition, 100, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testTryEnterIf() {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        assertTrue(monitor.tryEnterIf(condition));
        assertFalse(condition.isSatisfied());
        condition.signal();
        monitor.leave();
        assertTrue(condition.isSatisfied());
        assertFalse(monitor.tryEnterIf(condition));
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        monitor.enter();
        monitor.waitFor(condition);
        monitor.leave();
        condition.signal();
        assertFalse(condition.isSignaled());
        monitor.waitFor(condition);
        assertTrue(condition.isSignaled());
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        monitor.enter();
        monitor.waitForUninterruptibly(condition);
        monitor.leave();
        condition.signal();
        assertFalse(condition.isSignaled());
        monitor.waitForUninterruptibly(condition);
        assertTrue(condition.isSignaled());
        monitor.leave();
    }

    @Test
    public void testWaitForWithTime() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        monitor.enter();
        monitor.waitFor(condition, 100, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(condition.isSignaled());
        monitor.waitFor(condition, 100, TimeUnit.MILLISECONDS);
        assertTrue(condition.isSignaled());
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptiblyWithTime() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        monitor.enter();
        monitor.waitForUninterruptibly(condition, 100, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(condition.isSignaled());
        monitor.waitForUninterruptibly(condition, 100, TimeUnit.MILLISECONDS);
        assertTrue(condition.isSignaled());
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
        Monitor fairMonitor = new Monitor(true);
        assertFalse(monitor.isFair());
        assertTrue(fairMonitor.isFair());
    }

    @Test
    public void testIsOccupied() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupied());
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() {
        Monitor monitor = new Monitor();
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
        Monitor monitor = new Monitor();
        Condition condition1 = new ReentrantLock().newCondition();
        Condition condition2 = new ReentrantLock().newCondition();
        monitor.enter();
        monitor.enterWhen(condition1);
        monitor.enterWhen(condition2);
        monitor.leave();
        assertEquals(2, monitor.getQueueLength());
        condition1.signal();
        condition2.signal();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Condition condition1 = new ReentrantLock().newCondition();
        Condition condition2 = new ReentrantLock().newCondition();
        monitor.enter();
        monitor.enterWhen(condition1);
        monitor.enterWhen(condition2);
        monitor.leave();
        assertTrue(monitor.hasQueuedThreads(condition1));
        assertTrue(monitor.hasQueuedThreads(condition2));
        condition1.signal();
        condition2.signal();
        assertFalse(monitor.hasQueuedThreads(condition1));
        assertFalse(monitor.hasQueuedThreads(condition2));
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Condition condition1 = new ReentrantLock().newCondition();
        Condition condition2 = new ReentrantLock().newCondition();
        monitor.enter();
        monitor.enterWhen(condition1);
        monitor.enterWhen(condition2);
        monitor.leave();
        assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
        Thread thread = new Thread(() -> {});
        monitor.enterWhen(condition1);
        monitor.enterWhen(condition2);
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(thread));
        condition1.signalAll();
        condition2.signalAll();
        assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
        assertFalse(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testHasWaiters() {
        Monitor monitor = new Monitor();
        Condition condition = new ReentrantLock().newCondition();
        monitor.enter();
        monitor.enterWhen(condition);
        monitor.leave();
        assertTrue(monitor.hasWaiters(condition));
        condition.signalAll();
        monitor.enter();
        monitor.enterWhen(condition);
        monitor.leave();
        assertFalse(monitor.hasWaiters(condition));
    }
}