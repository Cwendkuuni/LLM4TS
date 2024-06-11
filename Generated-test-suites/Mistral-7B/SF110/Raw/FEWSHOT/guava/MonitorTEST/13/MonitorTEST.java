package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.annotations.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import com.google.common.base.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

@Beta
public class MonitorTest {

 private Monitor monitor;
 private Condition condition;
 private ReentrantLock lock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 monitor = new Monitor();
 condition = mock(Condition.class);
 lock = new ReentrantLock();
 monitor.lock = lock;
 }

 @Test
 public void testMonitorInitialization() {
 Monitor monitor = new Monitor();
 assertNull(monitor.activeGuards);
 assertFalse(monitor.isFair());
 assertNotNull(monitor.lock);
 }

 @Test
 public void testMonitorEnter() {
 monitor.enter();
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testMonitorEnterWithWrongMonitor() {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 otherMonitor.enter(); // should throw an exception
 }

 @Test
 public void testMonitorEnterInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorEnterInterruptiblyFailed() throws InterruptedException {
 monitor.enterInterruptibly();
 lock.lock(); // this thread shouldn't get the lock
 assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorEnterGuard() throws InterruptedException {
 monitor.enterWhen(any(Guard.class));
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testMonitorEnterGuardWithWrongMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enterWhen(any(Guard.class));
 otherMonitor.enter();
 otherMonitor.enter(); // should throw an exception
 }

 @Test
 public void testMonitorEnterGuardInterruptibly() throws InterruptedException {
 monitor.enterWhenInterruptibly(any(Guard.class));
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testMonitorEnterGuardInterruptiblyWithWrongMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enterWhenInterruptibly(any(Guard.class));
 otherMonitor.enter();
 otherMonitor.enter(); // should throw an exception
 }

 @Test
 public void testMonitorEnterGuardTimeout() throws InterruptedException, TimeoutException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorEnterGuardTimeoutInterruptively() throws InterruptedException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.enterWhenInterruptibly(guard, 10, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorEnterGuardTimeoutSuccess() throws InterruptedException, TimeoutException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
 assertTrue(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorEnterGuardTimeoutSuccessInterruptibly() throws InterruptedException, TimeoutException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 monitor.enterWhenInterruptibly(guard, 10, TimeUnit.MILLISECONDS);
 assertTrue(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorTryEnter() {
 monitor.tryEnter();
 assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorTryEnterSuccess() {
 monitor.tryEnter();
 lock.tryLock();
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorTryEnterFailed() {
 monitor.tryEnter();
 lock.lock();
 assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorTryEnterGuard() {
 monitor.tryEnterIf(any(Guard.class));
 assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorTryEnterGuardSuccess() {
 monitor.tryEnterIf(any(Guard.class));
 monitor.enterWhen(any(Guard.class));
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorTryEnterGuardFailed() {
 monitor.tryEnterIf(any(Guard.class));
 monitor.enterWhen(any(Guard.class));
 lock.lock();
 assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorTryEnterGuardTimeout() {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.tryEnterIf(guard);
 assertFalse(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorTryEnterGuardTimeoutSuccess() {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 monitor.tryEnterIf(guard);
 assertTrue(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorWaitFor() throws InterruptedException {
 monitor.waitFor(any(Guard.class));
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorWaitForUninterruptibly() {
 monitor.waitForUninterruptibly(any(Guard.class));
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorWaitForWithSatisfiedGuard() {
 monitor.waitFor(any(Guard.class));
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testMonitorWaitForInterrupted() throws InterruptedException {
 monitor.waitFor(any(Guard.class));
 Thread.currentThread().interrupt();
 }

 @Test(expected = InterruptedException.class)
 public void testMonitorWaitForInterruptiblyInterrupted() throws InterruptedException {
 monitor.waitForUninterruptibly(any(Guard.class));
 Thread.currentThread().interrupt();
 }

 @Test
 public void testMonitorWaitForTimeout() throws InterruptedException, TimeoutException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.waitFor(guard, 10, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorWaitForTimeoutInterruptibly() throws InterruptedException, TimeoutException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.waitForUninterruptibly(guard, 10, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorWaitForTimeoutSuccess() throws InterruptedException, TimeoutException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 monitor.waitFor(guard, 10, TimeUnit.MILLISECONDS);
 assertTrue(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorWaitForTimeoutSuccessInterruptibly() throws InterruptedException, TimeoutException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 monitor.waitForUninterruptibly(guard, 10, TimeUnit.MILLISECONDS);
 assertTrue(lock.isHeldByCurrentThread());
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testMonitorLeave() {
 monitor.leave();
 assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testMonitorIsFair() {
 Monitor fairMonitor = new Monitor(true);
 Monitor unfairMonitor = new Monitor(false);
 assertTrue(fairMonitor.isFair());
 assertFalse(unfairMonitor.isFair());
 }

 @Test
 public void testMonitorIsOccupied() {
 monitor.enter();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testMonitorIsOccupiedByCurrentThread() {
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testMonitorGetOccupiedDepth() {
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 assertEquals(2, monitor.getOccupiedDepth());
 }

 @Test
 public void testMonitorGetWaitQueueLength() {
 Monitor monitor = new Monitor();
 monitor.enter();
 Guard guard = new Guard(monitor);
 monitor.await(guard, true);
 assertEquals(1, monitor.getWaitQueueLength(guard));
 monitor.leave();
 assertEquals(0, monitor.getWaitQueueLength(guard));
 }

 @Test
 public void testMonitorHasQueuedThreads() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor);
 monitor.await(guard, true);
 assertTrue(monitor.hasQueuedThreads(guard));
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads(guard));
 }

 @Test
 public void testMonitorHasQueuedThread() {
 Monitor monitor = new Monitor();
 Thread thread = new Thread();
 monitor.enter();
 monitor.await(any(Guard.class), true);
 monitor.leave();
 assertTrue(monitor.hasQueuedThread(thread));
 }

 @Test
 public void testMonitorSignalNextWaiter() {
 Monitor monitor = new Monitor();
 Guard guard1 = new Guard(monitor);
 Guard guard2 = new Guard(monitor);
 monitor.enter();
 monitor.await(guard1, true);
 monitor.signalNextWaiter();
 monitor.await(guard2, true);
 monitor.leave();
 assertEquals(guard2, monitor.getActiveGuards());
 }
}