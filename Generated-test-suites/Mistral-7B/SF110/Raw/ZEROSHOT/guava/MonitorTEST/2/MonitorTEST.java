package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import static com.google.common.base.Preconditions.*;

@Beta
public class MonitorTest {

 private Monitor monitor;
 private ReentrantLock lock;
 private Condition condition;

 @Before
 public void setUp() {
 monitor = new Monitor();
 lock = monitor.lock;
 condition = monitor.lock.newCondition();
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyInterrupted() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 fail();
 }

 @Test
 public void testEnterInterruptiblyNotInterrupted() throws InterruptedException {
 monitor.enterInterruptibly();
 assertFalse(Thread.interrupted());
 }

 @Test
 public void testEnterWithTimeUnit() {
 long start = System.nanoTime();
 monitor.enter(1, TimeUnit.NANOSECONDS);
 long end = System.nanoTime();
 long duration = end - start;
 assertTrue(lock.isHeldByCurrentThread());
 assertTrue(duration > 0);
 }

 @Test
 public void testEnterWhenGuardSatisfied() throws InterruptedException {
 Guard guard = new GuardMonitorSatisfied();
 monitor.enterWhen(guard);
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenGuardNotSatisfied() throws InterruptedException {
 Guard guard = new GuardMonitorNotSatisfied();
 monitor.enterWhen(guard);
 fail();
 }

 @Test
 public void testEnterWhenUninterruptiblyGuardSatisfied() {
 Guard guard = new GuardMonitorSatisfied();
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenUninterruptiblyGuardNotSatisfied() {
 Guard guard = new GuardMonitorNotSatisfied();
 monitor.enterWhenUninterruptibly(guard);
 fail();
 }

 @Test
 public void testEnterWhenWithTimeUnitGuardSatisfied() throws InterruptedException {
 Guard guard = new GuardMonitorSatisfied();
 long start = System.nanoTime();
 monitor.enterWhen(guard, 1, TimeUnit.NANOSECONDS);
 long end = System.nanoTime();
 long duration = end - start;
 assertTrue(lock.isHeldByCurrentThread());
 assertTrue(duration > 0);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithTimeUnitGuardNotSatisfied() throws InterruptedException {
 Guard guard = new GuardMonitorNotSatisfied();
 monitor.enterWhen(guard, 1, TimeUnit.NANOSECONDS);
 fail();
 }

 @Test
 public void testEnterIfGuardSatisfied() {
 Guard guard = new GuardMonitorSatisfied();
 monitor.enterIf(guard);
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfGuardNotSatisfied() {
 Guard guard = new GuardMonitorNotSatisfied();
 monitor.enterIf(guard);
 fail();
 }

 @Test
 public void testEnterIfInterruptiblyGuardSatisfied() throws InterruptedException {
 Guard guard = new GuardMonitorSatisfied();
 monitor.enterIfInterruptibly(guard);
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyGuardNotSatisfied() throws InterruptedException {
 Guard guard = new GuardMonitorNotSatisfied();
 monitor.enterIfInterruptibly(guard);
 fail();
 }

 @Test
 public void testEnterIfWithTimeUnitGuardSatisfied() {
 Guard guard = new GuardMonitorSatisfied();
 monitor.enterIf(guard, 1, TimeUnit.NANOSECONDS);
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfWithTimeUnitGuardNotSatisfied() throws InterruptedException {
 Guard guard = new GuardMonitorNotSatisfied();
 monitor.enterIf(guard, 1, TimeUnit.NANOSECONDS);
 fail();
 }

 @Test
 public void testEnterIfWithTimeUnitInterrupted() {
 Guard guard = new GuardMonitorInterrupted();
 monitor.enterIf(guard, 1, TimeUnit.NANOSECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 assertTrue(Thread.interrupted());
 }

 @Test
 public void testTryEnter() {
 monitor.tryEnter();
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterInterrupted() throws InterruptedException {
 monitor.tryEnter();
 Thread.currentThread().interrupt();
 assertFalse(lock.isHeldByCurrentThread());
 assertTrue(Thread.interrupted());
 }

 @Test
 public void testTryEnterWithTimeUnit() {
 long start = System.nanoTime();
 boolean result = monitor.tryEnter(1, TimeUnit.NANOSECONDS);
 long end = System.nanoTime();
 long duration = end - start;
 if (result) {
 assertTrue(lock.isHeldByCurrentThread());
 } else {
 assertTrue(duration > 0);
 }
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterWithTimeUnitInterrupted() throws InterruptedException {
 long start = System.nanoTime();
 boolean result = monitor.tryEnter(1, TimeUnit.NANOSECONDS);
 Thread.currentThread().interrupt();
 assertFalse(lock.isHeldByCurrentThread());
 assertFalse(result);
 assertTrue(Thread.interrupted());
 }

 @Test
 public void testIsFair() {
 Monitor monitorFair = new Monitor(true);
 Monitor monitorNotFair = new Monitor(false);
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
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 monitor.enter();
 monitor.enter();
 assertEquals(2, monitor.getQueueLength());
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.enter();
 assertTrue(monitor.hasQueuedThreads());
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads());
 }

 @Test
 public void testHasQueuedThread() {
 Thread thread1 = new Thread();
 Thread thread2 = new Thread();
 monitor.enter();
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThread(thread1));
 assertFalse(monitor.hasQueuedThread(thread2));
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 assertTrue(monitor.hasQueuedThread(thread1));
 assertFalse(monitor.hasQueuedThread(thread2));
 }

 @Test
 public void testHasWaiters() {
 Guard guard = new GuardMonitorSatisfied();
 monitor.enterWhen(guard);
 monitor.enterWhen(guard);
 assertTrue(monitor.hasWaiters(guard));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasWaiters(guard));
 }

 @Test
 public void testGetWaitQueueLength() throws InterruptedException {
 Guard guard = new GuardMonitorSatisfied();
 monitor.enterWhen(guard);
 monitor.enterWhen(guard);
 monitor.enterWhen(guard);
 monitor.leave();
 monitor.leave();
 monitor.leave();
 assertEquals(2, monitor.getWaitQueueLength(guard));
 }

}

 class GuardMonitorSatisfied extends Monitor.Guard {
 private boolean isSatisfied = true;

 @Override
 public boolean isSatisfied() {
 return isSatisfied;
 }
 }

 class GuardMonitorNotSatisfied extends Monitor.Guard {
 private boolean isSatisfied = false;

 @Override
 public boolean isSatisfied() {
 return isSatisfied;
 }
 }

 class GuardMonitorInterrupted extends Monitor.Guard {
 private boolean isSatisfied = false;
 private boolean isInterrupted = false;

 @Override
 public boolean isSatisfied() {
 return isSatisfied;
 }

 @Override
 public void await() throws InterruptedException {
 Thread.currentThread().interrupt();
 isInterrupted = true;
 }

 public boolean wasInterrupted() {
 return isInterrupted;
 }
 }