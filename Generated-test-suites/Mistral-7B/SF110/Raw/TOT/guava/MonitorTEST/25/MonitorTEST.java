package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

 private Monitor monitor;
 private ConditionVariable cv;

 @Before
 public void setUp() {
 monitor = new Monitor();
 cv = new ConditionVariable();
 }

 @Test
 public void testEnter() {
 ReentrantLock lock = monitor.lock;
 Assert.assertFalse(lock.isHeldByCurrentThread());
 monitor.enter();
 Assert.assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 Assert.assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 ReentrantLock lock = monitor.lock;
 monitor.enterInterruptibly();
 Assert.assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyInterrupted() throws InterruptedException {
 Thread interrupter = new Thread(() -> Thread.currentThread().interrupt());
 interrupter.start();
 ReentrantLock lock = monitor.lock;
 try {
 monitor.enterInterruptibly();
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 interrupter.join();
 }
 }

 @Test
 public void testEnterWithGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enterWhen(guard);
 Assert.assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithInvalidGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(monitor);
 ReentrantLock lock = monitor.lock;
 try {
 monitor.enterWhen(guard);
 fail("IllegalMonitorStateException expected");
 } catch (IllegalMonitorStateException e) {
 }
 }

 @Test
 public void testEnterInterruptiblyWithGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enterWhenInterruptibly(guard);
 Assert.assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyWithInvalidGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(monitor);
 ReentrantLock lock = monitor.lock;
 try {
 monitor.enterWhenInterruptibly(guard);
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 }
 }

 @Test
 public void testTryEnter() {
 ReentrantLock lock = monitor.lock;
 Assert.assertFalse(lock.tryLock());
 }

 @Test
 public void testTryEnterWithGuard() {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 Assert.assertTrue(monitor.tryEnterWhen(guard));
 Assert.assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testTryEnterWithInvalidGuard() {
 final Guard guard = new GuardWithValue(monitor);
 ReentrantLock lock = monitor.lock;
 try {
 monitor.tryEnterWhen(guard);
 fail("IllegalMonitorStateException expected");
 } catch (IllegalMonitorStateException e) {
 }
 }

 @Test
 public void testTryEnterWithTimeout() {
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 Assert.assertFalse(monitor.tryEnterWhen(timeoutNanos, TimeUnit.NANOSECONDS));
 }

 @Test
 public void testTryEnterWithTimeoutAndGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 Assert.assertTrue(monitor.tryEnterWhen(guard, 1L, TimeUnit.NANOSECONDS));
 Assert.assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterWithTimeoutAndInterrupted() throws InterruptedException {
 Thread interrupter = new Thread(() -> Thread.currentThread().interrupt());
 interrupter.start();
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.tryEnterWhen(timeoutNanos, TimeUnit.NANOSECONDS);
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 interrupter.join();
 }
 }

 @Test
 public void testEnterWithTimeout() throws InterruptedException {
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.enter((long) 1, TimeUnit.MILLISECONDS);
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 }
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithTimeoutAndInterrupted() throws InterruptedException {
 Thread interrupter = new Thread(() -> Thread.currentThread().interrupt());
 interrupter.start();
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.enter((long) 1, TimeUnit.MILLISECONDS);
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 interrupter.join();
 }
 }

 @Test
 public void testEnterWithTimeoutAndGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.enterWhen(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 Assert.assertTrue(lock.isHeldByCurrentThread());
 } catch (InterruptedException e) {
 fail("Unexpected InterruptedException");
 } finally {
 lock.unlock();
 }
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithTimeoutAndGuardInterrupted() throws InterruptedException {
 Thread interrupter = new Thread(() -> Thread.currentThread().interrupt());
 interrupter.start();
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.enterWhen(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 interrupter.join();
 }
 }

 @Test
 public void testEnterUninterruptibly() {
 ReentrantLock lock = monitor.lock;
 monitor.enterUninterruptibly();
 Assert.assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test
 public void testEnterUninterruptiblyWithGuard() {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enterWhenUninterruptibly(guard);
 Assert.assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test
 public void testEnterUninterruptiblyWithTimeout() {
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.enterUninterruptibly((long) 1, TimeUnit.MILLISECONDS);
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 }
 }

 @Test(expected = InterruptedException.class)
 public void testEnterUninterruptiblyWithTimeoutAndInterrupted() {
 Thread interrupter = new Thread(() -> Thread.currentThread().interrupt());
 interrupter.start();
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.enterUninterruptibly((long) 1, TimeUnit.MILLISECONDS);
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 interrupter.join();
 }
 }

 @Test
 public void testEnterUninterruptiblyWithTimeoutAndGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.enterWhenUninterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 Assert.assertTrue(lock.isHeldByCurrentThread());
 } catch (InterruptedException e) {
 fail("Unexpected InterruptedException");
 } finally {
 lock.unlock();
 }
 }

 @Test(expected = InterruptedException.class)
 public void testEnterUninterruptiblyWithTimeoutAndGuardInterrupted() throws InterruptedException {
 Thread interrupter = new Thread(() -> Thread.currentThread().interrupt());
 interrupter.start();
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 long timeoutNanos = 1L;
 try {
 monitor.enterWhenUninterruptibly(guard, timeoutNanos, TimeUnit.NANOSECONDS);
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 interrupter.join();
 }
 }

 @Test
 public void testAwait() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enter();
 cv.open();
 monitor.leave();
 lock.lock();
 try {
 monitor.await(guard, true);
 Assert.assertTrue(cv.isSignalled());
 } finally {
 lock.unlock();
 }
 }

 @Test(expected = InterruptedException.class)
 public void testAwaitInterrupted() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enter();
 cv.open();
 monitor.leave();
 lock.lock();
 try {
 monitor.await(guard, false);
 fail("InterruptedException expected");
 } finally {
 lock.unlock();
 }
 }

 @Test(expected = InterruptedException.class)
 public void testAwaitInterruptedWithGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enter();
 cv.open();
 monitor.leave();
 lock.lock();
 try {
 monitor.await(guard, false);
 fail("InterruptedException expected");
 } finally {
 lock.unlock();
 }
 }

 @Test
 public void testAwaitNanos() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enter();
 cv.open();
 monitor.leave();
 lock.lock();
 long timeoutNanos = 1L;
 try {
 monitor.awaitNanos(guard, timeoutNanos, true);
 Assert.assertTrue(cv.isSignalled());
 } finally {
 lock.unlock();
 }
 }

 @Test(expected = InterruptedException.class)
 public void testAwaitNanosInterrupted() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enter();
 cv.open();
 monitor.leave();
 lock.lock();
 long timeoutNanos = 1L;
 try {
 monitor.awaitNanos(guard, timeoutNanos, false);
 fail("InterruptedException expected");
 } finally {
 lock.unlock();
 }
 }

 @Test(expected = InterruptedException.class)
 public void testAwaitNanosInterruptedWithGuard() throws InterruptedException {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enter();
 cv.open();
 monitor.leave();
 lock.lock();
 long timeoutNanos = 1L;
 try {
 monitor.awaitNanos(guard, timeoutNanos, false);
 fail("InterruptedException expected");
 } finally {
 lock.unlock();
 }
 }

 @Test
 public void testIsFair() {
 Monitor monitor1 = new Monitor(false);
 Monitor monitor2 = new Monitor(true);
 Assert.assertFalse(monitor1.isFair());
 Assert.assertTrue(monitor2.isFair());
 }

 @Test
 public void testIsOccupied() {
 ReentrantLock lock = monitor.lock;
 Assert.assertFalse(monitor.isOccupied());
 monitor.enter();
 Assert.assertTrue(monitor.isOccupied());
 monitor.leave();
 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 ReentrantLock lock = monitor.lock;
 Assert.assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 Assert.assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 Assert.assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 ReentrantLock lock = monitor.lock;
 Assert.assertEquals(0, monitor.getOccupiedDepth());
 monitor.enter();
 monitor.enter();
 Assert.assertEquals(2, monitor.getOccupiedDepth());
 monitor.leave();
 Assert.assertEquals(1, monitor.getOccupiedDepth());
 monitor.leave();
 Assert.assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 ReentrantLock lock = monitor.lock;
 Assert.assertEquals(0, monitor.getQueueLength());
 Thread thread1 = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 fail("Unexpected InterruptedException");
 }
 });
 thread1.start();
 Thread thread2 = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 fail("Unexpected InterruptedException");
 }
 });
 thread2.start();
 Assert.assertEquals(2, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 ReentrantLock lock = monitor.lock;
 Assert.assertFalse(monitor.hasQueuedThreads());
 Thread thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 fail("Unexpected InterruptedException");
 }
 });
 thread.start();
 Assert.assertTrue(monitor.hasQueuedThreads());
 }

 @Test
 public void testHasQueuedThread() {
 ReentrantLock lock = monitor.lock;
 Thread thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 fail("Unexpected InterruptedException");
 }
 });
 thread.start();
 Assert.assertTrue(monitor.hasQueuedThread(thread));
 }

 @Test
 public void testHasWaiters() {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enterWhen(guard);
 Assert.assertTrue(monitor.hasWaiters(guard));
 monitor.leave();
 Assert.assertFalse(monitor.hasWaiters(guard));
 }

 @Test
 public void testGetWaitQueueLength() {
 final Guard guard = new GuardWithValue(true);
 ReentrantLock lock = monitor.lock;
 monitor.enterWhen(guard);
 Assert.assertEquals(1, monitor.getWaitQueueLength(guard));
 monitor.leave();
 Assert.assertEquals(0, monitor.getWaitQueueLength(guard));
 }

 private static class GuardWithValue implements Guard {
 private final boolean value;
 private final Monitor monitor;

 public GuardWithValue(boolean value) {
 this.value = value;
 this.monitor = null;
 }

 public GuardWithValue(Monitor monitor) {
 this.value = false;
 this.monitor = Preconditions.checkNotNull(monitor, (Object)"monitor");
 }

 @Override
 public boolean isSatisfied() {
 return value;
 }
 }
}