package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MonitorTest {

 private Monitor monitor;
 @Mock
 private Condition condition;
 @Mock
 private ReentrantLock lock;
 @Captor
 private ArgumentCaptor<Guard> guardCaptor;

 @Before
 public void setup() {
 monitor = new Monitor(false);
 when(lock.newCondition()).thenReturn(condition);
 monitor.lock = lock;
 }

 @Test
 public void testEnterSuccess() throws InterruptedException {
 monitor.enter();
 verify(lock).lock();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyInterruptedFailure() throws InterruptedException {
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 verifyZeroInteractions(condition);
 }

 @Test
 public void testEnterInterruptiblySuccess() throws InterruptedException {
 monitor.enterInterruptibly();
 verifyZeroInteractions(condition);
 }

 @Test
 public void testTryEnterSuccess() {
 assertTrue(monitor.tryEnter());
 }

 @Test
 public void testTryEnterFailure() {
 assertFalse(monitor.tryEnter());
 }

 @Test
 public void testEnterWhenSuccess() throws InterruptedException {
 Guard guard = mock(Guard.class);
 monitor.enterWhen(guard);
 verify(lock).lockInterruptibly();
 }

 @Test
 public void testEnterWhenInterruptFailure() throws InterruptedException {
 Thread.currentThread().interrupt();
 Guard guard = mock(Guard.class);
 monitor.enterWhen(guard);
 verifyZeroInteractions(condition);
 }

 @Test
 public void testEnterWhenTimeoutFailure() throws InterruptedException {
 Long duration = 5L;
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.enterWhen(guard, duration, TimeUnit.SECONDS);
 verifyZeroInteractions(condition);
 }

 @Test
 public void testEnterWhenTimeoutSuccess() throws InterruptedException {
 Long duration = 5L;
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.enterWhen(guard, duration, TimeUnit.SECONDS);
 Thread.sleep(duration + 10);
 verify(condition).signal();
 }

 @Test
 public void testEnterIfSuccess() {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 assertTrue(monitor.enterIf(guard));
 }

 @Test
 public void testEnterIfFailure() {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 assertFalse(monitor.enterIf(guard));
 }

 @Test
 public void testEnterIfInterruptFailure() throws InterruptedException {
 Thread.currentThread().interrupt();
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 assertFalse(monitor.enterIfInterruptibly(guard));
 }

 @Test
 public void testEnterIfInterruptSuccess() throws InterruptedException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 assertTrue(monitor.enterIfInterruptibly(guard));
 }

 @Test
 public void testEnterIfTimeoutFailure() {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 assertFalse(monitor.enterIf(guard, 5, TimeUnit.SECONDS));
 }

 @Test
 public void testEnterIfTimeoutSuccess() throws InterruptedException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.enterIf(guard, 5, TimeUnit.SECONDS);
 Thread.sleep(5100);
 verify(condition).signal();
 }

 @Test
 public void testTryEnterIfSuccess() {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 assertTrue(monitor.tryEnterIf(guard));
 }

 @Test
 public void testTryEnterIfFailure() {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 assertFalse(monitor.tryEnterIf(guard));
 }

 @Test
 public void testWaitForSuccess() throws InterruptedException {
 Guard guard = mock(Guard.class);
 monitor.waitFor(guard);
 verify(condition).await();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForFailure() throws InterruptedException {
 Guard guard = mock(Guard.class);
 assertFalse(monitor.isHeldByCurrentThread());
 monitor.waitFor(guard);
 }

 @Test
 public void testWaitForUninterruptiblySuccess() {
 Guard guard = mock(Guard.class);
 monitor.waitForUninterruptibly(guard);
 verify(condition).awaitUninterruptibly();
 }

 @Test
 public void testWaitForUninterruptiblyInterruptFailure() throws InterruptedException {
 Thread.currentThread().interrupt();
 Guard guard = mock(Guard.class);
 monitor.waitForUninterruptibly(guard);
 verifyZeroInteractions(condition);
 }

 @Test
 public void testWaitForUninterruptiblyTimeoutFailure() {
 Long duration = 5L;
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.waitForUninterruptibly(guard, duration, TimeUnit.SECONDS);
 verifyZeroInteractions(condition);
 }

 @Test
 public void testWaitForUninterruptiblyTimeoutSuccess() throws InterruptedException {
 Long duration = 5L;
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.waitForUninterruptibly(guard, duration, TimeUnit.SECONDS);
 Thread.sleep(duration + 10);
 verify(condition).signalAll();
 }

 @Test
 public void testLeaveSuccess() {
 monitor.leave();
 verify(lock).unlock();
 }

 @Test
 public void testIsFairSuccess() {
 assertTrue(monitor.isFair());
 }

 @Test
 public void testIsFairFalse() {
 assertFalse(monitor.isFair(false));
 }

 @Test
 public void testIsOccupiedSuccess() {
 assertTrue(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThreadSuccess() {
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepthSuccess() {
 assertEquals(1, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLengthSuccess() {
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreadsSuccess() {
 assertFalse(monitor.hasQueuedThreads());
 }

 @Test
 public void testHasQueuedThreadSuccess() {
 Thread thread = mock(Thread.class);
 monitor.addQueue(thread);
 assertTrue(monitor.hasQueuedThread(thread));
 }

 @Test
 public void testHasWaitersSuccess() {
 Guard guard = mock(Guard.class);
 monitor.beginWaitingFor(guard);
 assertTrue(monitor.hasWaiters(guard));
 }
}