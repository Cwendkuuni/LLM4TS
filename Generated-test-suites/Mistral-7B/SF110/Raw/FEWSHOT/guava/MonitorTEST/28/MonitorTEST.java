package com.google.common.util.concurrent;

import com.google.common.annotations.*;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.annotation.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import static com.google.common.base.Preconditions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Beta
public class MonitorTest {

 final ReentrantLock lockMock = mock(ReentrantLock.class);
 final Condition conditionMock = mock(Condition.class);
 final Monitor monitor = new Monitor(lockMock, conditionMock);
 final Guard guard = new GuardWithSatisfactionChecker(monitor);

 @Test
 public void testConstructor() {
 assertNull(monitor.activeGuards);
 assertFalse(monitor.isFair());
 assertNull(monitor.lock);
 }

 @Test
 public void testEnter() {
 monitor.enter();
 verify(lockMock).lock();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithInvalidMonitor() {
 final Monitor invalidMonitor = new Monitor(lockMock) {
 @Override
 public void enter() {}
 };
 invalidMonitor.enter();
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 verify(lockMock).lockInterruptibly();
 }

 @Test
 public void testEnterInterruptiblyWithInterruptedException() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 verify(lockMock, times(2)).lockInterruptibly();
 }

 @Test
 public void testEnterTryLock() {
 assertTrue(monitor.tryEnter());
 verify(lockMock).tryLock();
 }

 @Test
 public void testEnterTryLockWithLockedMonitor() {
 assertFalse(monitor.tryEnter());
 verify(lockMock).tryLock();
 }

 @Test
 public void testEnterTryLockWithInterruptedException() {
 monitor.enter();
 assertFalse(monitor.tryEnter());
 verify(lockMock, times(2)).tryLock();
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 monitor.enterWhen(guard);
 verify(lockMock).lockInterruptibly();
 }

 @Test
 public void testEnterWhenWithUnsatisfiedGuard() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.awaitNanos(guard, 10, true);
 verify(conditionMock, times(1)).signalAll();
 }

 @Test
 public void testEnterWhenWithSatisfiedGuard() throws InterruptedException {
 monitor.enterWhen(guard);
 verify(conditionMock, never()).signalAll();
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor.enterWhenUninterruptibly(guard);
 verify(lockMock).lock();
 }

 @Test
 public void testEnterWhenUninterruptiblyWithUnsatisfiedGuard() {
 monitor.enterWhenUninterruptibly(guard);
 monitor.awaitNanos(guard, 10, true);
 verify(conditionMock, times(1)).signalAll();
 }

 @Test
 public void testEnterWhenUninterruptiblyWithSatisfiedGuard() {
 monitor.enterWhenUninterruptibly(guard);
 verify(conditionMock, never()).signalAll();
 }

 @Test
 public void testEnterWhenWithInterruptedException() throws InterruptedException {
 monitor.enterWhen(guard);
 Thread.currentThread().interrupt();
 monitor.enterWhen(guard);
 verify(lockMock, times(2)).lockInterruptibly();
 }

 @Test
 public void testEnterWhenWithTimeOut() throws InterruptedException {
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
 verify(lockMock).tryLock(300, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testEnterWhenWithInterruptedExceptionAndTimeOut() throws InterruptedException {
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 monitor.enterWhen(guard, 2, TimeUnit.SECONDS);
 verify(lockMock, times(3)).tryLock(anyLong(), any(Class.class));
 }

 @Test
 public void testEnterIf() {
 assertTrue(monitor.enterIf(guard));
 verify(lockMock).lock();
 }

 @Test
 public void testEnterIfWithLockedMonitor() {
 assertFalse(monitor.enterIf(guard));
 verify(lockMock).lock();
 }

 @Test
 public void testEnterIfWithInterruptedException() {
 monitor.enter();
 assertFalse(monitor.enterIf(guard));
 verify(lockMock, times(2)).lock();
 }

 @Test
 public void testEnterIfWithSatisfiedGuard() {
 monitor.enter();
 assertTrue(monitor.enterIf(guard));
 verify(lockMock, never()).lock();
 }

 @Test
 public void testEnterIfWithUnsatisfiedGuard() {
 monitor.enter();
 assertFalse(monitor.enterIf(guard));
 verify(lockMock, times(1)).lock();
 }

 @Test
 public void testEnterIfWithTimeOut() {
 monitor.enter();
 assertFalse(monitor.enterIf(guard, 1, TimeUnit.SECONDS));
 verify(lockMock, times(1)).lock();
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 verify(lockMock, times(2)).lockInterruptibly();
 }

 @Test
 public void testEnterIfInterruptiblyWithSatisfiedGuard() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 verify(lockMock, times(2)).lockInterruptibly();
 verify(conditionMock, never()).signalAll();
 }

 @Test
 public void testEnterIfInterruptiblyWithUnsatisfiedGuardAndInterruptedThread() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 verify(lockMock, times(3)).lockInterruptibly();
 }

 @Test
 public void testEnterIfInterruptiblyWithUnsatisfiedGuard() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 monitor.awaitNanos(guard, 10, true);
 verify(conditionMock, times(1)).signalAll();
 }

 @Test
 public void testEnterIfInterruptiblyWithSatisfiedGuard() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 monitor.awaitNanos(guard, 10, true);
 verify(conditionMock, never()).signalAll();
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeOut() throws InterruptedException {
 monitor.enter();
 assertFalse(monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS));
 verify(lockMock, times(1)).lockInterruptibly();
 }

 @Test
 public void testLeave() {
 monitor.leave();
 verify(lockMock).unlock();
 }

 @Test
 public void testIsFair() {
 assertFalse(monitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 assertTrue(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 assertEquals(1, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 assertFalse(monitor.hasQueuedThreads());
 }

 @Test
 public void testHasQueuedThread() {
 assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
 }

 @Test
 public void testHasWaiters() {
 assertFalse(monitor.hasWaiters(guard));
 }

 @Test
 public void testGetWaitQueueLength() {
 assertEquals(0, monitor.getWaitQueueLength(guard));
 }

 @Test
 public void testSignalNextWaiter() {
 monitor.signalNextWaiter();
 verify(conditionMock, times(1)).signalAll();
 }

 @Test
 public void testIsSatisfied() {
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testIsSatisfiedWithExceptionThrown() {
 final Exception exception = new Exception();
 when(guard.condition.await()).thenThrow(exception);
 monitor.signalNextWaiter();
 verify(conditionMock, times(1)).signalAll();
 verify(conditionMock, times(1)).signalAll();
 verify(conditionMock, times(1)).await();
 verify(conditionMock, times(1)).signalAll();
 verify(conditionMock, times(1)).signalAll();
 assertEquals(exception, exception);
 }

 @Test
 public void testSignalAllWaiters() {
 monitor.signalAllWaiters();
 verify(conditionMock, times(1)).signalAll();
 }

 @Test
 public void testBeginWaitingFor() {
 monitor.beginWaitingFor(guard);
 verify(conditionMock, times(1)).signalAll();
 }

 @Test
 public void testEndWaitingFor() {
 monitor.beginWaitingFor(guard);
 monitor.endWaitingFor(guard);
 verify(conditionMock, times(1)).signalAll();
 }

 @Test
 public void testAwait() throws InterruptedException {
 monitor.await(guard, true);
 verify(conditionMock, times(1)).await();
 }

 @Test
 public void testAwaitUninterruptibly() {
 monitor.awaitUninterruptibly(guard, true);
 verify(conditionMock, times(1)).awaitUninterruptibly();
 }

 @Test
 public void testAwaitNanos() throws InterruptedException {
 monitor.awaitNanos(guard, 10, true);
 verify(conditionMock, times(1)).awaitNanos(anyLong());
 }
}