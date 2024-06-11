package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

 private Monitor monitor;
 private Guard guard;
 private Thread thread1, thread2;

 @Before
 public void setUp() {
 monitor = new Monitor();
 guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 thread1 = new Thread(() -> { monitor.enterWhen(guard); });
 thread2 = new Thread(() -> { monitor.enterWhen(guard); });
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterInterruptiblyThrowsExceptionWhenLockedByCurrentThread() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 fail("Expected InterruptedException");
 }

 @Test
 public void testEnterInterruptiblyAcquiresLockWhenAvailable() throws InterruptedException {
 monitor.enterInterruptibly();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertFalse(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutAcquiresLockIfPossible() throws InterruptedException, TimeoutException {
 monitor.enterInterruptibly();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertFalse(monitor.isOccupied());
 monitor.leave();

 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();

 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 Thread.sleep(2); // Allow thread1 to enter and exit
 monitor.leave();
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutDoesNotAcquireLockWhenBusy() throws InterruptedException, TimeoutException {
 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 fail("Expected TimeoutException");
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutDoesNotAcquireLockWhenInterrupted() throws InterruptedException, TimeoutException {
 monitor.enterInterruptibly();
 monitor.enter();
 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException");
 }

 @Test
 public void testTryEnter() {
 monitor.tryEnter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testTryEnterFailsWhenLockedByCurrentThread() {
 monitor.enter();
 assertFalse(monitor.tryEnter());
 }

 @Test
 public void testTryEnterSucceedsWhenAvailable() {
 monitor.tryEnter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenWithGuardSucceedsWhenConditionIsSatisfied() throws InterruptedException {
 monitor.enterWhen(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithGuardFailsWhenConditionIsNotSatisfied() throws InterruptedException {
 monitor.enterWhen(guard);
 assertSame(monitor, guard.monitor);
 thread1.start();
 thread2.start();
 monitor.enterWhen(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testEnterWhenWithGuardDoesNotBlockWhenConditionIsNotSatisfied() throws InterruptedException {
 monitor.enterWhen(guard);
 assertSame(monitor, guard.monitor);
 thread1.start();
 thread2.start();
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithGuardSucceedsWhenConditionIsSatisfied() throws InterruptedException {
 monitor.enterWhenUninterruptibly(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testEnterWhenUninterruptiblyWithGuardFailsWhenConditionIsNotSatisfied() throws InterruptedException {
 monitor.enterWhenUninterruptibly(guard);
 assertSame(monitor, guard.monitor);
 thread1.start();
 thread2.start();
 monitor.enterWhenUninterruptibly(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testEnterWhenUninterruptiblyWithGuardBlocksWhenConditionIsNotSatisfied() throws InterruptedException {
 monitor.enterWhenUninterruptibly(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 Thread.sleep(100); // Allow thread1 and thread2 to enter and exit
 monitor.enterWhenUninterruptibly(guard);
 assertSame(monitor, guard.monitor);
 }

 @Test
 public void testEnterWhenWithGuardAndTimeoutSucceedsWhenConditionIsSatisfied() throws InterruptedException {
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithGuardAndTimeoutFailsWhenConditionIsNotSatisfied() throws InterruptedException, TimeoutException {
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 fail("Expected TimeoutException");
 }

 @Test
 public void testEnterWhenWithGuardAndTimeoutDoesNotBlockWhenConditionIsNotSatisfied() throws InterruptedException, TimeoutException {
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfSucceedsWhenConditionIsSatisfied() {
 monitor.enterIf(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testEnterIfFailsWhenConditionIsNotSatisfied() {
 monitor.enterIf(guard);
 assertSame(monitor, guard.monitor);
 thread1.start();
 thread2.start();
 monitor.enterIf(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testEnterIfDoesNotBlockWhenConditionIsNotSatisfied() {
 monitor.enterIf(guard);
 assertSame(monitor, guard.monitor);
 thread1.start();
 thread2.start();
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfWithTimeoutSucceedsWhenConditionIsSatisfied() throws InterruptedException {
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testEnterIfWithTimeoutFailsWhenConditionIsNotSatisfied() throws InterruptedException, TimeoutException {
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 fail("Expected TimeoutException");
 }

 @Test
 public void testEnterIfWithTimeoutBlocksWhenConditionIsNotSatisfied() throws InterruptedException, TimeoutException {
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 Thread.sleep(100); // Allow thread1 and thread2 to enter and exit
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 assertSame(monitor, guard.monitor);
 }

 @Test
 public void testTryEnterIfSucceedsWhenConditionIsSatisfied() {
 monitor.tryEnterIf(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testTryEnterIfFailsWhenConditionIsNotSatisfied() {
 monitor.tryEnterIf(guard);
 assertSame(monitor, guard.monitor);
 thread1.start();
 thread2.start();
 monitor.leave();
 monitor.tryEnterIf(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testTryEnterIfDoesNotBlockWhenConditionIsNotSatisfied() {
 monitor.tryEnterIf(guard);
 assertSame(monitor, guard.monitor);
 thread1.start();
 thread2.start();
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForWithGuardSucceedsWhenConditionIsSatisfied() throws InterruptedException {
 monitor.waitFor(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testWaitForWithGuardBlocksWhenConditionIsNotSatisfied() throws InterruptedException {
 monitor.waitFor(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 thread1.start();
 thread2.start();
 monitor.waitFor(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testWaitForUninterruptiblyWithGuardSucceedsWhenConditionIsSatisfied() throws InterruptedException {
 monitor.waitForUninterruptibly(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptiblyWithGuardBlocksWhenConditionIsNotSatisfied() throws InterruptedException {
 monitor.waitForUninterruptibly(guard);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 thread1.start();
 thread2.start();
 monitor.waitForUninterruptibly(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testWaitForWithGuardAndTimeoutSucceedsWhenConditionIsSatisfied() throws InterruptedException, TimeoutException {
 monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testWaitForWithGuardAndTimeoutBlocksWhenConditionIsNotSatisfied() throws InterruptedException, TimeoutException {
 monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
 fail("Expected TimeoutException");
 }

 @Test
 public void testWaitForUninterruptiblyWithGuardAndTimeoutSucceedsWhenConditionIsSatisfied() throws InterruptedException, TimeoutException {
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertSame(monitor, guard.monitor);
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptiblyWithGuardAndTimeoutBlocksWhenConditionIsNotSatisfied() throws InterruptedException, TimeoutException {
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException");
 }

 @Test
 public void testLeaveReleasesLock() {
 monitor.enter();
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @After
 public void tearDown() throws InterruptedException {
 thread1.join();
 thread2.join();
 }
}