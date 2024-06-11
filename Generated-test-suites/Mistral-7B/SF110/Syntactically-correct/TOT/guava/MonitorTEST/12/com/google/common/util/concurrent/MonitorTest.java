package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor.Guard;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

public class MonitorTest {

    @Test
    public void testEnterAndLeave() {
        Monitor monitor = new Monitor();
        try {
            monitor.enter();
            Assert.assertTrue(monitor.isOccupiedByCurrentThread());
            monitor.leave();
            Assert.assertFalse(monitor.isOccupiedByCurrentThread());
        } finally {
            monitor.leave();
        }
    }

    @Test
    public void testEnterInterruptibly() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread(() -> {
            try {
                monitor.enterInterruptibly();
                Assert.fail("InterruptedException should be thrown");
            } catch (InterruptedException e) {
                // expected
            }
        });
        monitor.enter();
        thread.start();
        try {
            thread.join(100);
        } catch (InterruptedException e) {
            // ignored
        }
        monitor.leave();
    }

    @Test
    public void testEnterWhen() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardCondition(monitor, () -> true);
        try {
            monitor.enterWhen(guard);
            Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        } finally {
            monitor.leave();
        }
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWhenWithDifferentMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        Guard guard = new GuardCondition(monitor1, () -> true);
        monitor2.enterWhen(guard);
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardCondition(monitor, () -> true);
        monitor.enterWhenUninterruptibly(guard);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterWhenWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardCondition(monitor, () -> false);
        try {
            monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
            Assert.fail("Expected timeout exception");
        } catch (TimeoutException e) {
            // expected
        }
        monitor.leave();

        try {
            monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
            Assert.fail("Expected timeout exception");
        } catch (TimeoutException e) {
            // expected
        }
        monitor.leave();

        Thread thread = new Thread(() -> {
            try {
                monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                Assert.fail("Expected to enter the monitor");
            }
        });
        monitor.enter();
        thread.start();
        try {
            thread.join(1);
        } catch (InterruptedException e) {
            // ignored
        }
        monitor.leave();
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardIfCondition(monitor, () -> true);
        monitor.enterIf(guard);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();
    }

    @Test
    public void testEnterIfInterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardIfCondition(monitor, () -> false);
        try {
            monitor.enterIfInterruptibly(guard);
            Assert.fail("Expected InterruptedException");
        } catch (InterruptedException e) {
            // expected
        }
        monitor.leave();

        Guard guard2 = new GuardIfCondition(monitor, () -> true);
        monitor.enterIf(guard2);
        monitor.leave();
        monitor.enterIfInterruptibly(guard2);
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardIfCondition(monitor, () -> false);
        try {
            monitor.enterIf(guard, 1, TimeUnit.SECONDS);
            Assert.fail("Expected timeout exception");
        } catch (TimeoutException e) {
            // expected
        }
        monitor.leave();

        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
        Assert.assertTrue(monitor.isOccupiedByCurrentThread());
        monitor.leave();

        Thread thread = new Thread(() -> {
            try {
                monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                Assert.fail("Expected to enter the monitor");
            }
        });
        monitor.enter();
        thread.start();
        try {
            thread.join(1);
        } catch (InterruptedException e) {
            // ignored
        }
        monitor.leave();
    }

    @Test
    public void testEnterIfWithDifferentMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        Guard guard = new GuardIfCondition(monitor1, () -> true);
        try {
            monitor2.enterIf(guard);
        } catch (IllegalMonitorStateException e) {
            // expected
        }
    }

    @Test
    public void testWaitFor() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardCondition(monitor, () -> false);
        try {
            monitor.enter();
            monitor.waitFor(guard);
            Assert.assertTrue(guard.isSatisfied());
            monitor.leave();
        } catch (InterruptedException e) {
            // ignored
        }
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardCondition(monitor, () -> false);
        monitor.enter();
        monitor.enterWhenUninterruptibly(guard);
        Assert.assertTrue(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testWaitForWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardCondition(monitor, () -> false);
        try {
            monitor.enter();
            monitor.waitFor(guard, 1, TimeUnit.SECONDS);
            Assert.assertFalse("Expected timeout exception", guard.isSatisfied());
            monitor.leave();
        } catch (TimeoutException e) {
            // expected
        }

        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        Assert.assertFalse("Expected timeout exception", guard.isSatisfied());
        monitor.leave();

        Thread thread = new Thread(() -> {
            try {
                monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                Assert.fail("Expected to wait for the condition");
            }
        });
        monitor.enter();
        thread.start();
        try {
            thread.join(1);
        } catch (InterruptedException e) {
            // ignored
        }
        monitor.leave();
    }

    @Test
    public void testWaitForWithDifferentMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        Guard guard = new GuardCondition(monitor1, () -> true);
        try {
            monitor2.enter();
            monitor2.waitFor(guard);
        } catch (IllegalMonitorStateException e) {
            // expected
        }
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        Assert.assertFalse(monitor.isOccupiedByCurrentThread());
    }

    private static class GuardCondition implements Monitor.Guard {
        private final Monitor monitor;
        private final boolean condition;

        public GuardCondition(Monitor monitor, boolean condition) {
            this.monitor = Preconditions.checkNotNull(monitor, "monitor");
            this.condition = condition;
        }

        @Override
        public boolean isSatisfied() {
            return condition;
        }
    }

    private static class GuardIfCondition implements Monitor.Guard {
        private final Monitor monitor;
        private final boolean condition;

        public GuardIfCondition(Monitor monitor, boolean condition) {
            this.monitor = Preconditions.checkNotNull(monitor, "monitor");
            this.condition = condition;
        }

        @Override
        public boolean isSatisfied() {
            return condition;
        }
    }

    private static class Condition {
        public void signal() {
            // no-op
        }

        public void signalAll() {
            // no-op
        }

        public long awaitNanos(long nanos) throws InterruptedException {
            Thread.sleep(nanos);
            return 0;
        }

        public long awaitNanos(long nanos, boolean interrutpible) throws InterruptedException {
            Thread.sleep(nanos);
            if (!interrutpible) {
                Thread.currentThread().interrupt();
            }
            return 0;
        }

        public void await() throws InterruptedException {
            Thread.currentThread().join();
        }

        public void awaitUninterruptibly() {
            // no-op
        }
    }
}