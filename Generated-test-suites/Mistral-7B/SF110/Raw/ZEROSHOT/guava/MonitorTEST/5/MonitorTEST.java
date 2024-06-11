package com.google.common.util.concurrent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

public class MonitorTest {

    private Monitor monitor;
    private Lock lock;
    private Condition condition;

    @Before
    public void setUp() {
        monitor = new Monitor();
        lock = monitor.lock;
        condition = monitor.lock.newCondition();
    }

    @After
    public void tearDown() {
        monitor = null;
        lock = null;
        condition = null;
    }

    @Test
    public void testConstructor() {
        assertEquals(false, monitor.isFair());
        assertNull(monitor.getActiveGuards());
    }

    @Test
    public void testEnter() {
        monitor.enter();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongMonitor() {
        Monitor otherMonitor = new Monitor();
        monitor.enter();
        otherMonitor.enter();
    }

    @Test
    public void testEnterInterruptibly() throws InterruptedException {
        monitor.enterInterruptibly();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenWithSatisfiedGuard() throws InterruptedException {
        final Guard guard = new GuardWithSatisfiedCondition(monitor);
        monitor.enterWhen(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenWithUnsatisfiedGuard() throws InterruptedException {
        final Guard guard = new GuardWithUnsatisfiedCondition(monitor);
        monitor.enterWhen(guard);
        monitor.waitFor(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenWithInterruptibleGuard() throws InterruptedException {
        final Guard guard = new GuardWithInterruptibleCondition(monitor);
        monitor.enterWhenInterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterWhenWithUninterruptibleGuard() {
        final Guard guard = new GuardWithUninterruptibleCondition(monitor);
        monitor.enterWhenUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithSatisfiedGuard() {
        final Guard guard = new GuardWithSatisfiedCondition(monitor);
        monitor.enterIf(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithUnsatisfiedGuard() {
        final Guard guard = new GuardWithUnsatisfiedCondition(monitor);
        monitor.enterIf(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithInterruptibleGuard() throws InterruptedException {
        final Guard guard = new GuardWithInterruptibleCondition(monitor);
        monitor.enterIfInterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithUninterruptibleGuard() {
        final Guard guard = new GuardWithUninterruptibleCondition(monitor);
        monitor.enterIfUninterruptibly(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeout() {
        final Guard guard = new GuardWithSatisfiedCondition(monitor);
        monitor.enterIf(guard, 0, TimeUnit.MILLISECONDS);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeoutAndNotSatisfied() {
        final Guard guard = new GuardWithUnsatisfiedCondition(monitor);
        monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithSatisfiedGuard() {
        final Guard guard = new GuardWithSatisfiedCondition(monitor);
        monitor.tryEnterIf(guard);
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithUnsatisfiedGuard() {
        final Guard guard = new GuardWithUnsatisfiedCondition(monitor);
        monitor.tryEnterIf(guard);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithTimeout() {
        final Guard guard = new GuardWithSatisfiedCondition(monitor);
        assertTrue(monitor.tryEnterIf(guard, 1, TimeUnit.MILLISECONDS));
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithTimeoutAndNotSatisfied() {
        final Guard guard = new GuardWithUnsatisfiedCondition(monitor);
        assertFalse(monitor.tryEnterIf(guard, 1, TimeUnit.MILLISECONDS));
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryEnterIfWithTimeoutAndInterrupted() throws InterruptedException {
        final Guard guard = new GuardWithInterruptibleCondition(monitor);
        monitor.tryEnterIf(guard, 1, TimeUnit.MILLISECONDS);
        assertFalse(lock.isHeldByCurrentThread());
        assertTrue(Thread.currentThread().isInterrupted());
    }

    public static class GuardWithSatisfiedCondition extends Guard {
        public GuardWithSatisfiedCondition(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }
    }

    public static class GuardWithUnsatisfiedCondition extends Guard {
        public GuardWithUnsatisfiedCondition(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() {
            return false;
        }
    }

    public static class GuardWithInterruptibleCondition extends Guard {
        public GuardWithInterruptibleCondition(Monitor monitor) {
            super(monitor);
        }

        @Override
        public boolean isSatisfied() throws InterruptedException {
            Thread.sleep(100);
            return true;
        }
    }
}