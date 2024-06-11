package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import com.google.common.base.*;
import com.google.common.util.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import org.junit.Test;

@Beta
public class MonitorTest {

 @Test
 public void testMonitorCreation() {
 Monitor monitor = new Monitor();
 Monitor fairMonitor = new Monitor(true);
 }

 @Test
 public void testEnter() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithNullLock() {
 Monitor monitor = new Monitor();
 ReentrantLock nullLock = null;
 monitor.enter(); // Should throw an exception
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 monitor.leave();
 }

 @Test
 public void testEnterInterruptiblyWithInterruption() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread thread = new Thread(() -> monitor.enterInterruptibly());
 monitor.enter();
 thread.start();
 thread.join();
 monitor.leave();
 }

 @Test
 public void testEnterWithTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 monitor.enter(5, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test(expected = TimeoutException.class)
 public void testEnterWithTimeoutAndTimeoutExpired() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 monitor.enter(0, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testEnterInterruptiblyWithTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly(5, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyWithInterruptionAndTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Thread thread = new Thread(() -> monitor.enterInterruptibly(5, TimeUnit.MILLISECONDS));
 monitor.enter();
 monitor.interrupt();
 thread.join();
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyWithInterruptionAndTimeoutExpired() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Thread thread = new Thread(() -> monitor.enterInterruptibly(0, TimeUnit.MILLISECONDS));
 monitor.enter();
 monitor.interrupt();
 thread.join();
 monitor.leave();
 }

 @Test
 public void testTryEnter() {
 Monitor monitor = new Monitor();
 assertTrue(monitor.tryEnter());
 monitor.leave();
 }

 @Test
 public void testTryEnterWithLockHeld() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertFalse(monitor.tryEnter());
 monitor.leave();
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithNullLock() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhen(guard);
 monitor.leave();
 monitor.enterWhen(guard); // Should throw an exception
 monitor.leave();
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhen(guard, 5, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test(expected = TimeoutException.class)
 public void testEnterWhenWithTimeoutExpired() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhen(guard, 0, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testEnterWhenInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhenInterruptibly(guard);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenInterruptiblyWithInterruption() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhenInterruptibly(guard);
 monitor.interrupt();
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenInterruptiblyWithInterruptionAndTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhenInterruptibly(guard, 5, TimeUnit.MILLISECONDS);
 monitor.interrupt();
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenInterruptiblyWithInterruptionAndTimeoutExpired() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhenInterruptibly(guard, 0, TimeUnit.MILLISECONDS);
 monitor.interrupt();
 monitor.leave();
 }

 @Test
 public void testTryEnterIf() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 assertTrue(monitor.tryEnterIf(guard));
 monitor.leave();
 }

 @Test
 public void testTryEnterIfWithGuardNotSatisfied() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 assertFalse(monitor.tryEnterIf(guard));
 monitor.leave();
 }

 @Test
 public void testTryEnterIfWithLockHeld() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 monitor.enter(); // Acquire second lock
 assertFalse(monitor.tryEnterIf(guard));
 monitor.leave();
 monitor.leave();
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitFor(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForWithNullLock() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitFor(guard);
 monitor.leave();
 monitor.waitFor(guard); // Should throw an exception
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptibly() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitForUninterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testWaitForWithTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitFor(guard, 5, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test(expected = TimeoutException.class)
 public void testWaitForWithTimeoutExpired() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitFor(guard, 0, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testWaitForInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitForInterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testWaitForInterruptiblyWithInterruption() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitForInterruptibly(guard);
 monitor.interrupt();
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForInterruptiblyWithInterruptionAndTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitForInterruptibly(guard, 5, TimeUnit.MILLISECONDS);
 monitor.interrupt();
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForInterruptiblyWithInterruptionAndTimeoutExpired() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitForInterruptibly(guard, 0, TimeUnit.MILLISECONDS);
 monitor.interrupt();
 monitor.leave();
 }

 @Test
 public void testLeave() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testLeaveWithNullLock() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 monitor.leave(); // Should throw an exception
 }

 @Test
 public void testIsFair() {
 Monitor monitor = new Monitor();
 assertTrue(monitor.isFair());
 Monitor fairMonitor = new Monitor(true);
 assertTrue(fairMonitor.isFair());
 Monitor unfairMonitor = new Monitor(false);
 assertFalse(unfairMonitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 monitor.enter();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 Monitor monitor = new Monitor();
 assertEquals(0, monitor.getOccupiedDepth());
 monitor.enter();
 assertEquals(1, monitor.getOccupiedDepth());
 monitor.leave();
 assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 Monitor monitor = new Monitor();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 Monitor monitor = new Monitor();
 assertTrue(monitor.hasQueuedThreads());
 monitor.enter();
 assertFalse(monitor.hasQueuedThreads());
 monitor.leave();
 }

 @Test
 public void testHasQueuedThread() {
 Monitor monitor = new Monitor();
 Thread thread = new Thread(() -> monitor.enter());
 assertTrue(monitor.hasQueuedThread(thread));
 monitor.leave();
 assertFalse(monitor.hasQueuedThread(thread));
 }

 @Test
 public void testHasWaiters() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.waitFor(guard);
 assertTrue(monitor.hasWaiters(guard));
 monitor.leave();
 assertFalse(monitor.hasWaiters(guard));
 }

}

class GuardMonitor extends Monitor.Guard {
 Monitor monitor;

 GuardMonitor() {
 this.monitor = new Monitor();
 }

 @Override
 public boolean isSatisfied() {
 return true;
 }
}