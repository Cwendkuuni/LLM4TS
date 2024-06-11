package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class MonitorTest {

 Monitor monitor;
 ReentrantLock lock;
 Condition condition;

 @Before
 public void setUp() {
 monitor = new Monitor();
 lock = monitor.lock;
 condition = monitor.new Condition();
 }

 @Test
 public void testIsFair() {
 Monitor monitor1 = new Monitor(true);
 Monitor monitor2 = new Monitor(false);
 assertTrue(monitor1.isFair());
 assertFalse(monitor2.isFair());
 }

 @Test
 public void testIsOccupied() {
 lock.lock();
 assertTrue(monitor.isOccupied());
 lock.unlock();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 lock.lock();
 assertTrue(monitor.isOccupiedByCurrentThread());
 lock.unlock();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 for (int i = 0; i < 10; i++) {
 lock.lock();
 }
 int depth = monitor.getOccupiedDepth();
 assertEquals(10, depth);
 }

 @Test
 public void testGetQueueLength() {
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 Lock lock = new ReentrantLock();
 Monitor monitor = new Monitor();
 Condition condition = monitor.new Condition();
 Guard guard = monitor.new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhen(guard);
 assertFalse(monitor.hasQueuedThreads(guard));
 }

 @Test
 public void testHasQueuedThread() {
 Lock lock = new ReentrantLock();
 Monitor monitor = new Monitor();
 Condition condition = monitor.new Condition();
 Guard guard1 = monitor.new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 Guard guard2 = monitor.new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhen(guard1);
 monitor.enterWhen(guard2);
 assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
 assertFalse(monitor.hasQueuedThread(guard1.monitor));
 assertFalse(monitor.hasQueuedThread(guard2.monitor));
 }

 @Test
 public void testEnter() {
 lock.lock();
 monitor.enter();
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 monitor.enterInterruptibly();
 assertFalse(lock.tryLock());
 }

 @Test
 public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lockInterruptibly();
 monitor.enterInterruptibly(1, TimeUnit.SECONDS);
 assertFalse(lock.tryLock(1, TimeUnit.SECONDS));
 }

 @Test
 public void testEnterWithTimeoutSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lock();
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 }, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test
 public void testEnterWithTimeoutFailure() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lock();
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 }, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 lock.unlock();
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterIf() {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 assertFalse(monitor.enterIf(guard));
 lock.unlock();
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 monitor.enterIfInterruptibly(new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 });
 assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterIfWithTimeoutSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lock();
 monitor.enterIf(new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 }, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test
 public void testEnterIfWithTimeoutFailure() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lock();
 monitor.enterIf(new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 }, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterIf(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterIfWithTimeoutFailureInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lock();
 monitor.enterIfInterruptibly(new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 });
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterIfInterruptibly(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lock();
 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 assertTrue(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutFailure() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lock();
 monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhen(guard);
 assertFalse(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhenUninterruptibly(guard);
 assertFalse(lock.isHeldByCurrentThread());
 lock.unlock();
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeoutSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 lock.unlock();
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeoutFailure() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterWhenWithTimeoutSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterWhenWithTimeoutFailure() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterIfWithTimeoutSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterIfWithTimeoutFailure() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutFailure() throws InterruptedException {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 Condition condition = monitor.new Condition();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(lock.isHeldByCurrentThread());
 monitor.enterWhen(new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertFalse(lock.isLocked());
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testLeave() {
 Monitor monitor = new Monitor();
 lock = monitor.lock;
 lock.lock();
 monitor.leave();
 assertFalse(lock.isLocked());
 assertFalse(lock.isHeldByCurrentThread());
 }

 @Test
 public void testSignalNextWaiter() {
 Monitor monitor = new Monitor();
 Lock lock = new ReentrantLock();
 Condition condition = monitor.new Condition();
 Guard guard1 = monitor.new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 Guard guard2 = monitor.new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhen(guard1);
 monitor.signalNextWaiter();
 monitor.enterWhen(guard2);
 assertFalse(lock.isHeldByCurrentThread());
 assertTrue(guard2.isSatisfied());
 }

 @Test
 public void testIsSatisfied() {
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 assertFalse(guard.isSatisfied());
 }

 @Test
 public void testSignalAllWaiters() {
 Monitor monitor = new Monitor();
 Lock lock = new ReentrantLock();
 Condition condition = monitor.new Condition();
 Guard guard1 = monitor.new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 Guard guard2 = monitor.new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 lock.lock();
 monitor.enterWhen(guard1);
 monitor.enterWhen(guard2);
 monitor.signalAllWaiters();
 assertTrue(guard1.isSatisfied());
 assertTrue(guard2.isSatisfied());
 }

}