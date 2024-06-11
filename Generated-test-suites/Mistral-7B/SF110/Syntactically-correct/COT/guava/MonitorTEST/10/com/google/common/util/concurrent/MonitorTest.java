package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

import static org.junit.Assert.*;

public class MonitorTest {
    private Monitor monitor;
    private Condition condition;

    @Before
    public void setUp() {
        monitor = new Monitor();
        condition = monitor.lock.newCondition();
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongMonitor() {
        Monitor otherMonitor = new Monitor();
        monitor.enter();
        otherMonitor.enter(); // should throw exception
        monitor.leave();
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptiblyWithInterruption() throws InterruptedException {
        monitor.enterInterruptibly();
        Thread interruptingThread = new Thread(() -> monitor.interrupt());
        interruptingThread.start();
        interruptingThread.join();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        assertFalse(monitor.tryEnter());
        monitor.enter();
        assertTrue(monitor.tryEnter());
        monitor.leave();
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testTryEnterWithInterruption() throws InterruptedException {
        monitor.enter();
        Thread interruptingThread = new Thread(() -> monitor.interrupt());
        interruptingThread.start();
        interruptingThread.join();
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testEnterWhenWithSatisfiedGuard() throws InterruptedException {
        final Guard guard = new SatisfiedGuard(monitor);
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenWithUnsatisfiedGuard() throws InterruptedException {
        final Guard guard = new UnsatisfiedGuard(monitor);
        monitor.enterWhen(guard);
        Thread.sleep(100); // give the guard some time to become satisfied
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenWithWrongMonitor() throws InterruptedException {
        final Monitor otherMonitor = new Monitor();
        Monitor monitorToTest = new Monitor();
        final Guard guard = new ConditionSatisfiedGuard(condition, otherMonitor);
        monitorToTest.enterWhen(guard);
        monitorToTest.leave();
        assertFalse(otherMonitor.isOccupiedByCurrentThread());
        monitorToTest.enterWhen(guard); // should throw exception
        monitorToTest.leave();
    }

    @Test
    public void testEnterIf() throws InterruptedException {
        final Guard guard = new ConditionSatisfiedGuard(condition);
        monitor.enterIf(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithInterruption() throws InterruptedException {
        final Guard guard = new ConditionSatisfiedInterruptibleGuard(condition);
        monitor.enterIfInterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithUnsatisfiedGuard() {
        final Guard guard = new UnsatisfiedGuard(monitor);
        assertFalse(monitor.enterIf(guard));
    }

    @Test
    public void testEnterIfWithInterruptedGuardAndInterruption() throws InterruptedException {
        final Guard guard = new InterruptedGuard(monitor);
        monitor.enterIfInterruptibly(guard);
        Thread.sleep(100); // give the guard some time to become interrupcible
        monitor.interrupt();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithInterruptedGuardAndNoInterruption() throws InterruptedException {
        final Guard guard = new InterruptedGuard(monitor);
        monitor.enterIfInterruptibly(guard);
        Thread.sleep(100); // give the guard some time to become interrupcible
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIf() {
        final Guard guard = new ConditionSatisfiedGuard(condition);
        assertTrue(monitor.tryEnterIf(guard));
        monitor.leave();
        assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testTryEnterIfWithInterruption() throws InterruptedException {
        final Guard guard = new InterruptedGuard(monitor);
        assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        final Guard guard = new ConditionSatisfiedGuard(condition);
        monitor.waitFor(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForWithInterruption() throws InterruptedException {
        final Guard guard = new InterruptedGuard(monitor);
        monitor.waitFor(guard);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        final Guard guard = new UnsatisfiedGuard(monitor);
        monitor.waitFor(guard, 100);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptibly() {
        final Guard guard = new ConditionSatisfiedGuard(condition);
        monitor.waitForUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        final Guard guard = new UnsatisfiedGuard(monitor);
        monitor.waitForUninterruptibly(guard, 100);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testLeaveWithNotHeldLock() {
        monitor.leave(); // should throw exception
    }

    @Test
    public void testIsFair() {
        Monitor fairMonitor = new Monitor(true);
        Monitor unfairMonitor = new Monitor(false);
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
        assertEquals(1, monitor.getOccupiedDepth());
        monitor.enter();
        assertEquals(2, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitorWithThreadWaiting = new Monitor();
        monitorWithThreadWaiting.enter();
        Monitor monitorWithThreadInterrupted = new Monitor();
        monitorWithThreadInterrupted.enterInterruptibly();
        Thread interruptedThread = new Thread(() -> monitorWithThreadInterrupted.interrupt());
        interruptedThread.start();
        assertEquals(2, monitorWithThreadWaiting.getQueueLength());
        monitorWithThreadWaiting.leave();
        assertEquals(1, monitorWithThreadInterrupted.getQueueLength());
        monitorWithThreadInterrupted.leave();
        assertEquals(0, monitorWithThreadInterrupted.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitorWithThreadWaiting = new Monitor();
        monitorWithThreadWaiting.enter();
        assertTrue(monitorWithThreadWaiting.hasQueuedThreads());
        monitorWithThreadWaiting.leave();
        assertFalse(monitorWithThreadWaiting.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitorWithThreadWaiting = new Monitor();
        monitorWithThreadWaiting.enter();
        Thread threadWaiting = new Thread(() -> monitorWithThreadWaiting.interrupt());
        threadWaiting.start();
        assertTrue(monitorWithThreadWaiting.hasQueuedThread(threadWaiting));
        monitorWithThreadWaiting.leave();
        assertFalse(monitorWithThreadWaiting.hasQueuedThread(threadWaiting));
    }

    static class SatisfiedGuard extends Monitor.Guard {
        public SatisfiedGuard(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }
    }

    static class UnsatisfiedGuard extends Monitor.Guard {
        public UnsatisfiedGuard(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }
    }

    static class ConditionSatisfiedGuard extends Monitor.Guard {
        private final Condition condition;

        public ConditionSatisfiedGuard(Condition condition) {
            super(condition.monitor);
            this.condition = condition;
        }

        @Override
        public boolean isSatisfied() {
            return condition.isSignaled();
        }
    }

    static class ConditionSatisfiedInterruptibleGuard extends Monitor.Guard {
        private final Condition condition;

        public ConditionSatisfiedInterruptibleGuard(Condition condition) {
            super(condition.monitor);
            this.condition = condition;
        }

        @Override
        public boolean isSatisfied() throws InterruptedException {
            return condition.await();
        }
    }

    static class InterruptedGuard extends Monitor.Guard {
        private final Monitor monitor;

        public InterruptedGuard(Monitor monitor) {
            super(monitor);
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() throws InterruptedException {
            Thread.interrupted();
            return false;
        }
    }
}