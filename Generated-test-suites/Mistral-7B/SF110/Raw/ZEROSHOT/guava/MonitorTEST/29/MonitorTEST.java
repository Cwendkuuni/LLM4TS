package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

 private Monitor monitor;
 private Guard guard;
 private Guard guard2;

 @Before
 public void setUp() {
 monitor = new Monitor();
 guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 guard2 = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyInterrupted() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 fail();
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 monitor.enterWhen(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenDifferentMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enterWhen(otherMonitor.newGuard());
 fail();
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenDifferentMonitorUninterruptibly() {
 Monitor otherMonitor = new Monitor();
 monitor.enterWhenUninterruptibly(otherMonitor.newGuard());
 fail();
 }

 @Test
 public void testEnterIf() {
 monitor.enterIf(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 monitor.enterIfInterruptibly(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfDifferentMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enterIf(otherMonitor.newGuard());
 fail();
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.enter();
 monitor.waitFor(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForDifferentMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 monitor.waitFor(otherMonitor.newGuard());
 fail();
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.enter();
 monitor.waitForUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForUninterruptiblyDifferentMonitor() {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 monitor.waitForUninterruptibly(otherMonitor.newGuard());
 fail();
 }

 @Test
 public void testWaitForWithTimeout() throws InterruptedException {
 monitor.enter();
 monitor.waitFor(guard, 1, TimeUnit.SECONDS);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForWithTimeoutInterrupted() throws InterruptedException {
 monitor.enter();
 Thread.currentThread().interrupt();
 monitor.waitFor(guard, 1, TimeUnit.SECONDS);
 fail();
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeout() {
 monitor.enter();
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.SECONDS);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testLeave() {
 monitor.enter();
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testIsFair() {
 Monitor monitorFair = new Monitor(true);
 Monitor monitorNotFair = new Monitor();
 assertTrue(monitorFair.isFair());
 assertFalse(monitorNotFair.isFair());
 }

 @Test
 public void testIsOccupied() {
 monitor.enter();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 monitor.enter();
 monitor.enter();
 monitor.enter();
 assertEquals(3, monitor.getOccupiedDepth());
 monitor.leave();
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 monitor.enter();
 monitor.enterWhen(guard);
 monitor.enterWhen(guard2);
 assertEquals(2, monitor.getQueueLength());
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.enterWhen(guard);
 monitor.enterWhen(guard2);
 assertTrue(monitor.hasQueuedThreads(guard));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads(guard));
 }

 @Test
 public void testSignalNextWaiter() {
 Monitor monitor = new Monitor();
 Monitor monitorGuard = new Monitor();
 Monitor monitorGuard2 = new Monitor();
 Monitor monitorGuard3 = new Monitor();
 Monitor monitorGuard4 = new Monitor();
 Monitor.Guard guard = monitor.newGuard();
 Monitor.Guard guard2 = monitorGuard.newGuard();
 Monitor.Guard guard3 = monitorGuard2.newGuard();
 Monitor.Guard guard4 = monitorGuard3.newGuard();
 monitor.enter();
 monitorGuard.enterWhen(guard);
 monitorGuard2.enterWhen(guard2);
 monitorGuard3.enterWhen(guard3);
 monitorGuard4.enterWhen(guard4);
 monitor.signalNextWaiter();
 assertTrue(monitorGuard.isSatisfied());
 assertFalse(monitorGuard2.isSatisfied());
 assertFalse(monitorGuard3.isSatisfied());
 assertFalse(monitorGuard4.isSatisfied());
 }
}