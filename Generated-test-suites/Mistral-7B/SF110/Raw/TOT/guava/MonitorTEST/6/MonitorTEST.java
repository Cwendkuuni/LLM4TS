package com.google.common.util.concurrent;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

 @Test
 public void testEnter() {
 Monitor monitor = new Monitor();
 Thread thread1 = Thread.currentThread();
 Thread thread2 = new Thread(() -> {
 monitor.enter();
 checkState(monitor.isOccupiedByCurrentThread(), "Thread 2 should have acquired the lock");
 });
 thread2.start();
 monitor.enter();
 checkState(monitor.isOccupiedByCurrentThread(), "Current thread should have acquired the lock");
 try {
 thread2.join();
 } catch (InterruptedException e) {
 // Handle InterruptedException
 }
 checkState(!monitor.isOccupiedByCurrentThread(), "Current thread should have released the lock");
 }

 @Test
 public void testEnterInterruptibly() {
 Monitor monitor = new Monitor();
 Thread thread1 = Thread.currentThread();
 Thread thread2 = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 // Handle InterruptedException
 }
 checkState(monitor.isOccupiedByCurrentThread(), "Thread 2 should have acquired the lock");
 });
 thread2.start();
 monitor.enterInterruptibly();
 checkState(monitor.isOccupiedByCurrentThread(), "Current thread should have acquired the lock");
 try {
 thread2.join();
 } catch (InterruptedException e) {
 // Handle InterruptedException
 }
 checkState(!monitor.isOccupiedByCurrentThread(), "Current thread should have released the lock");
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyWithDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Thread thread1 = Thread.currentThread();
 Thread thread2 = new Thread(() -> monitor1.enterInterruptibly());
 monitor2.enter();
 monitor1.enterInterruptibly();
 thread2.start();
 }

 @Test
 public void testEnterWhen() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard);
 checkState(monitor.isOccupiedByCurrentThread(), "Current thread should have acquired the lock");
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor1.enterWhen(guard);
 monitor2.enter();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithUnsatisfiedGuard() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard);
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhenUninterruptibly(guard);
 checkState(monitor.isOccupiedByCurrentThread(), "Current thread should have acquired the lock");
 monitor.leave();
 }

 @Test
 public void testEnterWhenUninterruptiblyWithDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor1.enterWhenUninterruptibly(guard);
 monitor2.enter();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenUninterruptiblyWithUnsatisfiedGuard() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhenUninterruptibly(guard);
 }

 @Test
 public void testEnterWhenWithTimeOut() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 long timeout = 1;
 boolean success = monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 assertTrue(success);
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithTimeOutAndDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 long timeout = 1;
 monitor1.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 monitor2.enter();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenWithTimeOutAndInterruption() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 long timeout = 1;
 monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 Thread thread = new Thread(() -> monitor.interrupt());
 thread.start();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIf() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIf(guard);
 }

 @Test
 public void testEnterIfWithSatisfiedGuard() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterIf(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfWithUnsatisfiedGuard() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIf(guard);
 }

 @Test
 public void testEnterIfInterruptibly() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 try {
 monitor.enterIfInterruptibly(guard);
 } catch (InterruptedException e) {
 // Handle InterruptedException
 }
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfInterruptiblyWithUnsatisfiedGuard() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIfInterruptibly(guard);
 }

 @Test
 public void testEnterIfWithTimeOut() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 long timeout = 1;
 boolean success = monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS);
 assertTrue(success);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfWithTimeOutAndInterruption() {
 Monitor monitor = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 long timeout = 1;
 monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS);
 Thread thread = new Thread(() -> monitor.interrupt());
 thread.start();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfWithDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor1.enterIf(guard);
 monitor2.enter();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testLeaveWhenNotLocked() {
 Monitor monitor = new Monitor();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testIsFairWhenNotLocked() {
 Monitor monitor = new Monitor();
 assertTrue(monitor.isFair());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testIsOccupiedWhenNotLocked() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testIsOccupiedByCurrentThreadWhenNotLocked() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithNegativeTimeout() {
 Monitor monitor = new Monitor();
 monitor.enter(-1, TimeUnit.MILLISECONDS);
 }

 @Test(expected = NullPointerException.class)
 public void testNullConstructor() {
 new Monitor(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFairConstructorWithFalseArgument() {
 new Monitor(false);
 }

 @Test(expected = IllegalStateException.class)
 public void testEnterTwice() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enter();
 }
}