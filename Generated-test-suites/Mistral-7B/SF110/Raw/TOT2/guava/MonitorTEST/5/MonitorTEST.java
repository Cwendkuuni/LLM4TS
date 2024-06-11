package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import com.google.common.util.concurrent.Monitor;
import org.junit.Test;

public class MonitorTest {
// Existing test cases

// New test case for enterInterruptibly_timesOut method
@Test
public void testEnterInterruptibly_timesOut() {
Monitor monitor = new Monitor();
Thread thread = new Thread(() -> {
try {
monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
} catch (InterruptedException ignored) {
}
});
thread.start();
thread.join();
assertFalse(monitor.lock.isHeldByCurrentThread());
}

// New test case for enterInterruptibly_isInterruptedWhileWaiting method
@Test
public void testEnterInterruptibly_isInterruptedWhileWaiting() {
Monitor monitor = new Monitor();
Thread interrupter = new Thread(() -> monitor.enterInterruptibly());
interrupter.start();
monitor.enterInterruptibly();
interrupter.interrupt();
assertThrows(InterruptedException.class, () -> monitor.enterInterruptibly());
monitor.leave();
interrupter.join();
}

// New test case for enterWhen_timesOut method
@Test
public void testEnterWhen_timesOut() {
Monitor monitor = new Monitor();
Guard guard = new MyGuard(monitor);
Thread thread = new Thread(() -> {
try {
monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
} catch (InterruptedException ignored) {
}
});
thread.start();
assertFalse(monitor.lock.isHeldByCurrentThread());
thread.join();
}

// New test case for enterWhen_isInterruptedWhileWaiting method
@Test
public void testEnterWhen_isInterruptedWhileWaiting() {
Monitor monitor = new Monitor();
Guard guard = new MyGuard(monitor);
Thread interrupter = new Thread(() -> monitor.enterInterruptibly());
interrupter.start();
monitor.enterWhen(guard);
interrupter.interrupt();
monitor.leave();
interrupter.join();
assertThrows(InterruptedException.class, () -> monitor.enterWhen(guard));
}

// New test case for isFair method
@Test
public void testIsFair() {
Monitor monitor = new Monitor();
Monitor monitorFair = new Monitor(true);
assertTrue(monitor.isFair());
assertTrue(monitorFair.isFair());
}

// New test case for isOccupied method
@Test
public void testIsOccupied() {
Monitor monitor = new Monitor();
Monitor monitorLocked = new Monitor();
monitorLocked.enter();
assertTrue(monitorLocked.isOccupied());
assertTrue(monitorLocked.isOccupiedByCurrentThread());
assertTrue(monitorLocked.isOccupied());
assertFalse(monitor.isOccupied());
assertFalse(monitor.isOccupiedByCurrentThread());
monitorLocked.leave();
}

// New test case for isOccupiedByCurrentThread method
@Test
public void testIsOccupiedByCurrentThread() {
Monitor monitor = new Monitor();
Monitor monitorLocked = new Monitor();
monitorLocked.enter();
assertTrue(monitorLocked.isOccupiedByCurrentThread());
assertFalse(monitor.isOccupiedByCurrentThread());
monitorLocked.leave();
}

// New test case for getOccupiedDepth method
@Test
public void testGetOccupiedDepth() {
Monitor monitor = new Monitor();
Monitor monitorLocked = new Monitor();
monitorLocked.enter();
monitorLocked.enter();
assertTrue(monitorLocked.getOccupiedDepth() == 2);
assertTrue(monitor.getOccupiedDepth() == 0);
monitorLocked.leave();
monitorLocked.leave();
}

// New test case for getQueueLength method
@Test
public void testGetQueueLength() {
Monitor monitor = new Monitor();
Monitor monitorLocked = new Monitor();
monitorLocked.enter();
Thread thread1 = new Thread(() -> monitor.enterInterruptibly());
Thread thread2 = new Thread(() -> monitor.enterInterruptibly());
monitorLocked.leave();
assertTrue(monitor.getQueueLength() == 2);
assertTrue(monitor.hasQueuedThreads());
thread1.join();
thread2.join();
assertTrue(monitor.getQueueLength() == 0);
assertFalse(monitor.hasQueuedThreads());
}

// New test case for hasQueuedThread method
@Test
public void testHasQueuedThread() {
Monitor monitor = new Monitor();
Monitor monitorLocked = new Monitor();
monitorLocked.enter();
Thread thread1 = new Thread(() -> monitor.enterInterruptibly());
Thread thread2 = new Thread(() -> monitor.enterInterruptibly());
monitorLocked.leave();
assertTrue(monitor.hasQueuedThread(thread1));
assertTrue(monitor.hasQueuedThread(thread2));
assertTrue(monitor.hasQueuedThreads());
thread1.join();
thread2.join();
assertFalse(monitor.hasQueuedThread(thread1));
assertFalse(monitor.hasQueuedThread(thread2));
assertFalse(monitor.hasQueuedThreads());
}

// New test case for hasWaiters method
@Test
public void testHasWaiters() {
Monitor monitor = new Monitor();
Monitor monitorLocked = new Monitor();
Monitor.Guard guard = new Monitor.Guard(monitor);
monitorLocked.enter();
Thread thread1 = new Thread(() -> monitor.enterWhen(guard));
Thread thread2 = new Thread(() -> monitor.enterWhen(guard));
monitorLocked.leave();
assertTrue(monitor.hasWaiters(guard));
assertTrue(monitor.getWaitQueueLength(guard) > 0);
thread1.join();
thread2.join();
assertFalse(monitor.hasWaiters(guard));
assertFalse(monitor.getWaitQueueLength(guard) > 0);
}

// New test case for signalNextWaiter method
@Test
public void testSignalNextWaiter() {
Monitor monitor = new Monitor();
Monitor.Guard guard1 = new Monitor.Guard(monitor);
Monitor.Guard guard2 = new Monitor.Guard(monitor);
monitor.enterWhen(guard1);
monitor.enterWhen(guard2);
monitor.signalNextWaiter();
Thread thread1 = new Thread(() -> monitor.enterWhen(guard1));
Thread thread2 = new Thread(() -> monitor.enterWhen(guard2));
thread1.start();
thread2.start();
assertTrue(guard1.isSatisfied());
assertTrue(guard2.isSatisfied());
}

// New test case for isSatisfied method
@Test
public void testIsSatisfied() {
Monitor monitor = new Monitor();
Monitor.Guard guard = new Monitor.Guard(monitor);
assertFalse(guard.isSatisfied());
}

// New test case for await method
@Test
public void testAwait() {
Monitor monitor = new Monitor();
Monitor.Guard guard = new Monitor.Guard(monitor);
Thread thread = new Thread(() -> {
try {
monitor.await(guard, true);
} catch (InterruptedException e) {
Thread.currentThread().interrupt();
}
});
thread.start();
try {
guard.isSatisfied();
fail("Expected InterruptedException");
} catch (InterruptedException e) {
// Expected
}
thread.join();
}

// New test case for awaitUninterruptibly method
@Test
public void testAwaitUninterruptibly() {
Monitor monitor = new Monitor();
Monitor.Guard guard = new Monitor.Guard(monitor);
Thread thread = new Thread(() -> monitor.awaitUninterruptibly(guard));
thread.start();
try {
guard.isSatisfied();
fail("Expected InterruptedException");
} catch (InterruptedException e) {
Thread.currentThread().interrupt();
}
thread.join();
}

// New test case for awaitNanos method
@Test
public void testAwaitNanos() {
Monitor monitor = new Monitor();
Monitor.Guard guard = new Monitor.Guard(monitor);
Thread thread = new Thread(() -> monitor.awaitNanos(guard, 1));
thread.start();
try {
guard.isSatisfied();
fail("Expected InterruptedException");
} catch (InterruptedException e) {
Thread.currentThread().interrupt();
}
thread.join();
}

}