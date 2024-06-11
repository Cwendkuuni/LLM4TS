package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Guard;
import com.google.common.base.Threads;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor$Guard;

public class MonitorTest {

 private Monitor monitor;
 private Guard guard;
 private Thread thread;

 @Test
 public void testEnter() {
 monitor = new Monitor();
 monitor.enter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterInterruptibly() {
 monitor = new Monitor();
 thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 thread.start();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWithTimeLimit() {
 monitor = new Monitor();
 thread = new Thread(() -> {
 try {
 monitor.enter(1, TimeUnit.MILLISECONDS);
 fail("TimeoutException expected");
 } catch (InterruptedException e) {
 // Expected
 } catch (TimeoutException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 thread.start();
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhen() {
 monitor = new Monitor();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenWithTimeLimit() {
 monitor = new Monitor();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 thread = new Thread(() -> {
 try {
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 fail("TimeoutException expected");
 } catch (InterruptedException e) {
 // Expected
 } catch (TimeoutException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 monitor.leave();
 thread.start();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor = new Monitor();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhenUninterruptibly(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeLimit() {
 monitor = new Monitor();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 thread = new Thread(() -> {
 try {
 monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 fail("TimeoutException expected");
 } catch (InterruptedException e) {
 // Expected
 } catch (TimeoutException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 monitor.leave();
 thread.start();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIf() {
 monitor = new Monitor();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterIf(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIfWithTimeLimit() {
 monitor = new Monitor();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 thread = new Thread(() -> {
 try {
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 fail("TimeoutException expected");
 } catch (InterruptedException e) {
 // Expected
 } catch (TimeoutException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 monitor.leave();
 thread.start();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIfInterruptibly() {
 monitor = new Monitor();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterIfInterruptibly(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeLimit() {
 monitor = new Monitor();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 thread = new Thread(() -> {
 try {
 monitor.enterIfInterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 fail("TimeoutException expected");
 } catch (InterruptedException e) {
 // Expected
 } catch (TimeoutException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 monitor.leave();
 thread.start();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterIfWithGuardThatAlwaysFails() {
 monitor = new Monitor();
 thread = new Thread(() -> {
 try {
 monitor.enterIf(new FailingGuard(), 1, TimeUnit.MILLISECONDS);
 fail("TimeoutException expected");
 } catch (InterruptedException e) {
 // Expected
 } catch (TimeoutException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 monitor.leave();
 thread.start();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testWaitFor() {
 monitor = new Monitor();
 thread = new Thread(() -> {
 try {
 monitor.waitFor(new WaitingGuard());
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 // Expected
 }
 });
 monitor.enter();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard);
 thread.start();
 monitor.leave();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor = new Monitor();
 thread = new Thread(() -> {
 try {
 monitor.waitForUninterruptibly(new WaitingGuard());
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 // Expected
 }
 });
 monitor.enter();
 guard = new Monitor$Guard() {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhenUninterruptibly(guard);
 thread.start();
 monitor.leave();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testWaitForWithGuardThatAlwaysFails() {
 monitor = new Monitor();
 thread = new Thread(() -> {
 try {
 monitor.waitFor(new FailingGuard(), 1, TimeUnit.MILLISECONDS);
 fail("TimeoutException expected");
 } catch (InterruptedException e) {
 // Expected
 } catch (TimeoutException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 monitor.leave();
 thread.start();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testWaitForUninterruptiblyWithGuardThatAlwaysFails() {
 monitor = new Monitor();
 thread = new Thread(() -> {
 try {
 monitor.waitForUninterruptibly(new FailingGuard());
 fail("InterruptedException expected");
 } catch (InterruptedException e) {
 // Expected
 }
 });
 monitor.enter();
 monitor.interrupt();
 monitor.leave();
 thread.start();
 thread.join();
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 private static class FailingGuard extends Monitor$Guard {
 @Override
 public boolean isSatisfied() {
 throw new IllegalStateException();
 }
 }

 private static class WaitingGuard extends Monitor$Guard {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 }

}