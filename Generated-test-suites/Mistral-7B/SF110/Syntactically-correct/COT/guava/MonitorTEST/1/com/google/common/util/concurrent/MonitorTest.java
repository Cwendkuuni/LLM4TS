package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

public class MonitorTest {

    private Monitor monitor;

    @Before
    public void setup() {
        monitor = new Monitor();
    }

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        assertNull(monitor.activeGuards);
        assertEquals(false, monitor.isFair());
        assertFalse(monitor.isOccupied());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithoutMonitorLockThrowsException() {
        monitor.enter();
        monitor.enter(); // This call should be inside the locked block
        fail("Expected IllegalMonitorStateException to be thrown");
    }

    @Test
    public void testEnterMethod() {
        monitor.enter();
        assertFalse(monitor.isOccupiedByCurrentThread());
        assertTrue(monitor.isOccupied());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyMethodThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
        Thread interrupter = new Thread(() -> monitor.interrupt());
        interrupter.start();
        monitor.enterInterruptibly();
        interrupter.join();
    }

    @Test
    public void testEnterInterruptiblyMethodDoesNotInterruptWhenNotInterrupted() throws InterruptedException {
        Thread interrupter = new Thread(() -> monitor.interrupt());
        interrupter.start();
        monitor.enterInterruptibly();
        interrupter.join();
        assertFalse(Thread.interrupted());
    }

    @Test
    public void testTryEnterMethod() {
        assertFalse(monitor.tryEnter());
        assertTrue(monitor.isOccupied());
    }

    @Test
    public void testTryEnterMethodWhenMonitorIsNotHeldByCurrentThread() {
        ReentrantLock lock = new ReentrantLock();
        assertFalse(monitor.tryEnter());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterMethodWhenMonitorIsHeldByCurrentThread() {
        monitor.enter();
        assertTrue(monitor.tryEnter());
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenMethod() throws InterruptedException {
        Condition condition = monitor.lock.newCondition();
        Guard guard = new WaitConditionGuard(monitor, condition);
        monitor.enterWhen(guard);
        assertTrue(condition.await());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenMethodWithInvalidGuardThrowsException() throws InterruptedException {
        Condition condition = monitor.lock.newCondition();
        Monitor otherMonitor = new Monitor();
        Guard guard = new WaitConditionGuard(otherMonitor, condition);
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterWhenUninterruptiblyMethod() {
        Condition condition = monitor.lock.newCondition();
        Guard guard = new WaitConditionGuard(monitor, condition);
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(condition.await());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenMethodWithInterruptedLockThrowsInterruptedException() throws InterruptedException, TimeoutException {
        Condition condition = monitor.lock.newCondition();
        Guard guard = new WaitConditionGuard(monitor, condition);
        monitor.lock.lockInterruptibly();
        Thread interrupter = new Thread(() -> monitor.interrupt());
        interrupter.start();
        monitor.enterWhen(guard);
        interrupter.join();
    }

    @Test
    public void testEnterWhenMethodWithTimeout() throws InterruptedException, TimeoutException {
        Condition condition = monitor.lock.newCondition();
        Guard guard = new WaitConditionGuard(monitor, condition);
        monitor.lock.lock();
        monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
        assertFalse(condition.await(10, TimeUnit.MILLISECONDS));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptiblyMethodWithTimeout() {
        Condition condition = monitor.lock.newCondition();
        Guard guard = new WaitConditionGuard(monitor, condition);
        monitor.lock.lock();
        monitor.enterWhenUninterruptibly(guard, 10, TimeUnit.MILLISECONDS);
        assertTrue(condition.await(10, TimeUnit.MILLISECONDS));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenWithInvalidMonitorThrowsException() throws InterruptedException {
        Condition condition = monitor.lock.newCondition();
        Guard guard = new WaitConditionGuard(monitor, condition);
        Monitor otherMonitor = new Monitor();
        otherMonitor.enterWhen(guard);
    }

    @Test
    public void testEnterIfMethod() {
        Monitor otherMonitor = new Monitor();
        Guard guard = new PassConditionGuard(otherMonitor);
        assertTrue(monitor.enterIf(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfMethodWithMonitorLockedByCurrentThread() {
        monitor.enter();
        assertTrue(monitor.enterIf(new PassConditionGuard(monitor)));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfMethodWithMonitorLockedByOtherThread() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        assertFalse(monitor.enterIf(new PassConditionGuard(otherMonitor)));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyMethod() throws InterruptedException {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        monitor.enterInterruptibly();
        monitor.enterInterruptibly();
        assertTrue(monitor.enterIfInterruptibly(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyMethodWithMonitorLockedByCurrentThread() throws InterruptedException {
        monitor.enter();
        Guard guard = new PassConditionGuard(monitor);
        monitor.enterInterruptibly();
        monitor.enterInterruptibly();
        assertFalse(monitor.enterIfInterruptibly(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyMethodWithMonitorLockedByOtherThread() throws InterruptedException {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(monitor);
        monitor.enterInterruptibly();
        monitor.enterInterruptibly();
        assertTrue(monitor.enterIfInterruptibly(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfMethodWithConditionSatisfied() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        guard.condition.signal();
        assertTrue(monitor.enterIf(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfMethodWithConditionNotSatisfied() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        assertFalse(monitor.enterIf(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyMethodWithConditionSatisfied() throws InterruptedException {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        otherMonitor.condition.signal();
        monitor.enterInterruptibly();
        monitor.enterInterruptibly();
        assertTrue(monitor.enterIfInterruptibly(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyMethodWithConditionNotSatisfied() throws InterruptedException {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        monitor.enterInterruptibly();
        monitor.enterInterruptibly();
        assertFalse(monitor.enterIfInterruptibly(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfMethodWithConditionSatisfiedAfterTimeout() throws InterruptedException, TimeoutException {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        otherMonitor.condition.signal();
        monitor.enterInterruptibly();
        monitor.enterInterruptibly();
        assertFalse(monitor.enterIf(guard, 10, TimeUnit.MILLISECONDS));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfMethodWithConditionNotSatisfiedAfterTimeout() throws InterruptedException, TimeoutException {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        monitor.enterInterruptibly();
        monitor.enterInterruptibly();
        assertFalse(monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyMethod() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(guard.condition.await());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyMethodWithMonitorLockedByCurrentThread() {
        monitor.enter();
        Guard guard = new PassConditionGuard(monitor);
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(guard.condition.await());
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyMethodWithMonitorLockedByOtherThread() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(monitor);
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(guard.condition.await());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyMethodWithConditionSatisfied() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        otherMonitor.condition.signal();
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(guard.condition.await());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyMethodWithConditionNotSatisfied() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        monitor.enterWhenUninterruptibly(guard);
        assertFalse(guard.condition.await());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyMethodWithConditionNotSatisfiedAndInterrupted() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        monitor.enterWhenInterruptibly(guard);
        monitor.interrupt();
        monitor.enterWhenUninterruptibly(guard);
        assertFalse(guard.condition.await());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfUninterruptiblyMethodWithConditionSatisfiedAndInterrupted() {
        Monitor otherMonitor = new Monitor();
        otherMonitor.enter();
        Guard guard = new PassConditionGuard(otherMonitor);
        otherMonitor.condition.signal();
        monitor.enterWhenInterruptibly(guard);
        monitor.interrupt();
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(guard.condition.await());
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsFair() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor(true);
        assertEquals(false, monitor1.isFair());
        assertEquals(true, monitor2.isFair());
    }

    @Test
    public void testIsOccupied() {
        Monitor monitor1 = new Monitor();
        monitor1.enter();
        assertTrue(monitor1.isOccupied());
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        Monitor monitor1 = new Monitor();
        monitor1.enter();
        assertTrue(monitor1.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() {
        Monitor monitor1 = new Monitor();
        monitor1.enter();
        monitor1.enter();
        assertEquals(2, monitor1.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor1 = new Monitor();
        Condition condition1 = monitor1.lock.newCondition();
        Condition condition2 = monitor1.lock.newCondition();
        monitor1.enter();
        Guard guard1 = new WaitConditionGuard(monitor1, condition1);
        Guard guard2 = new WaitConditionGuard(monitor1, condition2);
        condition1.await();
        assertEquals(1, monitor1.getQueueLength());
        condition2.signal();
        assertEquals(0, monitor1.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor1 = new Monitor();
        Condition condition1 = monitor1.lock.newCondition();
        Condition condition2 = monitor1.lock.newCondition();
        monitor1.enter();
        Guard guard1 = new WaitConditionGuard(monitor1, condition1);
        Guard guard2 = new WaitConditionGuard(monitor1, condition2);
        condition1.await();
        assertTrue(monitor1.hasQueuedThreads(guard2));
        condition2.signal();
        assertFalse(monitor1.hasQueuedThreads(guard2));
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor1 = new Monitor();
        Condition condition1 = monitor1.lock.newCondition();
        Condition condition2 = monitor1.lock.newCondition();
        monitor1.enter();
        Thread thread1 = new Thread(() -> monitor1.enterWhen(guard1));
        Thread thread2 = new Thread(() -> monitor1.enterWhen(guard2));
        thread1.start();
        thread2.start();
        thread1.join();
        assertTrue(monitor1.hasQueuedThread(thread2));
        monitor1.leave();
        assertFalse(monitor1.hasQueuedThread(thread2));
    }

    @Test
    public void testLeave() {
        Monitor monitor1 = new Monitor();
        Condition condition1 = monitor1.lock.newCondition();
        Condition condition2 = monitor1.lock.newCondition();
        monitor1.enter();
        Guard guard1 = new WaitConditionGuard(monitor1, condition1);
        Guard guard2 = new WaitConditionGuard(monitor1, condition2);
        condition1.await();
        monitor1.leave();
        assertFalse(monitor1.isOccupiedByCurrentThread());
        condition2.signal();
        assertTrue(guard2.condition.await());
    }

    private static class WaitConditionGuard extends Guard {
        public Condition condition;

        WaitConditionGuard(Monitor monitor, Condition condition) {
            super(monitor);
            this.condition = condition;
        }

        @Override
        public boolean isSatisfied() {
            return condition.isSignaled();
        }
    }

    private static class PassConditionGuard extends Guard {
        public PassConditionGuard(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }
    }
}