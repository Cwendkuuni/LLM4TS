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

    @Test
    public void testMonitorConstruction() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.enter();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly();
            } catch (InterruptedException e) {
                fail();
            }
        });
        monitor.enter();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        thread.start();
        thread.join();
    }

    @Test
    public void testEnterWithTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 100;
        boolean acquired = monitor.enterWhen(timeout, TimeUnit.MILLISECONDS);
        assertFalse(acquired);
        monitor.leave();
        monitor.enter();
        monitor.leave();
        monitor.enterWhen(timeout, TimeUnit.MILLISECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptiblyWithTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 100;
        assertTrue(monitor.enterInterruptiblyWhen(timeout, TimeUnit.MILLISECONDS));
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.enterInterruptiblyWhen(timeout, TimeUnit.MILLISECONDS));
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.tryEnter());
        monitor.leave();
        assertFalse(monitor.tryEnter());
    }

    @Test
    public void testEnterWhenGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.enterWhen(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enterWhen(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testEnterWhenUninterruptiblyGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.enterWhenUninterruptibly(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testEnterWhenGuardWithTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 100;
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        assertTrue(monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS));
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        assertTrue(monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptiblyGuardWithTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 100;
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        assertTrue(monitor.enterWhenUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS));
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        assertTrue(monitor.enterWhenUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.enterIf(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enterIf(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testEnterIfInterruptiblyGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.enterIfInterruptibly(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enterIfInterruptibly(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testEnterIfGuardWithTimeOut() {
        Monitor monitor = new Monitor();
        long timeout = 100;
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptiblyGuardWithTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 100;
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.enterIfInterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        monitor.enterIfInterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIfGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        assertTrue(monitor.tryEnterIf(guard));
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.tryEnterIf(guard));
    }

    @Test
    public void testWaitForGuard() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.waitFor(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        monitor.waitFor(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testWaitForUninterruptiblyGuard() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.waitForUninterruptibly(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        monitor.waitForUninterruptibly(guard);
        assertTrue(guard.isSatisfied());
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testWaitForGuardWithTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 100;
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyGuardWithTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 100;
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
        monitor.leave();
        assertFalse(guard.isSatisfied());
        monitor.enter();
        monitor.leave();
        monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.isFair());
        Monitor unfairMonitor = new Monitor(false);
        assertFalse(unfairMonitor.isFair());
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
        int depth = monitor.getOccupiedDepth();
        assertEquals(0, depth);
        monitor.enter();
        assertEquals(1, monitor.getOccupiedDepth());
        monitor.leave();
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        monitor.enter();
        Thread thread = new Thread(() -> monitor.enter());
        thread.start();
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        thread.join();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> monitor.enter());
        thread.start();
        assertTrue(monitor.hasQueuedThread(thread));
        monitor.leave();
        thread.join();
        assertFalse(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testHasWaiters() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.enter();
        monitor.enterWhen(guard);
        assertTrue(monitor.hasWaiters(guard));
        monitor.leave();
        monitor.leave();
        assertFalse(monitor.hasWaiters(guard));
    }

    @Test
    public void testGetWaitQueueLength() {
        Monitor monitor = new Monitor();
        long timeout = 100;
        Guard guard = new GuardWithCondition(monitor, new ReentrantLock());
        monitor.enter();
        monitor.enterWhen(guard);
        assertEquals(1, monitor.getWaitQueueLength(guard));
        monitor.leave();
        monitor.leave();
        assertEquals(0, monitor.getWaitQueueLength(guard));
    }

}

class GuardWithCondition implements Monitor.Guard {
    private final Monitor monitor;
    private final ReentrantLock lock;
    private volatile boolean isSatisfied = false;

    public GuardWithCondition(Monitor monitor, ReentrantLock lock) {
        this.monitor = monitor;
        this.lock = lock;
    }

    @Override
    public boolean isSatisfied() {
        return isSatisfied;
    }

    public void setSatisfied() {
        isSatisfied = true;
    }

    public void setCondition(Condition condition) {
        this.lock.unlock();
        this.lock = condition;
    }
}

class Condition {
    public boolean await() throws InterruptedException {
        return false;
    }

    public boolean awaitUninterruptibly() {
        return false;
    }

    public long awaitNanos(long nanos) throws InterruptedException {
        Thread.sleep(nanos);
        return nanos;
    }

    public long awaitNanos(long nanos, TimeUnit unit) {
        return awaitNanos((long) unit.toNanos(nanos));
    }

    public boolean signal() {
        return false;
    }

    public boolean signalAll() {
        return false;
    }
}