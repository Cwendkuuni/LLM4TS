package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

    private Monitor monitor;
    private ReentrantLock lock;
    private Condition condition;
    private Guard guard;

    @Before
    public void setUp() {
        monitor = new Monitor();
        lock = monitor.lock;
        condition = monitor.lock.newCondition();
        guard = new MyGuard(monitor);
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterInterruptiblyInterruptedException() throws InterruptedException {
        monitor.enterInterruptibly();
        Thread.currentThread().interrupt();
    }

    @Test
    public void testEnterInterruptiblyNoInterruption() throws InterruptedException {
        monitor.enterInterruptibly();
        assertFalse(Thread.interrupted());
    }

    @Test
    public void testEnterTimedOut() throws InterruptedException, TimeoutException {
        assertFalse(monitor.tryEnter());
        assertFalse(monitor.enter(1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testEnterTimedOutInterruptedException() throws InterruptedException, TimeoutException {
        monitor.enter();
        Thread.currentThread().interrupt();
        assertFalse(monitor.enter(1, TimeUnit.MILLISECONDS));
        assertTrue(Thread.interrupted());
    }

    @Test
    public void testEnterInterruptiblyTimedOut() throws InterruptedException, TimeoutException {
        assertFalse(monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS));
        assertTrue(Thread.interrupted());
    }

    @Test
    public void testTryEnter() {
        assertTrue(monitor.tryEnter());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        monitor.enterWhen(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenInvalidMonitor() throws InterruptedException {
        Monitor otherMonitor = new Monitor();
        monitor.enterWhen(otherMonitor.guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenInterruptedException() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.enterWhen(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenTimedOutInterruptedException() throws InterruptedException, TimeoutException {
        monitor.enter();
        monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
        assertTrue(Thread.interrupted());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenTimedOutNoInterruption() throws InterruptedException, TimeoutException {
        monitor.enter();
        monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(Thread.interrupted());
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenUninterruptiblyInterruptedException() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.enterWhenUninterruptibly(guard);
    }

    @Test
    public void testEnterWhenUninterruptiblyTimedOut() throws InterruptedException, TimeoutException {
        assertFalse(monitor.tryEnter());
        monitor.enterWhenUninterruptibly(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptiblyTimedOutInterruptedException() throws InterruptedException, TimeoutException {
        monitor.enter();
        assertFalse(monitor.tryEnter());
        monitor.enterWhenUninterruptibly(guard);
        assertFalse(lock.isHeldByCurrentThread());
        assertTrue(Thread.interrupted());
    }

    @Test
    public void testEnterIf() {
        monitor.enterIf(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterIfInvalidMonitor() throws InterruptedException {
        Monitor otherMonitor = new Monitor();
        monitor.enterIf(otherMonitor.guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfInterruptedException() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.enterIf(guard);
    }

    @Test
    public void testEnterIfTimedOut() {
        assertFalse(monitor.tryEnter());
        monitor.enterIf(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfTimedOutInterruptedException() throws InterruptedException {
        monitor.enter();
        assertFalse(monitor.tryEnter());
        monitor.enterIf(guard);
        assertFalse(lock.isHeldByCurrentThread());
        assertTrue(Thread.interrupted());
    }

    @Test
    public void testEnterIfUninterruptibly() {
        monitor.enterIfUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfUninterruptiblyInterruptedException() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.enterIfUninterruptibly(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfUninterruptiblyTimedOut() throws InterruptedException, TimeoutException {
        assertFalse(monitor.tryEnter());
        monitor.enterIfUninterruptibly(guard);
        assertFalse(lock.isHeldByCurrentThread());
        assertTrue(Thread.interrupted());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterIfUninterruptiblyTimedOutInterruptedException() throws InterruptedException, TimeoutException {
        monitor.enter();
        assertFalse(monitor.tryEnter());
        monitor.enterIfUninterruptibly(guard);
        assertFalse(lock.isHeldByCurrentThread());
        assertTrue(Thread.interrupted());
    }

    @Test
    public void testEnterIfTimedOutWithConditionSatisfied() {
        monitor.enterIf(guard);
        assertTrue(guard.isSatisfied());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfTimedOutWithConditionNotSatisfied() {
        assertFalse(guard.isSatisfied());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyTimedOutWithConditionSatisfied() {
        monitor.enterIfUninterruptibly(guard);
        assertTrue(guard.isSatisfied());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyTimedOutWithConditionNotSatisfied() {
        assertFalse(guard.isSatisfied());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        monitor.waitFor(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testWaitForInvalidMonitor() throws InterruptedException {
        Monitor otherMonitor = new Monitor();
        monitor.waitFor(otherMonitor.guard);
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForInterruptedException() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.waitFor(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForTimedOutInterruptedException() throws InterruptedException, TimeoutException {
        monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
        assertTrue(Thread.interrupted());
    }

    @Test
    public void testWaitForTimedOutWithConditionSatisfied() throws InterruptedException, TimeoutException {
        monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
        assertTrue(guard.isSatisfied());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForTimedOutWithConditionNotSatisfied() throws InterruptedException, TimeoutException {
        assertFalse(guard.isSatisfied());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptibly() {
        monitor.waitForUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForUninterruptiblyInterruptedException() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.waitForUninterruptibly(guard);
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForUninterruptiblyTimedOutInterruptedException() throws InterruptedException, TimeoutException {
        monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
        monitor.waitForUninterruptibly(guard);
        assertTrue(Thread.interrupted());
    }

    @Test(expected = InterruptedException.class)
    public void testWaitForUninterruptiblyTimedOutNoInterruption() throws InterruptedException, TimeoutException {
        monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
        monitor.waitForUninterruptibly(guard);
        assertFalse(Thread.interrupted());
    }

    @Test
    public void testWaitForUninterruptiblyWithConditionSatisfied() {
        monitor.waitForUninterruptibly(guard);
        assertTrue(guard.isSatisfied());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithConditionNotSatisfied() {
        assertFalse(guard.isSatisfied());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithConditionSatisfied() throws InterruptedException {
        monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
        assertTrue(guard.isSatisfied());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithConditionNotSatisfied() throws InterruptedException {
        assertFalse(guard.isSatisfied());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalStateException.class)
    public void testLeaveInterruptedException() throws InterruptedException {
        Thread.currentThread().interrupt();
        monitor.enter();
        monitor.leave();
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
        monitor.enter();
        Thread newThread = new Thread(MonitorTest::doNothing);
        newThread.start();
        monitor.enter();
        assertEquals(3, monitor.getQueueLength());
        monitor.leave();
        monitor.leave();
        newThread.join();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enter();
        Thread newThread1 = new Thread(MonitorTest::doNothing);
        newThread1.start();
        monitor.enter();
        Thread newThread2 = new Thread(MonitorTest::doNothing);
        newThread2.start();
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        monitor.leave();
        newThread1.join();
        newThread2.join();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        monitor.enter();
        Thread newThread1 = new Thread(MonitorTest::doNothing);
        newThread1.start();
        monitor.enter();
        Thread newThread2 = new Thread(MonitorTest::doNothing);
        newThread2.start();
        assertTrue(monitor.hasQueuedThread(newThread1));
        assertTrue(monitor.hasQueuedThread(newThread2));
        monitor.leave();
        monitor.leave();
        newThread1.join();
        newThread2.join();
        assertFalse(monitor.hasQueuedThread(newThread1));
        assertFalse(monitor.hasQueuedThread(newThread2));
    }

    @Test
    public void testHasWaiters() {
        monitor.enter();
        Condition condition = monitor.lock.newCondition();
        monitor.wait(condition, 1);
        assertTrue(monitor.hasWaiters(guard));
        monitor.leave();
        assertFalse(monitor.hasWaiters(guard));
    }

    @Test
    public void testGetWaitQueueLength() {
        monitor.enter();
        Condition condition = monitor.lock.newCondition();
        monitor.wait(condition, 1);
        assertEquals(1, monitor.getWaitQueueLength(guard));
        monitor.leave();
        assertFalse(monitor.hasWaiters(guard));
        assertFalse(monitor.getWaitQueueLength(guard) > 0);
    }

    @Test
    public void testSignalNextWaiter() {
        monitor.enter();
        Condition condition1 = monitor.lock.newCondition();
        Condition condition2 = monitor.lock.newCondition();
        monitor.wait(condition1, 1);
        monitor.signalNextWaiter();
        monitor.wait(condition2, 1);
        monitor.leave();
        assertTrue(condition2.signalAll());
    }

    @Test
    public void testIsSatisfied() {
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testIsSatisfiedWhenConditionFalse() {
        guard.waiterCount = 1;
        assertFalse(guard.isSatisfied());
    }

    private static void doNothing() {}
}

class MyGuard extends Guard {
    public MyGuard(Monitor monitor) {
        super(monitor);
    }

    @Override
    public boolean isSatisfied() {
        return true;
    }
}