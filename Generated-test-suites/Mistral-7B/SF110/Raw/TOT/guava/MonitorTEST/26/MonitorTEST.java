package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

public class MonitorTest {

 private Monitor monitor;
 private final Object guardCondition = new Object();
 private final Thread interrupterThread;

 @Before
 public void setUp() {
 monitor = new Monitor();
 interrupterThread = new Thread(() -> {});
 }

 @Test
 public void testEnter() {
 Thread thread = new Thread(() -> {
 monitor.enter();
 });
 monitor.enter();
 thread.start();
 try {
 thread.join();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 }

 @Test
 public void testEnterInterruptibly() {
 Thread thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 });
 monitor.enter();
 thread.start();
 try {
 thread.join();
 } catch (InterruptedException e) {
 monitor.enterInterruptibly();
 }
 }

 @Test
 public void testEnterWithTimeout() {
 long timeout = 1;
 Thread thread = new Thread(() -> {
 try {
 monitor.enter(timeout, TimeUnit.MILLISECONDS);
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 });
 monitor.enter();
 thread.start();
 try {
 thread.join();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 } catch (TimeoutException e) {
 // expected
 }
 monitor.enter(timeout, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testEnterWhenGuardIsSatisfied() {
 monitor.enterWhen(new MyGuard(monitor, guardCondition));
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenGuardIsNotSatisfied() {
 monitor.enterWhen(new MyGuard(monitor, guardCondition));
 monitor.awaitUninterruptibly(guardCondition, false);
 }

 @Test
 public void testEnterWhenInterrupted() {
 monitor.enterWhen(new MyInterruptedGuard(monitor, interrupterThread));
 interrupterThread.interrupt();
 }

 @Test
 public void testEnterWhenInterruptedWithTimeout() {
 monitor.enterWhen(new MyInterruptedGuard(monitor, interrupterThread));
 interrupterThread.start();
 Thread.sleep(100);
 monitor.enterWhen(new MyInterruptedGuard(monitor, interrupterThread), 50, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testEnterIf() {
 Guard guard = new MyGuard(monitor, guardCondition);
 monitor.enterIf(guard);
 assertFalse(guard.isSatisfied());
 monitor.awaitUninterruptibly(guard, false);
 assertTrue(guard.isSatisfied());
 monitor.enterIf(guard);
 }

 @Test
 public void testEnterIfInterruptibly() {
 Guard guard = new MyGuard(monitor, guardCondition);
 monitor.enterIfInterruptibly(guard);
 assertFalse(guard.isSatisfied());
 monitor.awaitUninterruptibly(guard, false);
 assertTrue(guard.isSatisfied());
 monitor.enterIfInterruptibly(guard);
 }

 @Test
 public void testEnterIfWithTimeout() {
 long timeout = 1;
 Guard guard = new MyGuard(monitor, guardCondition);
 monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS);
 assertFalse(guard.isSatisfied());
 monitor.awaitUninterruptibly(guard, false);
 assertTrue(guard.isSatisfied());
 monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeout() {
 long timeout = 1;
 Guard guard = new MyInterruptedGuard(monitor, interrupterThread);
 monitor.enterIfInterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 assertFalse(guard.isSatisfied());
 monitor.awaitUninterruptibly(guard, false);
 assertTrue(guard.isSatisfied());
 monitor.enterIfInterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testEnterIfTry() {
 Guard guard = new MyGuard(monitor, guardCondition);
 monitor.enterIfTry(guard);
 assertFalse(guard.isSatisfied());
 monitor.awaitUninterruptibly(guard, false);
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testWaitFor() {
 Thread thread = new Thread(() -> {
 monitor.enter();
 monitor.waitFor(guardCondition);
 monitor.leave();
 });
 monitor.enter();
 monitor.waitFor(guardCondition);
 thread.start();
 try {
 thread.join();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptibly() {
 Thread thread = new Thread(() -> {
 monitor.enter();
 monitor.waitForUninterruptibly(guardCondition);
 monitor.leave();
 });
 monitor.enter();
 monitor.waitForUninterruptibly(guardCondition);
 thread.start();
 try {
 thread.join();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 monitor.leave();
 }

 @Test
 public void testWaitForWithTimeout() {
 long timeout = 1;
 Thread thread = new Thread(() -> {
 try {
 monitor.enter();
 monitor.waitFor(guardCondition, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 } catch (TimeoutException e) {
 // expected
 }
 });
 monitor.enter();
 thread.start();
 try {
 thread.join();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 } catch (TimeoutException e) {
 // expected
 }
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeout() {
 long timeout = 1;
 Thread thread = new Thread(() -> {
 try {
 monitor.enter();
 monitor.waitForUninterruptibly(guardCondition, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 });
 monitor.enter();
 thread.start();
 try {
 thread.join();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 monitor.leave();
 }

 @Test
 public void testLeave() {
 monitor.enter();
 monitor.leave();
 }

 @Test
 public void testIsFair() {
 assertTrue(monitor.isFair());
 monitor = new Monitor(true);
 assertTrue(monitor.isFair());
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
 assertEquals(2, monitor.getOccupiedDepth());
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 List<Thread> threads = new ArrayList<>();
 for (int i = 0; i < 10; i++) {
 threads.add(new Thread(() -> {
 try {
 monitor.enter();
 monitor.waitFor(guardCondition);
 monitor.leave();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 }));
 }
 monitor.enter();
 for (Thread thread : threads) {
 thread.start();
 }
 assertEquals(9, monitor.getQueueLength());
 monitor.leave();
 for (Thread thread : threads) {
 try {
 thread.join();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 }
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.awaitUninterruptibly(guardCondition, false);
 monitor.leave();
 Thread thread = new Thread(() -> {
 monitor.enter();
 monitor.awaitUninterruptibly(guardCondition, false);
 monitor.leave();
 });
 thread.start();
 assertTrue(monitor.hasQueuedThreads(guardCondition));
 monitor.awaitUninterruptibly(guardCondition, false);
 assertFalse(monitor.hasQueuedThreads(guardCondition));
 }

 @Test
 public void testHasQueuedThread() {
 monitor.enter();
 monitor.awaitUninterruptibly(guardCondition, false);
 monitor.leave();
 Thread thread = new Thread(() -> {
 monitor.enter();
 monitor.awaitUninterruptibly(guardCondition, false);
 monitor.leave();
 });
 thread.start();
 assertTrue(monitor.hasQueuedThread(thread));
 monitor.awaitUninterruptibly(guardCondition, false);
 assertFalse(monitor.hasQueuedThread(thread));
 }

 @Test
 public void testSignalNextWaiter() {
 monitor.enter();
 monitor.beginWaitingFor(new MyGuard(monitor, guardCondition));
 monitor.signalNextWaiter();
 monitor.leave();
 monitor.awaitUninterruptibly(guardCondition, false);
 assertTrue(monitor.isSatisfied(monitor.getActiveGuards()));
 }

 @Test
 public void testIsSatisfied() {
 monitor.enter();
 monitor.beginWaitingFor(new MyGuard(monitor, guardCondition));
 assertFalse(monitor.isSatisfied(monitor.getActiveGuards()));
 monitor.signalNextWaiter();
 assertTrue(monitor.isSatisfied(monitor.getActiveGuards()));
 }

 @Test
 public void testSignalAllWaiters() {
 monitor.enter();
 monitor.beginWaitingFor(new MyGuard(monitor, guardCondition));
 monitor.beginWaitingFor(new MyGuard(monitor, guardCondition));
 monitor.signalAllWaiters();
 monitor.leave();
 monitor.awaitUninterruptibly(guardCondition, false);
 assertTrue(monitor.isSatisfied(monitor.getActiveGuards()));
 }

 class MyGuard extends Guard {
 private final Object condition;

 MyGuard(Monitor monitor, Object condition) {
 super(monitor);
 this.condition = Preconditions.checkNotNull(condition, "condition");
 }

 @Override
 public boolean isSatisfied() {
 return true;
 }
 }

 class MyInterruptedGuard extends Guard {
 private final Thread thread;

 MyInterruptedGuard(Monitor monitor, Thread thread) {
 super(monitor);
 this.thread = Preconditions.checkNotNull(thread, "thread");
 }

 @Override
 public boolean isSatisfied() {
 return thread.isInterrupted();
 }
 }
}