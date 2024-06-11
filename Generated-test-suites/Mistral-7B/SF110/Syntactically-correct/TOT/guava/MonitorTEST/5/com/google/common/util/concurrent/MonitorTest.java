package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

 @BeforeClass
 public static void setUp() {
 System.setProperty("test.thread.interrupted", "false");
 }

 @AfterClass
 public static void tearDown() {
 System.clearProperty("test.thread.interrupted");
 }

 @Test
 public void testConstructor() {
 Monitor monitor = new Monitor();
 assertNull(monitor.activeGuards);
 assertFalse(monitor.isFair());
 ReentrantLock lock = monitor.lock;
 assertFalse(lock.isLocked());
 }

 @Test
 public void testEnterMethodWhenLockIsNotHeld() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.lock.isLocked());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterMethodWithInvalidMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enter(); // This thread enters with an invalid monitor
 monitor1.enterWhen(monitor2);
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyMethodWhenInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 fail("InterruptedException should have been thrown");
 }

 @Test
 public void testEnterInterruptiblyMethodWhenLockIsHeldByCurrentThread() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterInterruptibly();
 assertFalse(Thread.interrupted());
 }

 @Test
 public void testEnterMethodWithGuardAndSatisfiedCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard);
 assertTrue(monitor.lock.isLocked());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterMethodWithGuardAndUnsatisfiedCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard);
 }

 @Test
 public void testEnterInterruptiblyMethodWithGuardAndSatisfiedCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.lock.isLocked());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyMethodWithGuardAndUnsatisfiedCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterInterruptibly(guard);
 }

 @Test
 public void testEnterMethodWithGuardAndTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard, 100, TimeUnit.MILLISECONDS);
 fail("TimeoutException should have been thrown");
 }

 @Test
 public void testEnterMethodWithGuardAndTimeoutAndSatisfiedCondition() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard, 100, TimeUnit.MILLISECONDS);
 assertTrue(monitor.lock.isLocked());
 }

 @Test
 public void testEnterMethodWithGuardAndTimeoutAndUnsatisfiedCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhenUninterruptibly(guard, 100, TimeUnit.MILLISECONDS);
 fail("TimeoutException should have been thrown");
 }

 @Test
 public void testEnterMethodWithGuardAndTimeoutAndSatisfiedCondition() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override public boolean isSatisfied() { return true; }
 };
 monitor.enterWhen(guard, 100, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertTrue(monitor.lock.isLocked());
 }

 @Test
 public void testEnterMethodWithGuardAndTimeoutAndUnsatisfiedCondition() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhenUninterruptibly(guard, 100, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertFalse(monitor.lock.isLocked());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterMethodWithGuardAndInvalidMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Monitor.Guard guard = new Monitor.Guard(monitor1) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor1.enter();
 monitor2.enter();
 monitor1.enterWhen(guard);
 }

 @Test
 public void testLeaveMethodWhenLockIsNotHeld() {
 Monitor monitor = new Monitor();
 monitor.leave();
 assertFalse(monitor.lock.isLocked());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testLeaveMethodWithInvalidMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enter();
 monitor1.leave();
 monitor2.leave();
 monitor1.leave();
 }

 @Test
 public void testIsFairMethod() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor(true);
 assertFalse(monitor1.isFair());
 assertTrue(monitor2.isFair());
 }

 @Test
 public void testIsOccupiedMethod() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThreadMethod() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepthMethod() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enter();
 assertEquals(2, monitor.getOccupiedDepth());
 monitor.leave();
 assertEquals(1, monitor.getOccupiedDepth());
 monitor.leave();
 assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLengthMethod() {
 Monitor monitor = new Monitor();
 monitor.enter();
 Monitor.Guard guard1 = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard1);
 Monitor.Guard guard2 = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard2);
 assertEquals(2, monitor.getQueueLength());
 monitor.leave();
 assertEquals(1, monitor.getQueueLength());
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreadsMethodWithNoThreads() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.hasQueuedThreads());
 }

 @Test
 public void testHasQueuedThreadsMethodWithQueuedThreads() {
 Monitor monitor = new Monitor();
 Monitor.Guard guard1 = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard1);
 Monitor.Guard guard2 = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard2);
 assertTrue(monitor.hasQueuedThreads());
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads());
 }

 @Test
 public void testHasWaitersMethodWithNoWaiters() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.hasWaiters(guard1));
 }

 @Test
 public void testHasWaitersMethodWithWaiters() {
 Monitor monitor = new Monitor();
 Monitor.Guard guard1 = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard1);
 Monitor.Guard guard2 = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard2);
 assertTrue(monitor.hasWaiters(guard2));
 monitor.leave();
 assertFalse(monitor.hasWaiters(guard2));
 }
}