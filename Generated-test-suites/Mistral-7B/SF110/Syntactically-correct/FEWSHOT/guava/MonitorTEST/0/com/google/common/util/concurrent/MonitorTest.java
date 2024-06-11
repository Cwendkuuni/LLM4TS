package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;
import org.junit.*;

@Beta
public final class MonitorTest {

    private static Monitor monitor;
    private static ReentrantLock lock;
    private static Guard guard;

    @BeforeClass
    public static void setUpClass() {
        monitor = new Monitor();
        lock = monitor.lock;
        guard = new GuardMonitor();
    }

    @Test
    public void testMonitorConstruction() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor(true);
        assertEquals(false, monitor1.isFair());
        assertEquals(true, monitor2.isFair());
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithInvalidGuard() {
        monitor.enterWhen(new InvalidGuard());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWithTimeout() throws InterruptedException {
        monitor.enter(1, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
        monitor.enterInterruptibly(1, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIf() {
        monitor.enterIf(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeout() {
        monitor.enterIf(guard, 1, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        monitor.enterIfInterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyWithTimeout() throws InterruptedException {
        monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithInvalidGuard() {
        monitor.enterIf(new InvalidGuard());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        monitor.enterWhen(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenWithTimeout() throws InterruptedException {
        monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeout() {
        monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenWithInvalidGuard() {
        monitor.enterWhen(new InvalidGuard());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        assertFalse(monitor.tryEnter());
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIf() {
        assertTrue(monitor.tryEnterIf(guard));
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithTimeout() {
        assertFalse(monitor.tryEnterIf(guard, 1, TimeUnit.SECONDS));
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfInterruptibly() throws InterruptedException {
        assertTrue(monitor.tryEnterIfInterruptibly(guard));
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfInterruptiblyWithTimeout() throws InterruptedException {
        assertFalse(monitor.tryEnterIfInterruptibly(guard, 1, TimeUnit.SECONDS));
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithInvalidGuard() {
        assertFalse(monitor.tryEnterIf(new InvalidGuard()));
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        monitor.waitFor(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptibly() {
        monitor.waitForUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForWithTimeout() throws InterruptedException {
        monitor.waitFor(guard, 1, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyWithTimeout() {
        monitor.waitForUninterruptibly(guard, 1, TimeUnit.SECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testLeave() {
        monitor.enter();
        monitor.leave();
        assertFalse(lock.isHeldByCurrentThread());
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
        assertEquals(0, monitor.getQueueLength());
        Guard guard1 = new GuardMonitor();
        Guard guard2 = new GuardMonitor();
        monitor.enterWhen(guard1);
        monitor.enterWhen(guard2);
        assertEquals(2, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        monitor.enter();
        monitor.enter();
        assertEquals(false, monitor.hasQueuedThreads(guard));
        Guard guard1 = new GuardMonitor();
        Guard guard2 = new GuardMonitor();
        monitor.enterWhen(guard1);
        monitor.enterWhen(guard2);
        assertEquals(true, monitor.hasQueuedThreads(guard));
    }

    @Test
    public void testHasQueuedThread() {
        monitor.enter();
        monitor.enter();
        assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
        Guard guard1 = new GuardMonitor();
        Guard guard2 = new GuardMonitor();
        monitor.enterWhen(guard1);
        monitor.enterWhen(guard2);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                monitor.enterWhen(guard);
            }
        });
        monitor.enter();
        assertTrue(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testSignalNextWaiter() {
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.signalNextWaiter();
        monitor.waitFor(guard);
        assertEquals(false, lock.isHeldByCurrentThread());
    }

    @Test
    public void testIsSatisfied() {
        Guard guard1 = new SatisfiedGuard();
        Guard guard2 = new UnsatisfiedGuard();
        assertEquals(true, guard1.isSatisfied());
        assertEquals(false, guard2.isSatisfied());
    }

    @Test
    public void testSignalAllWaiters() {
        monitor.enter();
        monitor.enterWhen(guard1);
        monitor.enterWhen(guard2);
        monitor.signalAllWaiters();
        monitor.waitFor(guard1);
        monitor.waitFor(guard2);
        assertEquals(false, lock.isHeldByCurrentThread());
    }

    @Test
    public void testBeginWaitingFor() {
        monitor.enter();
        monitor.enterWhen(guard);
        monitor.beginWaitingFor(guard);
        assertEquals(true, guard.next == null);
        assertEquals(true, guard.waiterCount == 1);
    }

    @Test
    public void testEndWaitingFor() {
        monitor.enter();
        monitor.enterWhen(guard1);
        monitor.enterWhen(guard2);
        monitor.endWaitingFor(guard1);
        assertEquals(guard2, guard1.next);
        assertEquals(0, guard1.waiterCount);
    }

    @Test
    public void testAwait() throws InterruptedException {
        monitor.enter();
        monitor.await(guard, true);
        monitor.leave();
        assertEquals(false, lock.isHeldByCurrentThread());
    }

    @Test
    public void testAwaitUninterruptibly() {
        monitor.enter();
        monitor.awaitUninterruptibly(guard, true);
        monitor.leave();
        assertEquals(false, lock.isHeldByCurrentThread());
    }

    @Test
    public void testAwaitNanos() throws InterruptedException {
        monitor.enter();
        long timeoutNanos = 100;
        monitor.awaitNanos(guard, timeoutNanos);
        monitor.leave();
        assertEquals(false, lock.isHeldByCurrentThread());
    }

    private static class GuardMonitor extends Monitor.Guard {
        @Override
        public boolean isSatisfied() {
            return true;
        }
    }

    private static class SatisfiedGuard extends Monitor.Guard {
        @Override
        public boolean isSatisfied() {
            return true;
        }
    }

    private static class UnsatisfiedGuard extends Monitor.Guard {
        @Override
        public boolean isSatisfied() {
            return false;
        }
    }

    private static class InvalidGuard extends Monitor.Guard {
        @Override
        public Monitor getMonitor() {
            throw new IllegalStateException("Monitor is null");
        }
    }
}