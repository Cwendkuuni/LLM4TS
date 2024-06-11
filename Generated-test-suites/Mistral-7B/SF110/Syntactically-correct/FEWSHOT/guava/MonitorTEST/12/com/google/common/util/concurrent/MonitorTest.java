package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static com.google.common.base.Preconditions.*;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import org.junit.*;

public class MonitorTest {

 final static Monitor monitor = new Monitor();
 final static Condition condition = monitor.newCondition();
 final static Guard guard = new Guard(monitor);

 @BeforeClass
 public static void setup() {
 checkState(monitor != null, "monitor should not be null");
 checkState(condition != null, "condition should not be null");
 checkState(guard != null, "guard should not be null");
 }

 @Test
 public void testIsFair() {
 Monitor monitor = new Monitor(true);
 Monitor monitor1 = new Monitor(false);
 assertTrue(monitor.isFair());
 assertFalse(monitor1.isFair());
 }

 @Test
 public void testIsOccupied() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enter();
 monitor.enter();
 int depth = monitor.getOccupiedDepth();
 assertEquals(3, depth);
 monitor.leave();
 monitor.leave();
 monitor.leave();
 depth = monitor.getOccupiedDepth();
 assertEquals(0, depth);
 }

 @Test
 public void testGetQueueLength() {
 Monitor monitor = new Monitor();
 int queueLength = monitor.getQueueLength();
 assertEquals(0, queueLength);
 }

 @Test
 public void testHasQueuedThreads() {
 Monitor monitor = new Monitor();
 boolean hasQueuedThreads = monitor.hasQueuedThreads(guard);
 assertFalse(hasQueuedThreads);
 monitor.enterWhen(guard);
 hasQueuedThreads = monitor.hasQueuedThreads(guard);
 assertTrue(hasQueuedThreads);
 monitor.leave();
 hasQueuedThreads = monitor.hasQueuedThreads(guard);
 assertFalse(hasQueuedThreads);
 }

 @Test
 public void testHasQueuedThread() {
 Monitor monitor = new Monitor();
 Thread thread = new Thread();
 boolean hasQueuedThread = monitor.hasQueuedThread(thread);
 assertFalse(hasQueuedThread);
 monitor.enterWhen(guard);
 hasQueuedThread = monitor.hasQueuedThread(thread);
 assertFalse(hasQueuedThread);
 monitor.enterWhenUninterruptibly(guard);
 hasQueuedThread = monitor.hasQueuedThread(thread);
 assertTrue(hasQueuedThread);
 monitor.leave();
 monitor.leave();
 hasQueuedThread = monitor.hasQueuedThread(thread);
 assertFalse(hasQueuedThread);
 }

 @Test
 public void testEnter() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithInvalidMonitor() {
 final Monitor monitor = new Monitor();
 final Monitor monitor1 = new Monitor();
 monitor.enter();
 try {
 monitor1.enter();
 }
 catch (IllegalMonitorStateException e) {
 fail("Expected IllegalMonitorStateException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread interruptingThread = new Thread(() -> monitor.interrupt());
 monitor.enterInterruptibly();
 interruptingThread.start();
 Thread.sleep(10);
 fail("Expected InterruptedException, but no exception was thrown.");
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenInvalidMonitor() throws InterruptedException {
 final Monitor monitor = new Monitor();
 final Monitor monitor1 = new Monitor();
 monitor.enterWhen(guard);
 try {
 monitor1.enter();
 monitor1.enterWhen(guard);
 monitor1.signalNextWaiter();
 monitor1.leave();
 }
 catch (IllegalMonitorStateException e) {
 fail("Expected IllegalMonitorStateException, but got: " + e.getMessage());
 }
 try {
 monitor.enterWhen(guard);
 fail("Expected IllegalMonitorStateException, but no exception was thrown.");
 }
 catch (IllegalMonitorStateException e) {
 // expected
 }
 }

 @Test
 public void testEnterWhenInterruptibly() throws InterruptedException {
 monitor.enterWhenInterruptibly(guard);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenInterruptiblyInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread interruptingThread = new Thread(() -> monitor.interrupt());
 monitor.enterWhenInterruptibly(guard);
 interruptingThread.start();
 Thread.sleep(10);
 fail("Expected InterruptedException, but no exception was thrown.");
 }

 @Test
 public void testEnterWhenWithTimeout() throws InterruptedException {
 long timeout = 1;
 boolean entered = monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 assertFalse(entered);
 Thread.sleep(timeout + 10);
 entered = monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 assertTrue(entered);
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeout() {
 long timeout = 1;
 monitor.enterWhenUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test
 public void testTryEnter() {
 Monitor monitor = new Monitor();
 boolean entered = monitor.tryEnter();
 assertFalse(entered);
 monitor.leave();
 entered = monitor.tryEnter();
 assertTrue(entered);
 monitor.leave();
 }

 @Test
 public void testTryEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread interruptingThread = new Thread(() -> monitor.interrupt());
 boolean entered = monitor.tryEnterInterruptibly();
 assertFalse(entered);
 monitor.leave();
 entered = monitor.tryEnterInterruptibly();
 assertTrue(entered);
 monitor.leave();
 }

 @Test
 public void testTryEnterIf() {
 Monitor monitor = new Monitor();
 boolean entered = monitor.tryEnterIf(guard);
 assertFalse(entered);
 monitor.leave();
 monitor.enterWhen(guard);
 entered = monitor.tryEnterIf(guard);
 assertTrue(entered);
 monitor.leave();
 }

 @Test
 public void testTryEnterIfInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread interruptingThread = new Thread(() -> monitor.interrupt());
 boolean entered = monitor.tryEnterIfInterruptibly(guard);
 assertFalse(entered);
 monitor.leave();
 monitor.enterWhenUninterruptibly(guard);
 entered = monitor.tryEnterIfInterruptibly(guard);
 assertTrue(entered);
 monitor.leave();
 }

 @Test
 public void testTryEnterIfWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 long timeout = 1;
 boolean entered = monitor.tryEnterIf(guard, timeout, TimeUnit.MILLISECONDS);
 assertFalse(entered);
 Thread.sleep(timeout + 10);
 entered = monitor.tryEnterIf(guard, timeout, TimeUnit.MILLISECONDS);
 assertTrue(entered);
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithGuardAndTimeout() throws InterruptedException {
 long timeout = 1;
 boolean entered = monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 assertFalse(entered);
 Thread.sleep(timeout + 10);
 entered = monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 assertTrue(entered);
 monitor.leave();
 }

 @Test
 public void testEnterWhenUninterruptiblyWithGuardAndTimeout() throws InterruptedException {
 long timeout = 1;
 monitor.enterWhenUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.waitFor(guard);
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptibly() throws InterruptedException {
 monitor.waitForUninterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testWaitForWithTimeout() throws InterruptedException {
 long timeout = 1;
 boolean waited = monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
 assertFalse(waited);
 Thread.sleep(timeout + 10);
 waited = monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
 assertTrue(waited);
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeout() throws InterruptedException {
 long timeout = 1;
 monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test
 public void testLeave() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 monitor.leave();
 }

 @Test
 public void testLeaveInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.leaveInterruptibly();
 monitor.enter();
 monitor.leaveInterruptibly();
 monitor.enter();
 monitor.leaveInterruptibly();
 monitor.enter();
 monitor.leave();
 }

 @Test
 public void testSignalNextWaiter() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterWhen(guard);
 monitor.signalNextWaiter();
 monitor.leave();
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test
 public void testSignalAllWaiters() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterWhen(guard);
 monitor.signalAllWaiters();
 monitor.leave();
 monitor.enterWhen(guard);
 monitor.leave();
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test
 public void testBeginWaitingFor() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterWhen(guard);
 monitor.beginWaitingFor(guard);
 monitor.leave();
 }

 @Test
 public void testEndWaitingFor() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterWhen(guard);
 monitor.beginWaitingFor(guard);
 monitor.endWaitingFor(guard);
 monitor.leave();
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test
 public void testAwait() throws InterruptedException {
 monitor.await(guard, true);
 monitor.leave();
 }

 @Test
 public void testAwaitUninterruptibly() throws InterruptedException {
 monitor.awaitUninterruptibly(guard, true);
 monitor.leave();
 }

 @Test
 public void testAwaitNanos() throws InterruptedException {
 long nanos = 1;
 boolean waited = monitor.awaitNanos(guard, nanos);
 assertFalse(waited);
 Thread.sleep(nanos + 10);
 waited = monitor.awaitNanos(guard, nanos);
 assertTrue(waited);
 monitor.leave();
 }

 @Test
 public void testAwaitNanosInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread interruptingThread = new Thread(() -> monitor.interrupt());
 long nanos = 1;
 boolean waited = monitor.awaitNanos(guard, nanos);
 assertFalse(waited);
 monitor.leave();
 interruptingThread.start();
 Thread.sleep(nanos + 10);
 waited = monitor.awaitNanos(guard, nanos);
 assertTrue(waited);
 monitor.leave();
 }

}