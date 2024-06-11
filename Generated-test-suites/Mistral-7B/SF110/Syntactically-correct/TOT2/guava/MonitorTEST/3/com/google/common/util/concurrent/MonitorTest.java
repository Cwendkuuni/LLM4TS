
package com.google.common.util.concurrent;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;
import org.junit.*;
import static org.junit.Assert.*;

@Beta
public final class MonitorTest {
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
        monitor.enterInterruptibly();
        assertTrue(monitor.isOccupiedByCurrentThread());
        Thread.currentThread().interrupt();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnter_Fair() throws InterruptedException {
        Monitor monitorFair = new Monitor(true);
        Thread thread1 = new Thread(() -> { monitorFair.enter(); });
        Thread thread2 = new Thread(() -> { monitorFair.enter(); });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        assertFalse(monitorFair.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptibly_Interrupted() throws InterruptedException {
        monitor.enterInterruptibly();
        Thread.currentThread().interrupt();
        monitor.enterInterruptibly();
        fail("Expected InterruptedException");
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterInterruptibly_IllegalMonitorStateException() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enter();
        try {
            monitor.enterInterruptibly();
        } finally {
            monitor.leave();
        }
        monitor.enterInterruptibly();
        fail("Expected IllegalMonitorStateException");
    }
}