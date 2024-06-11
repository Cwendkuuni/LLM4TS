package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;
import org.junit.Test;

@Beta
public class MonitorTest {

    @Test
    public void testMonitorCreation() {
        Monitor monitor = new Monitor();
        Monitor fairMonitor = new Monitor(true);
        assertNull(monitor.activeGuards);
        assertNull(fairMonitor.activeGuards);
    }

    @Test
    public void testEnter() {
        Monitor monitor = new Monitor();
        try {
            monitor.enter();
            assertTrue(monitor.isOccupiedByCurrentThread());
        } finally {
            monitor.leave();
        }
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testEnterWithWrongMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor2.enter();
        monitor1.leave();
        monitor1.enter(); //should throw an exception
    }

    @Test
    public void testEnterInterruptibly() {
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
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = InterruptedException.class)
    public void testEnterInterruptiblyInterrupted() {
        Monitor monitor = new Monitor();
        monitor.enterInterruptibly();
        Thread thread = new Thread(() -> monitor.enterInterruptibly(), "Thread1");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        monitor.leave();
        thread.interrupt();
        monitor.enterInterruptibly(); //should throw an exception
    }

    @Test
    public void testEnterWithTimeout() {
        Monitor monitor = new Monitor();
        long timeout = 100;
        boolean entered = monitor.enterWhen(new MyGuard(monitor), timeout, TimeUnit.MILLISECONDS);
        assertTrue(entered);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterWithTimeoutInterrupted() {
        Monitor monitor = new Monitor();
        long timeout = 100;
        MyGuard guard = new MyGuard(monitor);
        Thread thread = new Thread(() -> monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS), "Thread1");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        monitor.leave();
        thread.interrupt();
        monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS); //should not enter
    }

    @Test
    public void testEnterIf() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        assertTrue(monitor.enterIf(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfInterruptibly() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enterInterruptibly();
        assertTrue(monitor.enterIfInterruptibly(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeout() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        assertTrue(monitor.enterIf(guard, 100, TimeUnit.MILLISECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testEnterIfWithTimeoutInterrupted() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        Thread thread = new Thread(() -> monitor.enterIf(guard, 100, TimeUnit.MILLISECONDS), "Thread1");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        monitor.leave();
        thread.interrupt();
        assertFalse(monitor.enterIf(guard, 100, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testEnterIfWithTimeoutAndSignal() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        Thread thread = new Thread(() -> monitor.enterIf(guard, 100, TimeUnit.MILLISECONDS), "Thread1");
        thread.start();
        monitor.signalNextWaiter();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(monitor.enterIf(guard, 100, TimeUnit.MILLISECONDS));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitFor() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForInterrupted() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard);
        monitor.interrupt();
        assertFalse(guard.isSatisfied());
    }

    @Test
    public void testWaitForUninterruptibly() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForUninterruptiblyInterrupted() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        monitor.waitForUninterruptibly(guard);
        monitor.interrupt();
        monitor.waitForUninterruptibly(guard); //should not wait
    }

    @Test
    public void testWaitForWithTimeout() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard, 100, TimeUnit.MILLISECONDS);
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testWaitForWithTimeoutInterrupted() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        Thread thread = new Thread(() -> monitor.waitFor(guard, 100, TimeUnit.MILLISECONDS), "Thread1");
        thread.start();
        monitor.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(monitor.enterIf(guard));
    }

    @Test
    public void testWaitForWithTimeoutAndSignal() {
        Monitor monitor = new Monitor();
        MyGuard guard = new MyGuard(monitor);
        monitor.enter();
        Thread thread = new Thread(() -> monitor.waitFor(guard, 100, TimeUnit.MILLISECONDS), "Thread1");
        thread.start();
        monitor.signalNextWaiter();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(monitor.enterIf(guard));
        assertTrue(monitor.isOccupiedByCurrentThread());
    }

    @Test
    public void testLeave() {
        Monitor monitor = new Monitor();
        monitor.enter();
        monitor.leave();
        assertFalse(monitor.isOccupiedByCurrentThread());
    }

    @Test(expected = IllegalMonitorStateException.class)
    public void testLeaveWithWrongMonitor() {
        Monitor monitor1 = new Monitor();
        Monitor monitor2 = new Monitor();
        monitor1.enter();
        monitor2.enter();
        monitor1.leave();
        monitor1.leave(); //should throw an exception
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
        MyGuard guard1 = new MyGuard(monitor);
        MyGuard guard2 = new MyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard1);
        assertEquals(1, monitor.getQueueLength());
        monitor.leave();
        monitor.enter();
        monitor.waitFor(guard2);
        assertEquals(1, monitor.getQueueLength());
        monitor.leave();
        assertEquals(0, monitor.getQueueLength());
    }

    @Test
    public void testHasQueuedThreads() {
        Monitor monitor = new Monitor();
        MyGuard guard1 = new MyGuard(monitor);
        MyGuard guard2 = new MyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard1);
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        monitor.enter();
        monitor.waitFor(guard2);
        assertTrue(monitor.hasQueuedThreads());
        monitor.leave();
        assertFalse(monitor.hasQueuedThreads());
    }

    @Test
    public void testHasQueuedThread() {
        Monitor monitor = new Monitor();
        MyGuard guard1 = new MyGuard(monitor);
        MyGuard guard2 = new MyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard1);
        assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
        monitor.leave();
        monitor.enter();
        monitor.waitFor(guard2);
        assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
        monitor.leave();
        assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
    }

    @Test
    public void testSignalNextWaiter() {
        Monitor monitor = new Monitor();
        MyGuard guard1 = new MyGuard(monitor);
        MyGuard guard2 = new MyGuard(monitor);
        monitor.enter();
        monitor.waitFor(guard1);
        monitor.signalNextWaiter();
        monitor.waitFor(guard2);
        assertTrue(guard2.isSatisfied());
        monitor.leave();
    }

    @Test
    public void testIsSatisfied() {
        MyGuard guard = new MyGuard(null);
        assertTrue(guard.isSatisfied());
    }

    class MyGuard extends Monitor.Guard {
        final Monitor monitor;

        MyGuard(Monitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public boolean isSatisfied() {
            return true;
        }
    }
}