package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;

@Beta
public final class MonitorTest {

    @Test
    public void testMonitorCreation() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithoutLock() {
        Monitor monitor = new Monitor();
        monitor.enter();
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        monitor.leave();
    }

    @Test
    public void testEnterTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        assertFalse(monitor.enter(1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testEnterInterruptiblyTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        assertFalse(monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.tryEnter());
        monitor.leave();
    }

    @Test
    public void testTryEnterFailed() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.tryEnter());
        monitor.leave();
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        monitor.enterWhen(guard);
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        monitor.enterWhenUninterruptibly(guard);
        monitor.leave();
    }

    @Test
    public void testEnterWhenTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        assertFalse(monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        assertFalse(monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new MyFalseGuard();
        monitor.enterIf(guard);
        monitor.leave();
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MyFalseGuard();
        monitor.enterIfInterruptibly(guard);
        monitor.leave();
    }

    @Test
    public void testEnterIfTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyFalseGuard();
        assertFalse(monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testEnterIfInterruptiblyTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyFalseGuard();
        assertFalse(monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testTryEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new MyFalseGuard();
        assertTrue(monitor.tryEnterIf(guard));
        monitor.leave();
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        monitor.leave();
    }

    @Test
    public void testWaitForTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        assertFalse(monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testWaitForUninterruptiblyTimeout() throws InterruptedException, TimeoutException {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        assertFalse(monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS));
        monitor.leave();
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testLeaveWithoutLock() {
        Monitor monitor = new Monitor();
        monitor.leave();
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertTrue(monitor.isFair());
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
        assertEquals(2, monitor.getOccupiedDepth());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.enter();
        assertEquals(2, monitor.getQueueLength());
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.enter();
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread();
        monitor.enter();
        monitor.enter();
        monitor.beginWaitingFor(new MyGuard(monitor));
        thread.start();
        monitor.leave();
        monitor.leave();
        assertTrue(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testHasWaiters() {
        Monitor monitor = new Monitor();
        Guard guard = new MyGuard(monitor);
        monitor.enter();
        monitor.beginWaitingFor(guard);
        assertTrue(monitor.hasWaiters(guard));
        monitor.leave();
        assertFalse(monitor.hasWaiters(guard));
    }

    private static class MyGuard extends Guard {
        public MyGuard(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }
    }

    private static class MyFalseGuard extends Guard {
        public MyFalseGuard() {
            super(null);
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }
    }
}