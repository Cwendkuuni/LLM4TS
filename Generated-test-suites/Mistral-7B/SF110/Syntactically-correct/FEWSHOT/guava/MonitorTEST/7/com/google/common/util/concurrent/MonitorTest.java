package com.google.common.util.concurrent;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;

@Beta
public final class MonitorTest {

    @Test
    public void testMonitorCreation() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            monitor.enterInterruptibly();
        });
        monitor.enter();
        thread.start();
        thread.join();
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnter_timeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 1;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        boolean entered = monitor.enter(timeout, unit);
        Assert.assertFalse(entered);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterInterruptibly_timeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        long timeout = 1;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enterInterruptibly(timeout, unit);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        boolean canEnter = monitor.tryEnter();
        Assert.assertFalse(canEnter);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnter_canEnter() {
        Monitor monitor = new Monitor();
        monitor.enter();
        boolean canEnter = monitor.tryEnter();
        Assert.assertFalse(canEnter);
        monitor.leave();
        canEnter = monitor.tryEnter();
        Assert.assertTrue(canEnter);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enterWhen(guard);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enterWhenUninterruptibly(guard);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhen_timeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        long timeout = 1;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enterWhen(guard, timeout, unit);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWhenUninterruptibly_timeout() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        long timeout = 1;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enterWhenUninterruptibly(guard, timeout, unit);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enterIf(guard);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enterIfInterruptibly(guard);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIf_timeout() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        long timeout = 1;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enterIf(guard, timeout, unit);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptibly_timeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        long timeout = 1;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enterIfInterruptibly(guard, timeout, unit);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testTryEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enter();
        boolean canEnter = monitor.tryEnterIf(guard);
        Assert.assertFalse(canEnter);
        monitor.leave();
        canEnter = monitor.tryEnterIf(guard);
        Assert.assertTrue(canEnter);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitFor() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitFor_timeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        long timeout = 1;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enter();
        boolean satisfied = monitor.waitFor(guard, timeout, unit);
        Assert.assertFalse(satisfied);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptibly_timeout() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        long timeout = 1;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        monitor.enter();
        monitor.waitForUninterruptibly(guard, timeout, unit);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
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
        Assert.assertFalse(monitor.isFair());
        Monitor fairMonitor = new Monitor(true);
        Assert.assertTrue(fairMonitor.isFair());
    }

    @Test
    public void testIsOccupied() {
        Monitor monitor = new Monitor();
        Monitor occupiedMonitor = new Monitor();
        occupiedMonitor.enter();
        Assert.assertTrue(occupiedMonitor.isOccupied());
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
        Monitor occupiedMonitor = new Monitor();
        occupiedMonitor.enter();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(1, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Assert.assertFalse(monitor.hasQueuedThreads());
        Monitor occupiedMonitor = new Monitor();
        occupiedMonitor.enter();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(monitor.hasQueuedThread(thread));
    }

    @Test
    public void testHasWaiters() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        Assert.assertTrue(monitor.hasWaiters(guard));
        monitor.leave();
        Assert.assertFalse(monitor.hasWaiters(guard));
    }

    @Test
    public void testGetWaitQueueLength() {
        Monitor monitor = new Monitor();
        Guard guard = new DummyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        Assert.assertEquals(1, monitor.getWaitQueueLength(guard));
        monitor.leave();
        Assert.assertEquals(0, monitor.getWaitQueueLength(guard));
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
}