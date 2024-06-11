package com.google.common.util.concurrent;

import com.google.common.annotations.*;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import static com.google.common.base.Preconditions.*;
import static org.mockito.Mockito.*;

@Beta
public class MonitorTest {

 final static Monitor monitor = new Monitor();
 final static Guard guard = mock(Guard.class);
 final static Condition condition = mock(Condition.class);

 static {
 monitor.activeGuards = null;
 monitor.lock = new ReentrantLock();
 monitor.lock.newCondition();
 monitor.lock.lock();
 monitor.condition = condition;
 monitor.activeGuards = new Guard(monitor);
 }

 @Test
 public void testEnterAndLeave() {
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.lock.isHeldByCurrentThread());
 monitor.enter();
 monitor.leave();
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterAndLeaveInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.leave();
 assertTrue(monitor.lock.isHeldByCurrentThread());
 monitor.enterInterruptibly();
 monitor.leave();
 assertFalse(monitor.lock.isHeldByCurrentThread());
 }

 @Test
 public void testTryEnter() {
 assertFalse(monitor.tryEnter());
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.tryEnter());
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.enterWhen(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterWhenWithTimeout() throws InterruptedException {
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeout() {
 monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.SECONDS);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterIf() {
 monitor.enterIf(guard);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.enterIf(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 monitor.enterIfInterruptibly(guard);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.enterIfInterruptibly(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterIfWithTimeout() {
 monitor.enterIf(guard, 1, TimeUnit.SECONDS);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeout() throws InterruptedException {
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testTryEnterIf() {
 assertFalse(monitor.tryEnterIf(guard));
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.tryEnterIf(guard));
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.waitFor(guard);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.waitFor(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.waitForUninterruptibly(guard);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.waitForUninterruptibly(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testWaitForWithTimeout() throws InterruptedException {
 monitor.waitFor(guard, 1, TimeUnit.SECONDS);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeout() throws InterruptedException {
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.SECONDS);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testIsFair() {
 assertEquals(false, monitor.isFair());
 monitor = new Monitor(true);
 assertEquals(true, monitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 assertFalse(monitor.isOccupied());
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 assertEquals(0, monitor.getOccupiedDepth());
 monitor.enter();
 monitor.leave();
 assertEquals(1, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 assertEquals(0, monitor.getQueueLength());
 monitor.enter();
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 assertFalse(monitor.hasQueuedThreads());
 monitor.enter();
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads());
 }

 @Test
 public void testHasQueuedThread() {
 assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
 monitor.enter();
 monitor.leave();
 assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
 }

 @Test
 public void testHasWaiters() {
 assertFalse(monitor.hasWaiters(guard));
 monitor.enterWhen(guard);
 monitor.leave();
 assertTrue(monitor.hasWaiters(guard));
 }

 @Test
 public void testGetWaitQueueLength() {
 assertEquals(0, monitor.getWaitQueueLength(guard));
 monitor.enterWhen(guard);
 monitor.leave();
 assertEquals(1, monitor.getWaitQueueLength(guard));
 }

 @Test
 public void testSignalNextWaiter() {
 monitor.enter();
 monitor.signalNextWaiter();
 monitor.leave();
 assertFalse(monitor.lock.hasWaiters(guard));
 monitor.enterWhen(guard);
 monitor.leave();
 assertTrue(monitor.lock.hasWaiters(guard));
 monitor.signalNextWaiter();
 monitor.leave();
 assertFalse(monitor.lock.hasWaiters(guard));
 }

 @Test
 public void testIsSatisfied() {
 assertFalse(guard.isSatisfied());
 guard.isSatisfied = () -> true;
 assertTrue(guard.isSatisfied());
 }
}