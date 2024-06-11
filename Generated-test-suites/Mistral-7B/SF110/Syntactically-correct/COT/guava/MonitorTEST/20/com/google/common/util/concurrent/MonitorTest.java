package com.example.monitor;

import com.google.common.util.concurrent.Monitor;
import com.google.common.base.Preconditions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class MonitorTest {

    private Monitor monitor;

    @Before
    public void setup() {
        monitor = new Monitor();
    }

    @Test
    public void testMonitorConstructor() {
        Monitor actualMonitor = new Monitor();
        Assert.assertNotNull(actualMonitor);
    }

    @Test
    public void testMonitorConstructorWithFairFlag() {
        Monitor actualMonitor = new Monitor(true);
        Assert.assertNotNull(actualMonitor);
    }

    @Test(expected = NullPointerException.class)
    public void testEnterNullMonitor() {
        Monitor monitor = null;
        monitor.enter();
    }

    @Test
    public void testEnterNormalCase() {
        monitor.enter();
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyInterrupted() throws InterruptedException {
        Thread thread = Thread.currentThread();
        thread.interrupt();
        monitor.enterInterruptibly();
    }

    @Test
    public void testEnterInterruptiblyNormalCase() throws InterruptedException {
        monitor.enterInterruptibly();
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterNormalCase() {
        Assert.assertTrue(monitor.tryEnter());
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterBusyCase() {
        monitor.enter();
        Assert.assertFalse(monitor.tryEnter());
    }

    @Test
    public void testTryEnterInterruptiblyBusyCase() throws InterruptedException {
        monitor.enter();
        Thread thread = Thread.currentThread();
        thread.interrupt();
        Assert.assertFalse(monitor.tryEnterInterruptibly());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenNullGuard() {
        Monitor monitor = new Monitor();
        monitor.enterWhen(null);
    }

    @Test
    public void testEnterWhenGuardIsSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enterWhen(guard);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterWhenInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        Thread thread = Thread.currentThread();
        thread.interrupt();
        monitor.enterWhen(guard);
    }

    @Test
    public void testEnterWhenUninterruptiblyGuardIsSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enterWhenUninterruptibly(guard);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptiblyGuardIsNotSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enterWhenUninterruptibly(guard);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenNullGuardUninterruptibly() {
        Monitor monitor = new Monitor();
        monitor.enterWhenUninterruptibly(null);
    }

    @Test
    public void testEnterIfNormalCase() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        Assert.assertTrue(monitor.enterIf(guard));
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyNormalCase() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        Assert.assertTrue(monitor.enterIfInterruptibly(guard));
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfNullGuard() {
        Monitor monitor = new Monitor();
        Assert.assertFalse(monitor.enterIf(null));
    }

    @Test
    public void testEnterIfNullGuardInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Assert.assertFalse(monitor.enterIfInterruptibly(null));
    }

    @Test
    public void testEnterIfTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        Assert.assertFalse(monitor.enterIf(guard, 0, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testEnterIfInterruptiblyTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        Thread thread = Thread.currentThread();
        thread.interrupt();
        Assert.assertFalse(monitor.enterIfInterruptibly(guard, 0, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testTryEnterIfNormalCase() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        Assert.assertTrue(monitor.tryEnterIf(guard));
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIfBusyCase() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enter();
        Assert.assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testWaitForNullGuard() {
        Monitor monitor = new Monitor();
        try {
            monitor.waitFor(null);
        } catch (IllegalMonitorStateException e) {
            Assert.assertEquals("guard must not be null", e.getMessage());
        }
    }

    @Test
    public void testWaitForUninterruptiblyNullGuard() {
        Monitor monitor = new Monitor();
        monitor.waitForUninterruptibly(null);
    }

    @Test
    public void testWaitForNullMonitor() {
        Guard guard = new DummyGuard(null);
        try {
            monitor.waitFor(guard);
        } catch (IllegalMonitorStateException e) {
            Assert.assertEquals("monitor must not be null", e.getMessage());
        }
    }

    @Test
    public void testWaitForGuardIsSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummySatisfiedGuard(monitor);
        monitor.waitFor(guard);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        Thread thread = Thread.currentThread();
        thread.interrupt();
        try {
            monitor.waitFor(guard);
        } catch (InterruptedException e) {
            Assert.assertEquals("interrupted", e.getMessage());
        }
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyGuardIsSatisfied() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummySatisfiedGuard(monitor);
        monitor.waitForUninterruptibly(guard);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyGuardIsNotSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.waitForUninterruptibly(guard);
    }

    @Test
    public void testWaitForUninterruptiblyNullMonitor() {
        Guard guard = new DummySatisfiedGuard(null);
        monitor.waitForUninterruptibly(guard);
    }

    @Test
    public void testWaitForLongTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        long timeout = 100;
        Assert.assertFalse(monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testWaitForUninterruptiblyLongTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        long timeout = 100;
        monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        Assert.assertEquals(false, monitor.isFair());

        Monitor monitorFair = new Monitor(true);
        Assert.assertEquals(true, monitorFair.isFair());
    }

    @Test
    public void testIsOccupied() {
        Monitor monitor = new Monitor();
        Assert.assertFalse(monitor.isOccupied());

        monitor.enter();
        Assert.assertTrue(monitor.isOccupied());

        monitor.leave();
        Assert.assertFalse(monitor.isOccupied());
    }

    @Test
    public void testIsOccupiedByCurrentThread() {
        Monitor monitor = new Monitor();

        monitor.enter();
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());

        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testGetOccupiedDepth() {
        Monitor monitor = new Monitor();
        Assert.assertEquals(0, monitor.getOccupiedDepth());

        monitor.enter();
        Assert.assertEquals(1, monitor.getOccupiedDepth());

        monitor.enter();
        Assert.assertEquals(2, monitor.getOccupiedDepth());

        monitor.leave();
        Assert.assertEquals(1, monitor.getOccupiedDepth());

        monitor.leave();
        Assert.assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        Assert.assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Assert.assertEquals(false, monitor.hasQueuedThreads());

        Monitor monitorWithThread = new Monitor();
        monitorWithThread.enter();
        Assert.assertEquals(true, monitorWithThread.hasQueuedThreads());

        monitorWithThread.leave();
        Assert.assertEquals(false, monitorWithThread.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread();
        Assert.assertEquals(false, monitor.hasQueuedThread(thread));

        monitor.enter();
        monitor.addThreadToQueue(thread);
        Assert.assertEquals(true, monitor.hasQueuedThread(thread));

        monitor.leave();
        Assert.assertEquals(false, monitor.hasQueuedThread(thread));
    }

    @Test
    public void testSignalNextWaiter() {
        Monitor monitor = new Monitor();
        Guard guard1 = new DummyGuard(monitor);
        Guard guard2 = new DummyGuard(monitor);
        monitor.enter();
        monitor.await(guard1, true);
        monitor.signalNextWaiter();
        Assert.assertTrue(guard2.isSatisfied());
    }

    @Test
    public void testIsSatisfied() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        Assert.assertTrue(guard.isSatisfied());
    }

    static class DummyGuard implements Monitor.Guard {
        private final Monitor monitor;

        DummyGuard(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }
    }

    static class DummySatisfiedGuard extends DummyGuard {
        DummySatisfiedGuard(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }
    }
}