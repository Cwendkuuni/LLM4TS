package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;

public class MonitorTest {

    @Test
    public void testMonitorCreation() {
        Monitor monitor = new Monitor();
        assertEquals(false, monitor.isFair());
        Monitor fairMonitor = new Monitor(true);
        assertEquals(true, fairMonitor.isFair());
    }

    @Test
    public void testEnterAndLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testEnterInterruptiblyAndLeave() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        monitor.leave();
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testTryEnter() {
        Monitor monitor = new Monitor();
        assertTrue(monitor.tryEnter());
        assertFalse(monitor.tryEnter());
        assertFalse(monitor.isOccupied());
    }

    @Test
    public void testEnterWhen() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor();
        monitor.enterWhen(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testEnterWhenUninterruptibly() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor();
        monitor.enterWhenUninterruptibly(guard);
        monitor.leave();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testEnterWhenWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor();
        assertTrue(monitor.enterWhen(guard, 1, TimeUnit.SECONDS));
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterWhenUninterruptiblyWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitor();
        assertFalse(monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.SECONDS));
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitorSatisfied();
        assertTrue(monitor.enterIf(guard));
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testEnterIfInterruptibly() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitorSatisfied();
        assertTrue(monitor.enterIfInterruptibly(guard));
        monitor.leave();
        assertTrue(guard.isSatisfied());
    }

    @Test
    public void testEnterIfWithTimeout() {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitorSatisfied();
        assertTrue(monitor.enterIf(guard, 1, TimeUnit.SECONDS));
        assertFalse(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testEnterIfInterruptiblyWithTimeout() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitorSatisfied();
        assertFalse(monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS));
    }

    @Test
    public void testEnterIfWithTimeoutAndSignalThread() throws InterruptedException {
        Monitor monitor = new Monitor();
        Guard guard = new GuardMonitorSatisfied();
        assertTrue(monitor.enterIf(guard, 1, TimeUnit.SECONDS));
        monitor.signalNextWaiter();
        assertTrue(guard.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testIsFair() {
        Monitor monitor = new Monitor();
        assertFalse(monitor.isFair());

        Monitor fairMonitor = new Monitor(true);
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
        for (int i = 0; i < 10; i++) {
            monitor.enter();
        }
        assertEquals(10, monitor.getOccupiedDepth());
        for (int i = 0; i < 10; i++) {
            monitor.leave();
        }
        assertEquals(0, monitor.getOccupiedDepth());
    }

    @Test
    public void testGetQueueLength() {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor();
        Guard guard2 = new GuardMonitor();
        monitor.enterWhen(guard1);
        monitor.enterWhen(guard2);
        assertEquals(2, monitor.getQueueLength(null));
        monitor.leave();
        assertEquals(0, monitor.getQueueLength(null));
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        Guard guard1 = new GuardMonitor();
        monitor.enterWhen(guard1);
        assertTrue(monitor.hasQueuedThreads(guard1));
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads(guard1));
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        Thread thread = new Thread();
        monitor.enter();
        monitor.enterWhen(new GuardMonitor(thread));
        assertTrue(monitor.hasQueuedThread(thread));
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(thread));
    }
}

class GuardMonitor {
    Monitor monitor;

    GuardMonitor() {
        this.monitor = new Monitor();
    }

    GuardMonitor(Thread thread) {
        this.monitor = new Monitor();
        this.monitor.enterWhen(new GuardMonitorThread(this.monitor, thread));
    }

    @Override
    public boolean isSatisfied() {
        return false;
    }
}

class GuardMonitorSatisfied extends GuardMonitor {
    @Override
    public boolean isSatisfied() {
        return true;
    }
}

class GuardMonitorThread extends Thread {
    Monitor monitor;

    GuardMonitorThread(Monitor monitor, Thread thread) {
        this.monitor = monitor;
        this.setDaemon(true);
        this.setName("MonitorThread");
        this.start();
        try {
            join();
        } catch (InterruptedException e) {
            fail("Thread was interrupted");
        }
        this.monitor.signalAllWaiters();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("Thread was interrupted");
        }
        this.monitor.signalAllWaiters();
    }
}