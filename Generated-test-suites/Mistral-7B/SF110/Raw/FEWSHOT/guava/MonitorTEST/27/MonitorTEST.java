package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import com.google.common.annotations.*;
import com.google.common.base.*;
import com.google.common.util.concurrent.*;
import org.junit.Test;

@Beta
public class MonitorTest {

 @Test
 public void testMonitorCreation() {
 Monitor monitor = new Monitor();
 Monitor fairMonitor = new Monitor(true);
 }

 @Test
 public void testEnterAndLeave() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 }

 @Test
 public void testEnterInterruptiblyAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 monitor.leave();
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test
 public void testEnterInterruptiblyWithLongTimeoutAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 long timeoutNanos = TimeUnit.SECONDS.toNanos(1);
 monitor.enterInterruptibly(timeoutNanos, TimeUnit.NANOSECONDS);
 monitor.leave();
 }

 @Test
 public void testTryEnterAndLeave() {
 Monitor monitor = new Monitor();
 assertTrue(monitor.tryEnter());
 monitor.leave();
 }

 @Test
 public void testTryEnterWithFalseAndLeave() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.tryEnter());
 monitor.leave();
 }

 @Test
 public void testEnterWhenAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test
 public void testEnterWhenUninterruptiblyAndLeave() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithTimeoutAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 long timeoutNanos = TimeUnit.SECONDS.toNanos(1);
 monitor.enterWhen(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 monitor.leave();
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeoutAndLeave() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 long timeoutNanos = TimeUnit.SECONDS.toNanos(1);
 monitor.enterWhenUninterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 monitor.leave();
 }

 @Test
 public void testEnterIfAndLeave() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitorIfSatisfied();
 monitor.enterIf(guard);
 monitor.leave();
 }

 @Test
 public void testEnterIfInterruptiblyAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitorIfSatisfied();
 monitor.enterIfInterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testEnterIfWithTimeoutAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitorIfSatisfied();
 long timeoutNanos = TimeUnit.MILLISECONDS.toNanos(1);
 monitor.enterIf(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 monitor.leave();
 }

 @Test
 public void testEnterIfUninterruptiblyWithTimeoutAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitorIfSatisfied();
 long timeoutNanos = TimeUnit.SECONDS.toNanos(1);
 monitor.enterIfUninterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 monitor.leave();
 }

 @Test
 public void testTryEnterIfAndLeave() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitorIfSatisfied();
 assertTrue(monitor.tryEnterIf(guard));
 monitor.leave();
 }

 @Test
 public void testTryEnterIfWithFalseAndLeave() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitorIfSatisfied();
 assertFalse(monitor.tryEnterIf(guard));
 monitor.leave();
 }

 @Test
 public void testWaitForAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitFor(guard);
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptiblyAndLeave() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitForUninterruptedly(guard);
 monitor.leave();
 }

 @Test
 public void testWaitForWithTimeoutAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 long timeoutNanos = TimeUnit.SECONDS.toNanos(1);
 monitor.waitFor(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeoutAndLeave() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 long timeoutNanos = TimeUnit.SECONDS.toNanos(1);
 monitor.waitForUninterruptedly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 monitor.leave();
 }

 @Test
 public void testIsFair() {
 Monitor monitor = new Monitor();
 Monitor fairMonitor = new Monitor(true);
 assertEquals(false, monitor.isFair());
 assertEquals(true, fairMonitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 Monitor monitor = new Monitor();
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
 monitor.enter();
 monitor.enter();
 monitor.enter();
 assertEquals(3, monitor.getQueueLength());
 monitor.leave();
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enter();
 assertTrue(monitor.hasQueuedThreads(new AnyGuard()));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads(new AnyGuard()));
 }

 @Test
 public void testHasQueuedThread() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread thread = new Thread();
 monitor.enter();
 monitor.enter();
 thread.start();
 assertTrue(monitor.hasQueuedThread(thread));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThread(thread));
 }

 @Test
 public void testHasWaiters() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 monitor.enterWhen(guard);
 monitor.leave();
 assertTrue(monitor.hasWaiters(guard));
 monitor.leave();
 assertFalse(monitor.hasWaiters(guard));
 }

 @Test
 public void testGetWaitQueueLength() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 monitor.enterWhen(guard);
 monitor.leave();
 assertEquals(2, monitor.getWaitQueueLength(guard));
 monitor.leave();
 assertEquals(0, monitor.getWaitQueueLength(guard));
 }

 @Test
 public void testSignalNextWaiter() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard1 = new GuardMonitor();
 Guard guard2 = new GuardMonitor();
 monitor.enter();
 monitor.enterWhen(guard1);
 monitor.signalNextWaiter();
 monitor.enterWhen(guard2);
 monitor.leave();
 assertTrue(guard1.isSatisfied());
 assertTrue(guard2.isSatisfied());
 }

 @Test
 public void testIsSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 assertFalse(guard.isSatisfied());
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testSignalAllWaiters() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard1 = new GuardMonitor();
 Guard guard2 = new GuardMonitor();
 monitor.enter();
 monitor.enterWhen(guard1);
 monitor.enterWhen(guard2);
 monitor.signalAllWaiters();
 monitor.leave();
 assertTrue(guard1.isSatisfied());
 assertTrue(guard2.isSatisfied());
 }

 @Test
 public void testBeginWaitingForAndEndWaitingFor() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 monitor.beginWaitingFor(guard);
 monitor.leave();
 monitor.endWaitingFor(guard);
 assertEquals(0, guard.waiterCount);
 }
}

class AnyGuard extends Monitor.Guard {
 protected boolean isSatisfied() {
 return true;
 }
}

class GuardMonitor implements Monitor.Guard {
 private boolean isSatisfied = false;
 Monitor monitor;
 Condition condition;
 int waiterCount;
 Guard next;

 protected GuardMonitor() {
 this.monitor = null;
 this.condition = null;
 this.waiterCount = 0;
 this.next = null;
 }

 protected GuardMonitor(Monitor monitor) {
 this.monitor = monitor;
 this.condition = monitor.lock.newCondition();
 }

 @Override
 public boolean isSatisfied() {
 return isSatisfied;
 }
}

class GuardMonitorIfSatisfied extends Monitor.Guard {
 private boolean isSatisfied = false;

 protected GuardMonitorIfSatisfied() {
 super(null);
 }

 @Override
 public boolean isSatisfied() {
 return isSatisfied;
 }
}