package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.experimental.categories.Priority;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

@Category(IntegrationTests.class)
public class MonitorTest {

 private Monitor monitor;
 private Thread threadA;
 private Thread threadB;

 @Test
 public void testEnterAndLeave() {
 monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 }

 @Test
 @Priority(1)
 public void testEnterInterruptibly() throws InterruptedException {
 monitor = new Monitor();
 monitor.enterInterruptibly();
 }

 @Test
 @Priority(2)
 public void testEnterInterruptiblyWithInterruption() throws InterruptedException {
 monitor = new Monitor();
 threadA = new Thread(() -> monitor.enterInterruptibly());
 monitor.enter();
 threadA.interrupt();
 monitor.leave();
 }

 @Test
 @Priority(3)
 public void testEnterWithGuard() {
 monitor = new Monitor();
 Guard guard = new GuardWithValue(true);
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test
 @Priority(4)
 public void testEnterWithGuardAndTimeOut() throws InterruptedException {
 monitor = new Monitor();
 Guard guard = new GuardWithValue(false);
 monitor.enterWhen(guard, 1L, TimeUnit.SECONDS);
 }

 @Test
 @Priority(5)
 public void testEnterWithGuardAndInterruption() throws InterruptedException {
 monitor = new Monitor();
 Guard guard = new GuardWithValue(false);
 threadA = new Thread(() -> monitor.enterWhenInterruptibly(guard));
 monitor.enter();
 threadA.interrupt();
 monitor.leave();
 }

 @Test
 @Priority(6)
 public void testEnterIf() {
 monitor = new Monitor();
 Guard guard = new GuardWithValue(true);
 monitor.enterIf(guard);
 monitor.leave();
 }

 @Test
 @Priority(7)
 public void testEnterIfWithInterruption() throws InterruptedException {
 monitor = new Monitor();
 Guard guard = new GuardWithValue(false);
 monitor.enterInterruptibly();
 monitor.enterIf(guard);
 monitor.leave();
 }

 @Test
 @Priority(8)
 public void testEnterIfWithGuardAndTimeOut() {
 monitor = new Monitor();
 Guard guard = new GuardWithValue(false);
 monitor.enterIf(guard, 1L, TimeUnit.SECONDS);
 }

 @Test
 @Priority(9)
 public void testEnterIfInterruptibly() throws InterruptedException {
 monitor = new Monitor();
 Guard guard = new GuardWithValue(false);
 monitor.enterIfInterruptibly(guard);
 }

 @Test
 @Priority(10)
 public void testEnterIfInterruptiblyWithInterruption() throws InterruptedException {
 monitor = new Monitor();
 threadA = new Thread(() -> monitor.enterIfInterruptibly(new GuardWithValue(false)));
 monitor.enterInterruptibly();
 threadA.interrupt();
 monitor.leave();
 }

 @Test
 @Priority(11)
 public void testEnterIfWithGuardAndInterruptionAndTimeOut() throws InterruptedException {
 monitor = new Monitor();
 Guard guard = new GuardWithValue(false);
 threadA = new Thread(() -> monitor.enterIf(guard));
 monitor.enterInterruptibly();
 threadA.interrupt();
 monitor.leave();
 }

 @Test
 @Priority(12)
 public void testWaitForAndNotify() throws InterruptedException {
 monitor = new Monitor();
 Guard guardA = new GuardWithValue(true);
 Guard guardB = new GuardWithValue(false);
 threadA = new Thread(() -> {
 monitor.enterWhen(guardA);
 monitor.waitFor(guardB);
 monitor.leave();
 });
 threadB = new Thread(() -> monitor.enterWhen(guardB));
 monitor.enter();
 threadB.join();
 monitor.leave();
 threadA.join();
 }

 @Test
 @Priority(13)
 public void testWaitForAndNotifyAll() throws InterruptedException {
 monitor = new Monitor();
 Guard guardA = new GuardWithValue(true);
 Guard guardB = new GuardWithValue(false);
 threadA = new Thread(() -> {
 monitor.enterWhen(guardA);
 monitor.waitForUninterruptibly(guardB);
 monitor.leave();
 });
 threadB = new Thread(() -> monitor.enterWhen(guardB));
 monitor.enter();
 threadB.join();
 monitor.leave();
 threadA.join();
 }

 @Test
 @Priority(14)
 public void testWaitForAndNotifyAllWithInterruption() throws InterruptedException {
 monitor = new Monitor();
 Guard guardA = new GuardWithValue(true);
 Guard guardB = new GuardWithValue(false);
 threadA = new Thread(() -> {
 monitor.enterWhenInterruptibly(guardA);
 monitor.waitForUninterruptibly(guardB);
 monitor.leave();
 });
 monitor.enter();
 threadA.interrupt();
 threadB = new Thread(() -> monitor.enterWhen(guardB));
 monitor.leave();
 threadB.join();
 threadA.join();
 }

 @Test
 @Priority(15)
 public void testWaitForAndNotifyAllWithInterruptionAndTimeOut() throws InterruptedException {
 monitor = new Monitor();
 Guard guardA = new GuardWithValue(true);
 Guard guardB = new GuardWithValue(false);
 threadA = new Thread(() -> {
 monitor.enterInterruptibly();
 monitor.waitForUninterruptibly(guardB, 1L, TimeUnit.SECONDS);
 monitor.leave();
 });
 monitor.enter();
 threadB = new Thread(() -> monitor.enterWhen(guardB));
 monitor.leave();
 threadB.join();
 threadA.join();
 }

 @Test
 @Priority(16)
 public void testIsFair() {
 monitor = new Monitor();
 Assert.assertFalse(monitor.isFair());
 monitor = new Monitor(true);
 Assert.assertTrue(monitor.isFair());
 }

 @Test
 @Priority(17)
 public void testIsOccupied() throws InterruptedException {
 monitor = new Monitor();
 monitor.enter();
 Assert.assertTrue(monitor.isOccupied());
 monitor.leave();
 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 @Priority(18)
 public void testIsOccupiedByCurrentThread() throws InterruptedException {
 monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 Assert.assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 Assert.assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 @Priority(19)
 public void testGetOccupiedDepth() throws InterruptedException {
 monitor = new Monitor();
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 monitor.leave();
 Assert.assertEquals(3, monitor.getOccupiedDepth());
 }

 @Test
 @Priority(20)
 public void testGetQueueLength() throws InterruptedException {
 monitor = new Monitor();
 threadA = new Thread(() -> monitor.enterInterruptibly());
 threadB = new Thread(() -> monitor.enterInterruptibly());
 monitor.enter();
 monitor.leave();
 Assert.assertEquals(2, monitor.getQueueLength());
 }

 @Test
 @Priority(21)
 public void testHasQueuedThreads() throws InterruptedException {
 monitor = new Monitor();
 threadA = new Thread(() -> monitor.enterInterruptibly());
 threadB = new Thread(() -> monitor.enterInterruptibly());
 monitor.enter();
 monitor.leave();
 Assert.assertTrue(monitor.hasQueuedThreads());
 }

 @Test
 @Priority(22)
 public void testHasQueuedThread() throws InterruptedException {
 monitor = new Monitor();
 threadA = new Thread(() -> monitor.enterInterruptibly());
 threadB = new Thread(() -> monitor.enterInterruptibly());
 monitor.enter();
 monitor.leave();
 Assert.assertTrue(monitor.hasQueuedThread(threadA));
 Assert.assertTrue(monitor.hasQueuedThread(threadB));
 }

 @Test
 @Priority(23)
 public void testHasWaiters() throws InterruptedException {
 monitor = new Monitor();
 threadA = new Thread(() -> monitor.enterInterruptibly());
 threadB = new Thread(() -> monitor.enterInterruptibly());
 monitor.enter();
 monitor.leave();
 Guard guard = new GuardWithValue(false);
 monitor.enterWhen(guard);
 monitor.leave();
 Assert.assertTrue(monitor.hasWaiters(guard));
 }

 @Test
 @Priority(24)
 public void testIsSatisfied() {
 Guard guard = new GuardWithValue(true);
 Assert.assertTrue(guard.isSatisfied());
 }

 @Test
 @Priority(25)
 public void testIsSatisfiedFailed() {
 Guard guard = new GuardWithSatisfiedFailedValue();
 Assert.assertFalse(guard.isSatisfied());
 }

 private static class GuardWithValue extends Monitor.Guard {
 private final boolean value;

 GuardWithValue(boolean value) {
 this.value = value;
 }

 @Override
 public boolean isSatisfied() {
 return value;
 }
 }

 private static class GuardWithSatisfiedFailedValue extends Monitor.Guard {
 @Override
 public boolean isSatisfied() {
 throw new RuntimeException("Guard is not satisfied");
 }
 }
}