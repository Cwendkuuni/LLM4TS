package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

 private Monitor monitor;
 private Thread thread1;
 private Thread thread2;
 private Thread thread3;
 private Guard guard1;
 private Guard guard2;
 private Condition condition;

 @Before
 public void setUp() {
 monitor = new Monitor();
 thread1 = new Thread(() -> enterTest(monitor, guard1));
 thread2 = new Thread(() -> enterTest(monitor, guard2));
 thread3 = new Thread(() -> enterTestInterruptiblyNegativeTest(monitor));
 thread1.start();
 thread2.start();
 thread3.start();
 try {
 thread1.join();
 thread2.join();
 thread3.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 guard1 = new MyGuard(monitor);
 guard2 = new MyGuard(monitor);
 condition = monitor.lock.newCondition();
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyPositiveTest() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyNegativeTest() throws InterruptedException {
 monitor.enterInterruptibly();
 thread3.interrupt();
 monitor.enterInterruptibly();
 fail("Expected InterruptedException, but the test passed");
 }

 @Test
 public void testEnterWithTimeOutPositiveTest() {
 long timeout = 100;
 boolean success = monitor.enter(timeout, TimeUnit.MILLISECONDS);
 assertTrue(success);
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithTimeOutNegativeTest() throws InterruptedException {
 long timeout = 100;
 thread3.interrupt();
 monitor.enter(timeout, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException, but the test passed");
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 guard1.await();
 monitor.enterWhen(guard1);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithWrongMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 otherMonitor.enterWhen(guard1); //monitor and otherMonitor are not the same
 fail("Expected IllegalMonitorStateException, but the test passed");
 }

 @Test
 public void testEnterWhenUninterruptibly() throws InterruptedException {
 guard1.awaitUninterruptibly();
 monitor.enterWhenUninterruptibly(guard1);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenUninterruptiblyWithWrongMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 otherMonitor.enterWhenUninterruptibly(guard1); //monitor and otherMonitor are not the same
 fail("Expected IllegalMonitorStateException, but the test passed");
 }

 @Test
 public void testEnterWhenWithTimeOutPositiveTest() throws InterruptedException {
 long timeout = 100;
 boolean success = monitor.enterWhen(guard1, timeout, TimeUnit.MILLISECONDS);
 assertTrue(success);
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenWithTimeOutNegativeTest() throws InterruptedException {
 thread3.interrupt();
 long timeout = 100;
 monitor.enterWhen(guard1, timeout, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException, but the test passed");
 }

 @Test
 public void testTryEnter() {
 monitor.tryEnter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterInterruptiblyPositiveTest() {
 monitor.tryEnterInterruptibly();
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterInterruptiblyNegativeTest() throws InterruptedException {
 thread3.interrupt();
 monitor.tryEnterInterruptibly();
 fail("Expected InterruptedException, but the test passed");
 }

 @Test
 public void testTryEnterWithTimeOutPositiveTest() {
 long timeout = 100;
 boolean success = monitor.tryEnter(timeout, TimeUnit.MILLISECONDS);
 assertTrue(success);
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterWithTimeOutNegativeTest() throws InterruptedException {
 thread3.interrupt();
 long timeout = 100;
 monitor.tryEnter(timeout, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException, but the test passed");
 }

 @Test
 public void testTryEnterIf() {
 monitor.tryEnterIf(guard1);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterIfInterruptiblyPositiveTest() throws InterruptedException {
 monitor.tryEnterIfInterruptibly(guard1);
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterIfInterruptiblyNegativeTest() throws InterruptedException {
 thread3.interrupt();
 monitor.tryEnterIfInterruptibly(guard1);
 fail("Expected InterruptedException, but the test passed");
 }

 @Test
 public void testTryEnterIfWithTimeOutPositiveTest() {
 long timeout = 100;
 boolean success = monitor.tryEnterIf(guard1, timeout, TimeUnit.MILLISECONDS);
 assertTrue(success);
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterIfWithTimeOutNegativeTest() throws InterruptedException {
 thread3.interrupt();
 long timeout = 100;
 monitor.tryEnterIf(guard1, timeout, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException, but the test passed");
 }

 @Test
 public void testTryEnterIfNegativeTest() {
 monitor.tryEnterIf(guard1);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 guard1.await();
 monitor.waitFor(guard1);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForWithWrongMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 otherMonitor.waitFor(guard1); //monitor and otherMonitor are not the same
 fail("Expected IllegalMonitorStateException, but the test passed");
 }

 @Test
 public void testWaitForUninterruptibly() throws InterruptedException {
 guard1.awaitUninterruptibly();
 monitor.waitForUninterruptibly(guard1);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForUninterruptiblyWithWrongMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 otherMonitor.waitForUninterruptibly(guard1); //monitor and otherMonitor are not the same
 fail("Expected IllegalMonitorStateException, but the test passed");
 }

 @Test
 public void testWaitForWithTimeOutPositiveTest() throws InterruptedException {
 long timeout = 100;
 boolean success = monitor.waitFor(guard1, timeout, TimeUnit.MILLISECONDS);
 assertTrue(success);
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForWithTimeOutNegativeTest() throws InterruptedException {
 thread3.interrupt();
 long timeout = 100;
 monitor.waitFor(guard1, timeout, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException, but the test passed");
 }

 @Test
 public void testLeave() {
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testIsFair() {
 Monitor fairMonitor = new Monitor(true);
 Monitor unfairMonitor = new Monitor(false);
 assertTrue(fairMonitor.isFair());
 assertFalse(unfairMonitor.isFair());
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
 monitor.leave();
 monitor.enter();
 int depth = monitor.getOccupiedDepth();
 assertEquals(2, depth);
 }

 @Test
 public void testGetQueueLength() {
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 int length = monitor.getQueueLength();
 assertEquals(0, length);
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 boolean hasQueuedThreads = monitor.hasQueuedThreads(guard1);
 assertFalse(hasQueuedThreads);
 }

 @Test
 public void testHasQueuedThread() {
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 Monitor monitor2 = new Monitor();
 monitor2.enter();
 boolean hasQueuedThread = monitor.hasQueuedThread(monitor2.thread);
 assertFalse(hasQueuedThread);
 }

 @Test
 public void testHasWaiters() {
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 int waiterCount = monitor.getWaitQueueLength(guard1);
 assertEquals(0, waiterCount);
 }

 @Test
 public void testSignalNextWaiter() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Monitor monitor3 = new Monitor();
 Guard guard1 = new MyGuard(monitor1);
 Guard guard2 = new MyGuard(monitor2);
 Guard guard3 = new MyGuard(monitor3);
 monitor1.enter();
 monitor1.enter();
 monitor1.signalNextWaiter();
 monitor2.enterWhen(guard2);
 monitor2.enter();
 monitor2.signalNextWaiter();
 monitor3.enterWhen(guard3);
 int waiterCount = monitor3.getWaitQueueLength(guard3);
 assertEquals(1, waiterCount);
 }

 @Test
 public void testIsSatisfied() {
 guard1.isSatisfied = () -> true;
 assertTrue(guard1.isSatisfied());
 }

 @Test
 public void testIsSatisfiedNegativeTest() {
 guard1.isSatisfied = () -> false;
 assertFalse(guard1.isSatisfied());
 }

 private void enterTest(Monitor monitor, Guard guard) throws InterruptedException {
 monitor.enter();
 guard.await();
 }
}