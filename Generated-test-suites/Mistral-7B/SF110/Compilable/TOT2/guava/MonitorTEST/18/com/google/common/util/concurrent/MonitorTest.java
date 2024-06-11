package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.util.concurrent.Monitor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorTest {
    @Test
    public void testEnter() throws InterruptedException {
        Monitor monitor = new Monitor();
        ReentrantLock lock = monitor.lock;

        monitor.enter();
        assertTrue(lock.isHeldByCurrentThread());

        monitor.enter(); // should not be able to enter again without releasing the lock first
        assertFalse(lock.tryLock());

        monitor.leave();
        assertTrue(lock.tryLock());
    }

    @Test
    public void testEnterInterrupted() throws InterruptedException {
        Monitor monitor = new Monitor();
        ReentrantLock lock = monitor.lock;

        monitor.enter();
        Thread interrupter = Thread.currentThread();
        interrupter.interrupt();

        try {
            monitor.enterInterruptibly();
            fail("Expected InterruptedException");
        } catch (InterruptedException e) {
            // Expected
        }

        monitor.leave();
        assertTrue(lock.tryLock());
    }

    @Test
    public void testEnterFairness() throws InterruptedException {
        Monitor nonFairMonitor = new Monitor(false);
        Monitor fairMonitor = new Monitor(true);

        Thread thread1 = new Thread(() -> {
            nonFairMonitor.enter();
        });
        Thread thread2 = new Thread(() -> {
            fairMonitor.enter();
        });
        thread1.start();
        thread2.start();

        thread1.join();
        assertTrue(fairMonitor.isOccupiedByCurrentThread());
        assertTrue(nonFairMonitor.isOccupied());
    }

    @Test
    public void testEnterTimeOut() throws InterruptedException {
        Monitor monitor = new Monitor();

        boolean entered = false;
        try {
            entered = monitor.enter(1, TimeUnit.SECONDS);
        } finally {
            monitor.leave();
        }

        assertFalse(entered);
    }
}