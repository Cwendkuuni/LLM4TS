package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;
import org.junit.Test;

@Beta
public class MonitorTest {

 private Monitor monitor;
 private final Object condition = new Object();
 private final Guard guard = new MyGuard(monitor);

 @Before
 public void setUp() {
 monitor = new Monitor();
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertEquals(1, monitor.lock.getHoldCount());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithDifferentMonitor() {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter(); // should throw exception
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 assertEquals(1, monitor.lock.getHoldCount());
 }

 @Test
 public void testEnterInterruptiblyWithInterruptedThread() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 assertFalse(monitor.lock.tryLock());
 }

 @Test
 public void testEnterWithTimeOut() throws InterruptedException, TimeoutException {
 monitor.enter(5, TimeUnit.MILLISECONDS);
 assertEquals(1, monitor.lock.getHoldCount());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithTimeOutInterrupted() throws InterruptedException, TimeoutException {
 monitor.enter(5, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 monitor.enter(5, TimeUnit.MILLISECONDS); // should throw exception
 }

 @Test
 public void testEnterInterruptiblyWithTimeOut() throws InterruptedException, TimeoutException {
 monitor.enterInterruptibly(5, TimeUnit.MILLISECONDS);
 assertEquals(1, monitor.lock.getHoldCount());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyWithTimeOutInterrupted() throws InterruptedException, TimeoutException {
 monitor.enterInterruptibly(5, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly(5, TimeUnit.MILLISECONDS); // should throw exception
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyWithSatisfiedCondition() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.enterIfInterruptibly(guard); // should throw exception
 }

 @Test
 public void testEnterIfInterruptiblyWithUnSatisfiedCondition() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.enterIfInterruptibly(guard);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterIfWithSatisfiedCondition() {
 monitor.enter();
 assertTrue(monitor.enterIf(guard));
 }

 @Test
 public void testEnterIfWithUnSatisfiedCondition() {
 monitor.enter();
 assertFalse(monitor.enterIf(guard));
 }

 @Test
 public void testTryEnter() {
 assertTrue(monitor.tryEnter());
 assertEquals(1, monitor.lock.getHoldCount());
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterInterrupted() throws InterruptedException {
 monitor.tryEnter();
 Thread.currentThread().interrupt();
 assertFalse(monitor.tryEnter());
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 monitor.enter();
 monitor.enterWhen(guard);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithDifferentMonitor() throws InterruptedException {
 monitor.enter();
 Monitor otherMonitor = new Monitor();
 otherMonitor.enter();
 monitor.enterWhen(guard); // should throw exception
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor.enter();
 monitor.enterWhenUninterruptibly(guard);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenUninterruptiblyWithDifferentMonitor() {
 monitor.enter();
 Monitor otherMonitor = new Monitor();
 otherMonitor.enter();
 monitor.enterWhenUninterruptibly(guard); // should throw exception
 }

 @Test
 public void testEnterWhenWithTimeOut() throws InterruptedException {
 monitor.enter();
 monitor.enterWhen(guard, 5, TimeUnit.MILLISECONDS);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenWithTimeOutInterrupted() throws InterruptedException {
 monitor.enter();
 monitor.enterWhen(guard, 5, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 monitor.enterWhen(guard, 5, TimeUnit.MILLISECONDS); // should throw exception
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeOut() throws InterruptedException {
 monitor.enter();
 monitor.enterWhenUninterruptibly(guard, 5, TimeUnit.MILLISECONDS);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenUninterruptiblyWithTimeOutInterrupted() throws InterruptedException {
 monitor.enter();
 monitor.enterWhenUninterruptibly(guard, 5, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 monitor.enterWhenUninterruptibly(guard, 5, TimeUnit.MILLISECONDS); // should throw exception
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 monitor.enter();
 assertTrue(monitor.enterIfInterruptibly(guard));
 }

 @Test
 public void testEnterIfInterruptiblyWithUnSatisfiedCondition() throws InterruptedException {
 monitor.enter();
 assertFalse(monitor.enterIfInterruptibly(guard));
 }

 @Test
 public void testEnterIfWithSatisfiedCondition() {
 monitor.enter();
 assertTrue(monitor.enterIf(guard));
 }

 @Test
 public void testEnterIfWithUnSatisfiedCondition() {
 monitor.enter();
 assertFalse(monitor.enterIf(guard));
 }

 @Test
 public void testTryEnterIf() {
 assertTrue(monitor.tryEnterIf(guard));
 assertEquals(1, monitor.lock.getHoldCount());
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterIfInterrupted() throws InterruptedException {
 monitor.tryEnterIf(guard);
 Thread.currentThread().interrupt();
 assertFalse(monitor.tryEnterIf(guard));
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.enter();
 monitor.waitFor(guard);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.enter();
 monitor.waitForUninterruptibly(guard);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test
 public void testWaitForWithTimeOut() throws InterruptedException {
 monitor.enter();
 monitor.waitFor(guard, 5, TimeUnit.MILLISECONDS);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForWithTimeOutInterrupted() throws InterruptedException {
 monitor.enter();
 monitor.waitFor(guard, 5, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 monitor.waitFor(guard, 5, TimeUnit.MILLISECONDS); // should throw exception
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeOut() throws InterruptedException {
 monitor.enter();
 monitor.waitForUninterruptibly(guard, 5, TimeUnit.MILLISECONDS);
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForUninterruptiblyWithTimeOutInterrupted() throws InterruptedException {
 monitor.enter();
 monitor.waitForUninterruptibly(guard, 5, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 monitor.waitForUninterruptibly(guard, 5, TimeUnit.MILLISECONDS); // should throw exception
 }

 @Test
 public void testLeave() {
 monitor.enter();
 monitor.leave();
 assertEquals(0, monitor.lock.getHoldCount());
 }

 @Test
 public void testIsFair() {
 assertEquals(false, monitor.isFair());
 Monitor fairMonitor = new Monitor(true);
 assertEquals(true, fairMonitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 monitor.enter();
 assertTrue(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 monitor.enter();
 monitor.enter();
 assertEquals(2, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 assertEquals(2, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 assertTrue(monitor.hasQueuedThreads(guard));
 }

 @Test
 public void testHasQueuedThread() {
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
 }

 @Test
 public void testHasWaiters() {
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 monitor.enter();
 monitor.waitFor(guard);
 monitor.leave();
 assertTrue(monitor.hasWaiters(guard));
 }

 private static class MyGuard extends Monitor.Guard {
 private final Monitor monitor;

 MyGuard(Monitor monitor) {
 super(monitor);
 this.monitor = monitor;
 }

 @Override
 public boolean isSatisfied() {
 return true;
 }
 }
}