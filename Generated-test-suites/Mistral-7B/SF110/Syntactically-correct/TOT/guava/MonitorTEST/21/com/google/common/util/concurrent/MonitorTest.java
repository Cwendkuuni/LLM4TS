package com.google.common.util.concurrent.monitor;

import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor.Guard;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorTest {

 @Test
 public void testEnter() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 monitor.enter();
 Assert.assertTrue("Monitor should be locked", monitor.isOccupiedByCurrentThread());
 monitor.leave();
 Assert.assertFalse("Monitor should be unlocked", monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 Thread thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 });
 monitor.enter();
 thread.start();
 thread.join();
 monitor.leave();
 }

 @Test
 public void testEnterWithTimeOut() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 long time = 10L;
 TimeUnit unit = TimeUnit.MILLISECONDS;

 boolean entered = monitor.enter(time, unit);
 Assert.assertTrue("Monitor should have been able to enter", entered);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 Thread thread = new Thread(() -> monitor.enterInterruptibly());
 monitor.enter();
 thread.start();
 monitor.leave();
 thread.join();
 }

 @Test
 public void testEnterIf() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 boolean entered = monitor.enterIf(guard);
 Assert.assertTrue("Monitor should allow entering if condition is true", entered);
 monitor.leave();
 }

 @Test
 public void testEnterIfWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };

 boolean entered = monitor.enterIf(guard);
 Assert.assertFalse("Monitor should not allow entering if condition is false", entered);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfWithWrongMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 monitor1.enter();
 monitor2.enterIf(guard);
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Monitor monitor = new Monitor();
 final Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };

 monitor.enterWhen(guard);
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 final Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };

 long time = 10L;
 TimeUnit unit = TimeUnit.MILLISECONDS;

 monitor.enterWhen(guard, time, unit);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithWrongMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 final Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };

 monitor1.enter();
 monitor1.enterWhen(guard);
 monitor2.enter();
 }

 @Test
 public void testEnterWhenInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 final Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };

 monitor.enterWhenInterruptibly(guard);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenInterruptiblyWithInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor();
 final Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };

 Thread thread = new Thread(() -> monitor.enterWhenInterruptibly(guard));
 monitor.enter();
 thread.start();
 monitor.leave();
 thread.join();
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 final Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 boolean entered = monitor.enterIfInterruptibly(guard);
 Assert.assertTrue("Monitor should allow entering if condition is true", entered);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 final Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };

 Thread thread = new Thread(() -> monitor.enterIfInterruptibly(guard));
 monitor.enter();
 monitor.leave();
 thread.join();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfInterruptiblyWithWrongMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 final Guard guard = new Monitor.Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 monitor1.enter();
 monitor1.enterIfInterruptibly(guard);
 monitor2.enter();
 }
}