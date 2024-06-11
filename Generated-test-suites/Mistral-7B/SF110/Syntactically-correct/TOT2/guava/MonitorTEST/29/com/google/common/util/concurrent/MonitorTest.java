package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.util.concurrent.Monitor;
import org.junit.*;

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

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyInterrupted() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyInterruptedWhileLocked() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 }

 @Test
 public void testEnterWhenNotSatisfied() throws InterruptedException {
 Monitor.Guard guard = new Monitor.Guard(monitor, false);
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test
 public void testEnterWhenSatisfied() throws InterruptedException {
 Monitor.Guard guard = mock(Monitor.Guard.class);
 when(guard.isSatisfied()).thenReturn(true);
 monitor.enterWhen(guard);
 monitor.leave();
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 Monitor.Guard guard = spy(new Monitor.Guard(monitor, true));
 doReturn(true).when(guard).isSatisfied();
 monitor.enterIfInterruptibly(guard);
 monitor.leave();
 verify(guard, times(1)).isSatisfied();
 }

 @Test
 public void testEnterIfInterruptiblyTimedOut() throws InterruptedException {
 Monitor.Guard guard = spy(new Monitor.Guard(monitor, true));
 doReturn(false).when(guard).isSatisfied();
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
 verify(guard, never()).isSatisfied();
 }

 @Test
 public void testEnterIfInterruptiblyInterruptedWhileWaiting() throws InterruptedException {
 Monitor.Guard guard = spy(new Monitor.Guard(monitor, true));
 doReturn(false).when(guard).isSatisfied();
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.SECONDS);
 verify(guard, never()).isSatisfied();
 }

 @Test
 public void testEnterWhenWithCondition() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard = new Monitor.Guard(monitor, condition);
 monitor.enterWhen(guard);
 monitor.leave();
 verify(condition, times(1)).await();
 }

 @Test
 public void testEnterWhenWithConditionInterruptedWhileWaiting() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard = new Monitor.Guard(monitor, condition);
 monitor.enterWhen(guard);
 Thread.currentThread().interrupt();
 monitor.enterWhen(guard);
 verify(condition, times(1)).await();
 }

 @Test
 public void testEnterWhenWithConditionTimedOut() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard = new Monitor.Guard(monitor, condition);
 long timeout = 1;
 monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 verify(condition, never()).await();
 }

 @Test
 public void testEnterWhenWithConditionSignalAllWaiters() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard = new Monitor.Guard(monitor, condition);
 monitor.enterWhen(guard);
 monitor.signalAllWaiters();
 monitor.leave();
 verify(condition, times(1)).signalAll();
 }

 @Test
 public void testEnterWhenWithConditionSignalOneWaiter() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard1 = new Monitor.Guard(monitor, condition);
 Monitor.Guard guard2 = new Monitor.Guard(monitor, condition);
 monitor.enterWhen(guard1);
 monitor.signal();
 monitor.enterWhen(guard2);
 monitor.leave();
 verify(condition, times(1)).signal();
 }

 @Test
 public void testEnterIfWithCondition() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard = new Monitor.Guard(monitor, condition);
 monitor.enterIf(guard);
 monitor.leave();
 verify(condition, times(1)).isSatisfied();
 }

 @Test
 public void testEnterIfWithConditionInterruptedWhileWaiting() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard = new Monitor.Guard(monitor, condition);
 monitor.enterIf(guard);
 Thread.currentThread().interrupt();
 monitor.enterIf(guard);
 verify(condition, times(1)).isSatisfied();
 }

 @Test
 public void testEnterIfWithConditionTimedOut() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard = new Monitor.Guard(monitor, condition);
 long timeout = 1;
 monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS);
 verify(condition, never()).isSatisfied();
 }

 @Test
 public void testEnterIfWithConditionSignalAllWaiters() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard = new Monitor.Guard(monitor, condition);
 monitor.enterIf(guard);
 monitor.signalAllWaiters();
 monitor.leave();
 verify(condition, times(1)).signalAll();
 }

 @Test
 public void testEnterIfWithConditionSignalOneWaiter() throws InterruptedException {
 Condition condition = mock(Condition.class);
 Monitor.Guard guard1 = new Monitor.Guard(monitor, condition);
 Monitor.Guard guard2 = new Monitor.Guard(monitor, condition);
 monitor.enterIf(guard1);
 monitor.signal();
 monitor.enterIf(guard2);
 monitor.leave();
 verify(condition, times(1)).signal();
 }
}