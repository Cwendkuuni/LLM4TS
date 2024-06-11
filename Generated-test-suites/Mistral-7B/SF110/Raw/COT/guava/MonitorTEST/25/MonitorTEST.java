package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;

public class MonitorTest {

    private Monitor monitor;

    @Before
    public void setUp() {
        monitor = new Monitor();
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enter();
        monitor.enterInterruptibly();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenGuardIsSatisfied() throws InterruptedException {
        Guard guard = new MyGuard(monitor);
        monitor.enterWhen(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenGuardIsNotSatisfied() throws InterruptedException {
        Guard guard = new MyGuard(monitor);
        monitor.enterWhen(guard);
        monitor.leave(); // Let's assume that a thread satisfied the guard condition
        monitor.enterWhen(guard); // This should throw an exception
    }

    @Test
    public void testEnterWhenWithTimeOut() throws InterruptedException {
        Guard guard = new MyGuard(monitor);
        boolean entered = monitor.enterWhen(guard, 1L, TimeUnit.SECONDS);
        assertTrue(entered);
        monitor.leave();
    }

    @Test
    public void testEnterWhenWithTimeOutAndInterrupted() throws InterruptedException {
        Guard guard = new MyGuard(monitor);
        monitor.enterInterruptibly();
        monitor.enterWhen(guard, 1L, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIf() {
        Guard guard = new MyGuard(monitor);
        monitor.enter();
        assertTrue(monitor.enterIf(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        Guard guard = new MyGuard(monitor);
        monitor.enterInterruptibly();
        assertFalse(monitor.enterIfInterruptibly(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeOut() {
        Guard guard = new MyGuard(monitor);
        monitor.enter();
        assertTrue(monitor.enterIf(guard, 1L, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterIfWithTimeOutAndInterrupted() throws InterruptedException {
        Guard guard = new MyGuard(monitor);
        monitor.enterInterruptibly();
        assertFalse(monitor.enterIf(guard, 1L, TimeUnit.MILLISECONDS));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIf() {
        Guard guard = new MyGuard(monitor);
        monitor.enter();
        assertTrue(monitor.tryEnterIf(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testTryEnterIfInterruptibly() throws InterruptedException {
        Guard guard = new MyGuard(monitor);
        monitor.enterInterruptibly();
        assertFalse(monitor.tryEnterIfInterruptibly(guard));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithTimeOut() {
        Guard guard = new MyGuard(monitor);
        monitor.enter();
        assertTrue(monitor.tryEnterIf(guard, 1L, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testTryEnterIfWithTimeOutAndInterrupted() throws InterruptedException {
        Guard guard = new MyGuard(monitor);
        monitor.enterInterruptibly();
        assertFalse(monitor.tryEnterIf(guard, 1L, TimeUnit.MILLISECONDS));
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Guard guard = new MyWaitGuard(monitor);
        monitor.waitFor(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptibly() {
        Guard guard = new MyWaitGuard(monitor);
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeOut() throws InterruptedException {
        Guard guard = new MyWaitGuard(monitor);
        monitor.enter();
        assertTrue(monitor.waitFor(guard, 1L, TimeUnit.SECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeOutAndInterrupted() throws InterruptedException {
        Monitor monitor1 = new Monitor();
        Guard guard = new MyWaitGuard(monitor);
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor2.enterInterruptibly();
        monitor2.waitFor(guard, 1L, TimeUnit.MILLISECONDS);
        assertFalse(monitor2.isOccupiedByCurrentThread());
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsFair() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor(true);
        assertTrue(monitor1.isFair());
        assertTrue(monitor2.isFair());
        assertTrue(monitor2.isFair()); // Monitor constructed with a fairness argument also reports fair for new instances
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
        assertEquals(1, monitor.getQueueLength());
        monitor.leave();
        monitor.enter();
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enter();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Thread thread = new Thread();
        monitor.enter();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        monitor.enter();
        monitor.leave();
        assertTrue(monitor.hasQueuedThread(thread));
        assertFalse(monitor.hasQueuedThread(new Thread()));
    }

    private static class MyGuard extends Monitor.Guard {

        public boolean isSatisfied() {
            return true;
        }

    }

    private static class MyWaitGuard extends Monitor.Guard {

        public boolean isSatisfied() {
            return false;
        }

    }

}