package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class MonitorTest {

 private Monitor monitor;
 @Mock
 private Condition condition;
 @Mock
 private Guard guard;

 @Before
 public void setUp() {
 monitor = new Monitor();
 Mockito.when(guard.monitor).thenReturn(monitor);
 Mockito.when(guard.condition).thenReturn(condition);
 }

 @Test
 public void testEnterAndLeave() {
 monitor.enter();
 monitor.leave();
 assertTrue(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyAndLeave() {
 monitor.enterInterruptibly();
 monitor.leave();
 assertTrue(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWithGuardAndLeave() {
 monitor.enterWhen(guard);
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithGuardAndLeave() throws InterruptedException {
 monitor.enterWhenInterruptibly(guard);
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfAndLeave() {
 Guard guard = Mockito.mock(Guard.class);
 Monitor monitor = new Monitor();
 monitor.enterIf(guard);
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptiblyAndLeave() throws InterruptedException {
 Guard guard = Mockito.mock(Guard.class);
 Monitor monitor = new Monitor();
 monitor.enterIfInterruptibly(guard);
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterAndLeave() {
 assertTrue(monitor.tryEnter());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterIfAndLeave() {
 Guard guard = Mockito.mock(Guard.class);
 assertTrue(monitor.tryEnterIf(guard));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.waitFor(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.enterWhen(guard);
 monitor.waitForUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForWithTimeout() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.waitFor(guard, 1, TimeUnit.SECONDS);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeout() {
 monitor.enterWhen(guard);
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.SECONDS);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForInterrupted() throws InterruptedException {
 monitor.enterWhen(guard);
 Thread.currentThread().interrupt();
 monitor.waitFor(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForUninterruptiblyInterrupted() {
 monitor.enterWhen(guard);
 Thread.currentThread().interrupt();
 monitor.waitForUninterruptibly(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testSignalNextWaiter() {
 monitor.enterWhen(guard);
 monitor.signalNextWaiter();
 Mockito.verify(condition).signal();
 }

 @Test
 public void testIsFair() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor(true);
 assertFalse(monitor1.isFair());
 assertTrue(monitor2.isFair());
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
 monitor.enterWhen(guard);
 monitor.enterWhen(guard);
 assertEquals(2, monitor.getQueueLength());
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enterWhen(guard);
 monitor.enterWhen(guard);
 assertTrue(monitor.hasQueuedThreads(guard));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads(guard));
 }

 @Test
 public void testHasQueuedThread() {
 monitor.enterWhen(guard);
 Thread thread = new Thread();
 monitor.enterWhen(guard);
 assertTrue(monitor.hasQueuedThread(thread));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThread(thread));
 }
}