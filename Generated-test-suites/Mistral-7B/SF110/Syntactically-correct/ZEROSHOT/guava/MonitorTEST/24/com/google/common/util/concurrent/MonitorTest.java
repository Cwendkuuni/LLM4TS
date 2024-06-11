package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

 @Test
 public void testMonitorConstruction() {
 Monitor monitor = new Monitor();
 Monitor fairMonitor = new Monitor(true);
 }

 @Test
 public void testEnterUnlock() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testEnterInterruptiblyLock() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyDifferentMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enterInterruptibly();
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testEnterTimeoutUnlock() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 assertTrue(monitor.enter(1, TimeUnit.MILLISECONDS));
 monitor.leave();
 }

 @Test(expected = TimeoutException.class)
 public void testEnterTimeoutInterruptible() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS));
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterTimeoutDifferentMonitor() throws InterruptedException, TimeoutException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter(1, TimeUnit.MILLISECONDS);
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenDifferentMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor();
 monitor1.enterWhen(guard);
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenUninterruptiblyDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor();
 monitor1.enterWhenUninterruptibly(guard);
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testEnterWhenTimeoutUnlock() throws InterruptedException, TimeoutException {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 assertTrue(monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS));
 monitor.leave();
 }

 @Test(expected = TimeoutException.class)
 public void testEnterWhenTimeoutInterruptible() throws InterruptedException, TimeoutException {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 assertFalse(monitor.enterWhenInterruptibly(guard, 1, TimeUnit.MILLISECONDS));
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenTimeoutDifferentMonitor() throws InterruptedException, TimeoutException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor();
 monitor1.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testEnterIf() {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 assertTrue(monitor.enterIf(guard));
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor();
 monitor1.enterIf(guard);
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testEnterIfTimeoutUnlock() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 assertTrue(monitor.enterIf(new TimeMonitor(monitor), 1, TimeUnit.MILLISECONDS));
 monitor.leave();
 }

 @Test(expected = TimeoutException.class)
 public void testEnterIfTimeoutInterruptible() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.enterIfInterruptibly(new TimeMonitor(monitor), 1, TimeUnit.MILLISECONDS));
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfTimeoutDifferentMonitor() throws InterruptedException, TimeoutException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new TimeMonitor(monitor1);
 monitor1.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testEnterIfTry() {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 assertTrue(monitor.tryEnterIf(guard));
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfTryDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor();
 monitor1.tryEnterIf(guard);
 monitor2.enter();
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForDifferentMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor();
 monitor1.enter();
 monitor2.enter();
 monitor1.waitFor(guard);
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testWaitForUninterruptibly() {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.waitForUninterruptibly(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForUninterruptiblyDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor();
 monitor1.enter();
 monitor2.enter();
 monitor1.waitForUninterruptibly(guard);
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testWaitForTimeoutUnlock() throws InterruptedException, TimeoutException {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS));
 monitor.leave();
 }

 @Test(expected = TimeoutException.class)
 public void testWaitForTimeoutInterruptible() throws InterruptedException, TimeoutException {
 Guard guard = new GuardMonitor();
 Monitor monitor = new Monitor();
 assertFalse(monitor.waitForInterruptibly(guard, 1, TimeUnit.MILLISECONDS));
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForTimeoutDifferentMonitor() throws InterruptedException, TimeoutException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor();
 monitor1.enter();
 monitor2.enter();
 monitor1.waitFor(guard, 1, TimeUnit.MILLISECONDS);
 monitor1.leave(); // Should throw IMSException
 }

 @Test
 public void testIsFair() {
 Monitor monitor = new Monitor();
 assertTrue(monitor.isFair());
 Monitor fairMonitor = new Monitor(true);
 assertTrue(fairMonitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 monitor.enter();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 Monitor monitor = new Monitor();
 assertEquals(0, monitor.getOccupiedDepth());
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 Monitor monitor = new Monitor();
 assertEquals(0, monitor.getQueueLength());
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard1 = new ThreadMonitor(Thread.currentThread(), monitor1);
 Guard guard2 = new ThreadMonitor(Thread.currentThread(), monitor2);
 monitor.enter();
 monitor1.enter();
 monitor1.waitFor(guard1, 1, TimeUnit.MILLISECONDS);
 monitor1.leave();
 monitor1.enter();
 monitor2.enter();
 monitor2.waitFor(guard2, 1, TimeUnit.MILLISECONDS);
 monitor1.leave();
 monitor2.leave();
 monitor.leave();
 assertEquals(2, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.hasQueuedThreads(null));
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard1 = new ThreadMonitor(Thread.currentThread(), monitor1);
 Guard guard2 = new ThreadMonitor(Thread.currentThread(), monitor2);
 monitor.enter();
 monitor1.enter();
 monitor1.waitFor(guard1, 1, TimeUnit.MILLISECONDS);
 monitor1.leave();
 monitor1.enter();
 monitor2.enter();
 monitor2.waitFor(guard2, 1, TimeUnit.MILLISECONDS);
 monitor1.leave();
 monitor2.leave();
 monitor.leave();
 assertTrue(monitor.hasQueuedThreads(null));
 }

 @Test
 public void testHasQueuedThread() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.hasQueuedThread(null));
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard1 = new ThreadMonitor(Thread.currentThread(), monitor1);
 Monitor monitor3 = new Monitor();
 Guard guard2 = new ThreadMonitor(Thread.currentThread(), monitor2);
 Monitor monitor4 = new Monitor();
 Guard guard3 = new ThreadMonitor(Thread.currentThread(), monitor3);
 monitor.enter();
 monitor1.enter();
 monitor1.waitFor(guard1, 1, TimeUnit.MILLISECONDS);
 monitor1.leave();
 monitor1.enter();
 monitor2.enter();
 monitor2.waitFor(guard2, 1, TimeUnit.MILLISECONDS);
 monitor1.leave();
 monitor2.leave();
 monitor3.enter();
 monitor3.waitFor(guard3, 1, TimeUnit.MILLISECONDS);
 monitor3.leave();
 monitor4.enter();
 monitor4.leave();
 monitor.leave();
 assertTrue(monitor.hasQueuedThread(monitor2));
 assertFalse(monitor.hasQueuedThread(monitor1));
 assertFalse(monitor.hasQueuedThread(monitor3));
 assertFalse(monitor.hasQueuedThread(monitor4));
 }
}

class GuardMonitor extends Monitor.Guard {
 @Override
 public boolean isSatisfied() {
 return true;
 }
}

class TimeMonitor extends Monitor.Guard {
 private final Monitor monitor;

 public TimeMonitor(Monitor monitor) {
 this.monitor = monitor;
 }

 @Override
 public boolean isSatisfied() {
 return System.currentTimeMillis() > 1000;
 }
}

class ThreadMonitor implements Runnable {
 private final Monitor monitor;
 private final Thread thread;

 public ThreadMonitor(Thread thread, Monitor monitor) {
 this.thread = thread;
 this.monitor = monitor;
 }

 @Override
 public void run() {
 try {
 this.monitor.waitFor(this);
 } catch (InterruptedException e) {
 // Ignore
 }
 }
}