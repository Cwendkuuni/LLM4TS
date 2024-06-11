package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;

public class MonitorTest {

 @Test
 public void testEnter() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterOutsideMonitor() {
 Monitor monitor = new Monitor();
 Thread thread = new Thread(() -> monitor.enter());
 monitor.enter(); // This thread should already own the lock
 thread.start();
 thread.join();
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWithTimeout() {
 Monitor monitor = new Monitor();
 boolean entered = monitor.enter(1, TimeUnit.SECONDS);
 assertTrue(entered);
 monitor.leave();
 }

 @Test
 public void testEnterWithTimeoutAndInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor();
 boolean entered = monitor.enter(1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 assertFalse(entered);
 }

 @Test
 public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 boolean entered = monitor.enterInterruptibly(1, TimeUnit.SECONDS);
 assertTrue(entered);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyWithTimeoutAndInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly(1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 }

 @Test
 public void testTryEnter() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.tryEnter());
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.tryEnter());
 }

 @Test
 public void testTryEnterInterruptedThread() {
 Monitor monitor = new Monitor();
 Thread thread = new Thread(() -> monitor.enter());
 monitor.enter(); // This thread should already own the lock
 thread.start();
 monitor.leave();
 assertFalse(monitor.tryEnter());
 thread.join();
 }

 @Test
 public void testTryEnterWithTimeout() {
 Monitor monitor = new Monitor();
 boolean tried = monitor.tryEnter(1, TimeUnit.SECONDS);
 assertFalse(tried);
 monitor.leave();
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.tryEnter(1, TimeUnit.SECONDS));
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterWithTimeoutInterruptedThread() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter(); // This thread should already own the lock
 Thread thread = new Thread(() -> monitor.tryEnter(1, TimeUnit.SECONDS));
 monitor.leave();
 thread.join();
 assertFalse(monitor.tryEnter(1, TimeUnit.SECONDS));
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enterWhen(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithDifferentMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardWithCondition(monitor1);
 monitor2.enter(); // This thread should already own the lock
 monitor2.enterWhen(guard);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithConditionNotSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 assertFalse(guard.isSatisfied());
 monitor.enterWhen(guard);
 }

 @Test
 public void testEnterWhenWithConditionSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 guard.setSatisfied(true);
 monitor.enterWhen(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenWithConditionSatisfiedAndTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 guard.setSatisfied(true);
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenWithConditionSatisfiedAndTimeoutInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 guard.setSatisfied(true);
 Thread thread = new Thread(() -> monitor.enterWhen(guard, 1, TimeUnit.SECONDS));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 thread.join();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenWithConditionInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 Thread thread = new Thread(() -> monitor.enterWhen(guard));
 monitor.leave();
 thread.join();
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithConditionNotSatisfied() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 guard.setSatisfied(true);
 }

 @Test
 public void testEnterWhenUninterruptiblyWithConditionSatisfied() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithConditionSatisfiedAndTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 guard.setSatisfied(true);
 monitor.leave();
 monitor.enter();
 monitor.leave();
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenUninterruptiblyWithConditionInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 Thread thread = new Thread(() -> monitor.enterWhenUninterruptibly(guard));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 thread.join();
 }

 @Test
 public void testEnterIf() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter();
 monitor.enterIf(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfWithDifferentMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardWithCondition(monitor1);
 monitor2.enter(); // This thread should already own the lock
 monitor2.enterIf(guard);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfWithConditionNotSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 monitor.enterIf(guard);
 }

 @Test
 public void testEnterIfWithConditionSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 monitor.leave();
 monitor.enterIf(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfWithConditionSatisfiedAndTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 monitor.leave();
 monitor.enterIf(guard, 1, TimeUnit.SECONDS);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfWithConditionSatisfiedAndTimeoutInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 monitor.leave();
 Thread thread = new Thread(() -> monitor.enterIf(guard, 1, TimeUnit.SECONDS));
 assertFalse(monitor.isOccupiedByCurrentThread());
 thread.join();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfWithConditionInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 Thread thread = new Thread(() -> monitor.enterIf(guard));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 thread.join();
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter();
 monitor.enterIfInterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfInterruptiblyWithDifferentMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardWithCondition(monitor1);
 monitor2.enter(); // This thread should already own the lock
 monitor2.enterIfInterruptibly(guard);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfInterruptiblyWithConditionNotSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 monitor.enterIfInterruptibly(guard);
 }

 @Test
 public void testEnterIfInterruptiblyWithConditionSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 monitor.leave();
 monitor.enterIfInterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptiblyWithConditionSatisfiedAndTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 monitor.leave();
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyWithConditionInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 monitor.leave();
 Thread thread = new Thread(() -> monitor.enterIfInterruptibly(guard));
 assertFalse(monitor.isOccupiedByCurrentThread());
 thread.join();
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter();
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyWithTimeoutInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter(); // This thread should already own the lock
 thread.currentThread().interrupt();
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterIf() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 assertFalse(monitor.tryEnterIf(guard));
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.tryEnterIf(guard));
 }

 @Test
 public void testTryEnterIfWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter();
 monitor.leave();
 monitor.enterIf(guard);
 monitor.leave();
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.tryEnterIf(guard, 1, TimeUnit.SECONDS));
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterIfWithTimeoutInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter();
 monitor.leave();
 monitor.enterIf(guard);
 monitor.leave();
 Thread thread = new Thread(() -> monitor.tryEnterIf(guard, 1, TimeUnit.SECONDS));
 assertFalse(monitor.isOccupiedByCurrentThread());
 thread.join();
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterIfInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardWithCondition(monitor);
 monitor.enter();
 Thread thread = new Thread(() -> monitor.tryEnterIf(guard));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 thread.join();
 }
}

class GuardWithCondition extends Monitor.Guard {
 private final Monitor monitor;
 private final Condition condition;
 private boolean isSatisfied = false;

 public GuardWithCondition(Monitor monitor) {
 super(monitor);
 this.monitor = monitor;
 this.condition = monitor.lock.newCondition();
 }

 @Override
 public boolean isSatisfied() {
 return isSatisfied;
 }

 public void setSatisfied(boolean satisfied) {
 this.isSatisfied = satisfied;
 }
}