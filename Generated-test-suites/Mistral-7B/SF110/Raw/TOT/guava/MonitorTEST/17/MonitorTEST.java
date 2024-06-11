package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MonitorTest {

 private Monitor monitor;
 @Mock
 private Condition condition;
 @Mock
 private ReentrantLock reentrantLock;

 @Before
 public void setUp() {
 monitor = new Monitor(reentrantLock);
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertFalse(reentrantLock.isLocked());
 assertTrue(monitor.isOccupied());
 }

 @Test
 public void testEnterInterruptibly_interrupted() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 assertFalse(reentrantLock.isLocked());
 assertTrue(monitor.isOccupied());
 assertTrue(Thread.currentThread().isInterrupted());
 }

 @Test
 public void testEnterInterruptibly_notInterrupted() throws InterruptedException {
 monitor.enterInterruptibly();
 assertFalse(reentrantLock.isLocked());
 assertTrue(monitor.isOccupied());
 assertFalse(Thread.currentThread().isInterrupted());
 }

 @Test
 public void testEnter_timeOut() throws InterruptedException {
 monitor.enter(1, TimeUnit.MILLISECONDS);
 assertFalse(reentrantLock.isLocked());
 assertTrue(monitor.isOccupied());
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptibly_timeOut() throws InterruptedException {
 ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
 monitor.enterInterruptibly(timeCaptor.capture(1L), TimeUnit.MILLISECONDS);
 assertFalse(reentrantLock.isLocked());
 assertTrue(monitor.isOccupied());
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertTrue(Thread.currentThread().isInterrupted());
 }

 @Test
 public void testEnter_timeOut_fair() throws InterruptedException {
 Monitor monitorFair = new Monitor(true, reentrantLock);
 monitorFair.enter();
 monitor.enter();
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitorFair.isOccupied());
 assertTrue(monitor.isOccupied());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhen_illegalMonitorStateException_differentMonitor() throws InterruptedException {
 Monitor monitorDifferent = new Monitor(reentrantLock);
 monitor.enterWhen(new Guard() {}, true);
 monitorDifferent.enter();
 monitor.enterWhen(new Guard() {}, true);
 fail("Expected IllegalMonitorStateException");
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor.enterWhenUninterruptibly(new Guard() {});
 assertFalse(reentrantLock.isLocked());
 assertTrue(monitor.isOccupied());
 }

 @Test
 public void testEnterWhen_satisfied() throws InterruptedException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 monitor.enterWhen(guard, true);
 assertFalse(reentrantLock.isLocked());
 assertTrue(monitor.isOccupied());
 assertFalse(guard.isSatisfied());
 }

 @Test
 public void testEnterWhen_notSatisfied() throws InterruptedException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.enterWhen(guard, true);
 monitor.waitFor(guard);
 verify(guard).isSatisfied();
 }

 @Test
 public void testEnterWhen_timeout() throws InterruptedException {
 Guard guard = mock(Guard.class);
 long timeout = 1L;
 when(guard.isSatisfied()).thenReturn(false);
 monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIf() {
 monitor.enterIf(new Guard() {});
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIf_satisfied() {
 Guard guard = new Guard() {
 @Override public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterIf(guard);
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIf_notSatisfied() {
 Guard guard = new Guard() {
 @Override public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIf(guard);
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIf_timeout() {
 Guard guard = new Guard() {
 @Override public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 monitor.enterIfInterruptibly(new Guard() {});
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIfInterruptibly_satisfied() throws InterruptedException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 monitor.enterIfInterruptibly(guard);
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 assertFalse(guard.isSatisfied());
 }

 @Test
 public void testEnterIfInterruptibly_timeout() throws InterruptedException {
 Guard guard = mock(Guard.class);
 when(guard.isSatisfied()).thenReturn(false);
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIfInterruptibly_timeout_interrupted() throws InterruptedException {
 Guard guard = mock(Guard.class);
 monitor.enterIfInterruptibly(guard);
 Thread.currentThread().interrupt();
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 assertTrue(Thread.currentThread().isInterrupted());
 }

 @Test
 public void testEnterIf_timeout_interrupted() throws InterruptedException {
 Guard guard = mock(Guard.class);
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 assertTrue(Thread.currentThread().isInterrupted());
 }

 @Test
 public void testEnterWhen_timeout_interrupted() throws InterruptedException {
 Guard guard = mock(Guard.class);
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 assertTrue(Thread.currentThread().isInterrupted());
 }

 @Test
 public void testEnterWhenUninterruptibly_timeout_interrupted() throws InterruptedException {
 Guard guard = mock(Guard.class);
 monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 assertTrue(Thread.currentThread().isInterrupted());
 }

 @Test
 public void testEnterIf_timeout_timeOut() throws InterruptedException {
 Guard guard = new Guard() {
 @Override public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIf(guard, 0, TimeUnit.MILLISECONDS);
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIfInterruptibly_timeout_timeOut() throws InterruptedException {
 Guard guard = new Guard() {
 @Override public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIfInterruptibly(guard, 0, TimeUnit.MILLISECONDS);
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhen_timeout_timeOut_interrupted() throws InterruptedException {
 Guard guard = new Guard() {
 @Override public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard, 0, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 assertTrue(Thread.currentThread().isInterrupted());
 }

 @Test
 public void testEnterWhenUninterruptibly_timeout_timeOut_interrupted() throws InterruptedException {
 Guard guard = new Guard() {
 @Override public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhenUninterruptibly(guard, 0, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 assertFalse(reentrantLock.isLocked());
 assertFalse(monitor.isOccupied());
 assertTrue(Thread.currentThread().isInterrupted());
 }
}