package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import com.google.common.base.*;
import com.google.common.util.concurrent.Monitor.*;

public class MonitorTest {

 private Monitor monitor;

 @Before
 public void setUp() {
 monitor = new Monitor();
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithTimeout() throws InterruptedException, TimeoutException {
 monitor.enter();
 Thread interrupter = Thread.currentThread();
 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 Thread.sleep(2);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 interrupter.interrupt();
 monitor.leave();
 assertFalse(Thread.interrupted());
 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 assertTrue(Thread.interrupted());
 monitor.leave();
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenWithTimeout() throws InterruptedException, TimeoutException {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 assertTrue(monitor.isOccupiedByCurrentThread());
 Thread.sleep(2);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeout() throws InterruptedException {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIf() {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterIf(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterIfInterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfWithTimeout() throws InterruptedException, TimeoutException {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeout() throws InterruptedException {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 Thread.interrupted();
 }

 @Test
 public void testTryEnter() {
 monitor.enter();
 monitor.tryEnter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test
 public void testTryEnterUninterruptibly() {
 monitor.enter();
 monitor.tryEnter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test
 public void testTryEnterWithTimeout() throws InterruptedException, TimeoutException {
 monitor.enter();
 monitor.tryEnter(1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test
 public void testTryEnterIf() {
 monitor.enter();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.tryEnterIf(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterIfInterruptibly() throws InterruptedException {
 monitor.enter();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.tryEnterIfInterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterIfWithTimeout() throws InterruptedException, TimeoutException {
 monitor.enter();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.tryEnterIf(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test
 public void testTryEnterIfInterruptiblyWithTimeout() throws InterruptedException {
 monitor.enter();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.tryEnterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 Thread.interrupted();
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
 monitor.enter();
 monitor.enter();
 assertEquals(1, monitor.getQueueLength());
 monitor.leave();
 monitor.enter();
 assertEquals(1, monitor.getQueueLength());
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.enter();
 assertTrue(monitor.hasQueuedThreads());
 monitor.leave();
 monitor.enter();
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads());
 }

 @Test
 public void testHasQueuedThread() {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 assertTrue(monitor.hasQueuedThread(otherMonitor.getThreads()[0]));
 monitor.leave();
 assertFalse(monitor.hasQueuedThread(otherMonitor.getThreads()[0]));
 }

 @Test
 public void testHasWaiters() {
 monitor.enter();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard);
 assertTrue(monitor.hasWaiters(guard));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasWaiters(guard));
 }
}