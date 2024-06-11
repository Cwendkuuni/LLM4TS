package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

 private Monitor monitor;
 private ReentrantLock mockLock;
 private Condition mockCondition;
 private Guard mockGuard;

 @Before
 public void setUp() {
 monitor = new Monitor();
 mockLock = mock(ReentrantLock.class);
 mockCondition = mock(Condition.class);
 mockGuard = new GuardWithSatisfiedCondition(monitor);
 when(mockLock.newCondition()).thenReturn(mockCondition);
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertTrue(mockLock.tryLock());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnter_incorrectMonitor() {
 monitor = new Monitor();
 Monitor anotherMonitor = new Monitor();
 monitor.enter();
 anotherMonitor.enter();
 anotherMonitor.enterInterruptibly(); // this should throw an IllegalMonitorStateException
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 assertTrue(mockLock.tryLockInterruptibly(500, TimeUnit.MILLISECONDS));
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptibly_interrupted() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 mockLock.lockInterruptibly(); // this should throw an InterruptedException
 }

 @Test
 public void testEnter_longTimeout() throws TimeoutException, InterruptedException {
 monitor.enter(1, TimeUnit.SECONDS);
 assertTrue(mockLock.tryLock(1, TimeUnit.SECONDS));
 }

 @Test(expected = InterruptedException.class)
 public void testEnter_longTimeout_interrupted() throws TimeoutException, InterruptedException {
 monitor.enter(1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 mockLock.tryLock(1, TimeUnit.SECONDS); // this should throw an InterruptedException
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptibly_longTimeout_interrupted() throws TimeoutException, InterruptedException {
 monitor.enterInterruptibly(1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 mockLock.tryLock(1, TimeUnit.SECONDS); // this should throw an InterruptedException
 }

 @Test
 public void testTryEnter() {
 assertTrue(monitor.tryEnter());
 assertTrue(mockLock.tryLock());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testTryEnter_incorrectMonitor() {
 Monitor anotherMonitor = new Monitor();
 assertTrue(monitor.tryEnter());
 anotherMonitor.enter();
 anotherMonitor.enterInterruptibly(); // this should throw an IllegalMonitorStateException
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 monitor.enterWhen(mockGuard);
 monitor.lock.lockInterruptibly();
 mockGuard.condition.signalAll();
 monitor.leave();
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor.enterWhenUninterruptibly(mockGuard);
 monitor.lock.lock();
 mockGuard.condition.signalAll();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhen_incorrectMonitor() {
 monitor.enterWhen(mockGuard);
 Monitor anotherMonitor = new Monitor();
 anotherMonitor.enter();
 anotherMonitor.enterInterruptibly(); // this should throw an IllegalMonitorStateException
 }

 @Test
 public void testEnterWhen_longTimeout() throws InterruptedException, TimeoutException {
 long timeout = 1000L;
 monitor.enterWhen(mockGuard, timeout, TimeUnit.MILLISECONDS);
 monitor.lock.lockInterruptibly();
 monitor.lock.unlock();
 monitor.lock.lockInterruptibly();
 mockGuard.condition.signalAll();
 monitor.leave();
 assertTrue(mockLock.tryLock(timeout, TimeUnit.MILLISECONDS));
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhen_longTimeout_interrupted() throws InterruptedException, TimeoutException {
 long timeout = 1000L;
 monitor.enterWhen(mockGuard, timeout, TimeUnit.MILLISECONDS);
 monitor.lock.lockInterruptibly();
 Thread.currentThread().interrupt();
 mockLock.tryLock(timeout, TimeUnit.MILLISECONDS); // this should throw an InterruptedException
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenInterruptibly_longTimeout_interrupted() throws InterruptedException, TimeoutException {
 long timeout = 1000L;
 monitor.enterWhenInterruptibly(mockGuard, timeout, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 mockLock.tryLock(timeout, TimeUnit.MILLISECONDS); // this should throw an InterruptedException
 }

 @Test
 public void testEnterIf() {
 monitor.enterIf(mockGuard);
 assertTrue(mockLock.tryLock());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIf_incorrectMonitor() {
 Monitor anotherMonitor = new Monitor();
 assertTrue(monitor.enterIf(mockGuard));
 anotherMonitor.enter();
 anotherMonitor.enterInterruptibly(); // this should throw an IllegalMonitorStateException
 }

 @Test
 public void testEnterIf_longTimeout() throws TimeoutException, InterruptedException {
 Guard guard = new GuardWithSatisfiedCondition(monitor);
 monitor.enterIf(guard, 1, TimeUnit.SECONDS);
 monitor.lock.lockInterruptibly();
 monitor.lock.unlock();
 monitor.lock.lockInterruptibly();
 assertTrue(guard.isSatisfied());
 monitor.leave();
 assertTrue(mockLock.tryLock(1, TimeUnit.SECONDS));
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIf_longTimeout_interrupted() throws TimeoutException, InterruptedException {
 Guard guard = new GuardWithSatisfiedCondition(monitor);
 monitor.enterIf(guard, 1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 mockLock.tryLock(1, TimeUnit.SECONDS); // this should throw an InterruptedException
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 monitor.enterIfInterruptibly(mockGuard);
 monitor.lock.lockInterruptibly();
 monitor.leave();
 }

 @Test
 public void testEnterIfInterruptibly_longTimeout() throws InterruptedException, TimeoutException {
 monitor.enterIfInterruptibly(mockGuard, 1, TimeUnit.SECONDS);
 monitor.lock.lockInterruptibly();
 monitor.lock.unlock();
 monitor.lock.lockInterruptibly();
 assertTrue(mockLock.tryLock(1, TimeUnit.SECONDS));
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptibly_longTimeout_interrupted() throws InterruptedException, TimeoutException {
 monitor.enterIfInterruptibly(mockGuard, 1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 mockLock.tryLock(1, TimeUnit.SECONDS); // this should throw an InterruptedException
 }

 @Test
 public void testTryEnterIf() {
 monitor.tryEnterIf(mockGuard);
 assertTrue(mockLock.tryLock());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testTryEnterIf_incorrectMonitor() {
 Monitor anotherMonitor = new Monitor();
 assertTrue(monitor.tryEnterIf(mockGuard));
 anotherMonitor.enter();
 anotherMonitor.enterInterruptibly(); // this should throw an IllegalMonitorStateException
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.waitFor(mockGuard);
 monitor.lock.lockInterruptibly();
 monitor.lock.unlock();
 monitor.lock.lockInterruptibly();
 monitor.signalNextWaiter();
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.waitForUninterruptibly(mockGuard);
 monitor.lock.lock();
 monitor.lock.unlock();
 monitor.lock.lock();
 monitor.signalAllWaiters();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitFor_incorrectMonitor() {
 monitor.waitFor(mockGuard);
 Monitor anotherMonitor = new Monitor();
 anotherMonitor.enter();
 anotherMonitor.enterInterruptibly(); // this should throw an IllegalMonitorStateException
 }

 @Test
 public void testWaitFor_longTimeout() throws InterruptedException, TimeoutException {
 Guard guard = new GuardWithSatisfiedCondition(monitor);
 monitor.waitFor(guard, 1, TimeUnit.SECONDS);
 monitor.lock.lockInterruptibly();
 monitor.lock.unlock();
 monitor.lock.lockInterruptibly();
 monitor.signalNextWaiter();
 monitor.leave();
 assertTrue(mockLock.tryLock(1, TimeUnit.SECONDS));
 }

 @Test(expected = InterruptedException.class)
 public void testWaitFor_longTimeout_interrupted() throws InterruptedException, TimeoutException {
 Guard guard = new GuardWithSatisfiedCondition(monitor);
 monitor.waitFor(guard, 1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 mockLock.tryLock(1, TimeUnit.SECONDS); // this should throw an InterruptedException
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForInterruptibly_longTimeout_interrupted() throws InterruptedException, TimeoutException {
 Guard guard = new GuardWithSatisfiedCondition(monitor);
 monitor.waitForInterruptibly(guard, 1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 mockLock.tryLock(1, TimeUnit.SECONDS); // this should throw an InterruptedException
 }

 @Test
 public void testIsFair() {
 boolean fair = monitor.isFair();
 assertEquals(fair, monitor.isFair());
 }

 @Test
 public void testIsOccupied() {
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
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 assertEquals(2, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 monitor.enterWhen(mockGuard);
 monitor.enterWhenUninterruptibly(mockGuard);
 assertEquals(2, mockCondition.getQueueLength());
 monitor.leave();
 monitor.leave();
 assertEquals(0, mockCondition.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 assertEquals(false, monitor.hasQueuedThreads(mockGuard));
 monitor.enterWhen(mockGuard);
 monitor.enterWhenUninterruptibly(mockGuard);
 assertEquals(true, monitor.hasQueuedThreads(mockGuard));
 monitor.leave();
 monitor.leave();
 assertEquals(false, monitor.hasQueuedThreads(mockGuard));
 }
}

 class GuardWithSatisfiedCondition extends Guard {
 private boolean isSatisfied;

 GuardWithSatisfiedCondition(Monitor monitor) {
 super(monitor);
 this.isSatisfied = true;
 }

 @Override
 public boolean isSatisfied() {
 return isSatisfied;
 }
 }