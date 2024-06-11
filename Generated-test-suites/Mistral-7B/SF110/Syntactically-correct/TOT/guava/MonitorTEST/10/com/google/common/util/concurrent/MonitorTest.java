package com.google.common.util.concurrent;

import com.google.common.annotations.*;
import com.google.common.base.*;
import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

@Beta
public class MonitorTest {

 @Rule
 public final Timeout globalTimeout = new Timeout(5000); // Five seconds timeout for all tests

 @Test
 public void testMonitorConstruction() {
 Monitor monitor = new Monitor();
 Monitor fairMonitor = new Monitor(true);
 assertNull(monitor.getActiveGuards());
 assertNull(fairMonitor.getActiveGuards());
 }

 @Test
 public void testLockEnterAndLeave() throws InterruptedException {
 Monitor monitor = new Monitor();

 Thread thread1 = new Thread(() -> {
 monitor.enter();
 });
 Thread thread2 = new Thread(() -> {
 monitor.enter();
 });

 thread1.start();
 thread2.start();
 thread1.join();
 thread2.join();

 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testLockEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();

 Thread thread1 = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 // Interrupted exception is expected
 }
 });
 Thread thread2 = new Thread(() -> {
 monitor.enter();
 thread1.interrupt();
 });

 thread1.start();
 thread2.start();
 thread1.join();
 thread2.join();

 assertFalse(monitor.isOccupied());
 Thread.currentThread().interrupt();
 }

 @Test
 public void testLockEnterWithTimeOut() {
 Monitor monitor = new Monitor();

 Thread thread1 = new Thread(() -> {
 try {
 monitor.enter(1, TimeUnit.SECONDS);
 } catch (InterruptedException e) {
 fail("Interrupted exception is not expected");
 } catch (TimeoutException e) {
 // Timeout exception is expected
 }
 });
 Thread thread2 = new Thread(() -> {
 monitor.enter();
 thread1.interrupt();
 });

 thread1.start();
 thread2.start();
 thread1.join();
 thread2.join();

 assertFalse(monitor.isOccupied());
 Thread.currentThread().interrupt();
 }

 @Test
 public void testLockEnterInterruptiblyWithTimeOut() throws InterruptedException {
 Monitor monitor = new Monitor();

 Thread thread1 = new Thread(() -> {
 try {
 monitor.enterInterruptibly(1, TimeUnit.SECONDS);
 } catch (InterruptedException e) {
 fail("Interrupted exception is not expected");
 } catch (TimeoutException e) {
 // Timeout exception is expected
 }
 });
 Thread thread2 = new Thread(() -> {
 monitor.enter();
 thread1.interrupt();
 });

 thread1.start();
 thread2.start();
 thread1.join();
 thread2.join();

 assertFalse(monitor.isOccupied());
 Thread.currentThread().interrupt();
 }

 @Test
 public void testLockTryEnter() {
 Monitor monitor = new Monitor();

 Thread thread1 = new Thread(() -> {
 try {
 monitor.tryEnter();
 } catch (InterruptedException e) {
 fail("Interrupted exception is not expected");
 }
 });
 Thread thread2 = new Thread(() -> {
 monitor.enter();
 thread1.interrupt();
 });

 thread1.start();
 thread2.start();
 thread1.join();
 thread2.join();

 assertFalse(monitor.isOccupied());
 Thread.currentThread().interrupt();
 }

 @Test
 public void testLockEnterWhen() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuard());
 monitor.enter();
 monitor.enterWhen(new MyGuard());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testLockEnterWhenInvalidMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enterWhen(monitor1);
 }

 @Test
 public void testLockEnterWhenUninterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuard());
 monitor.enterWhenUninterruptibly();
 monitor.enterWhenUninterruptibly();
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testLockEnterWhenInvalidMonitorUninterruptibly() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enterWhenUninterruptibly();
 monitor2.enterWhenUninterruptibly(monitor1);
 }

 @Test
 public void testLockEnterWhenWithTimeOut() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuard());
 monitor.enterWhen(new MyGuard(), 1, TimeUnit.SECONDS);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testLockEnterWhenWithTimeOutInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuard());
 monitor.enterWhen(new MyGuard(), 1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 }

 @Test
 public void testLockEnterWhenWithSignalBeforeWaiting() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuard());
 monitor.enterWhen(new MyGuard(), true);
 monitor.leave();
 }

 @Test
 public void testLockEnterWhenWithSignalBeforeWaitingAndTimeOut() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuard());
 monitor.enterWhen(new MyGuard(), true, 1, TimeUnit.SECONDS);
 monitor.leave();
 }

 @Test
 public void testLockEnterIf() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardIf());
 monitor.enterIf();
 monitor.leave();
 }

 @Test
 public void testLockEnterIfInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardIf());
 monitor.enterIfInterruptibly();
 monitor.leave();
 }

 @Test
 public void testLockEnterIfWithTimeOut() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardIf());
 monitor.enterIf(1, TimeUnit.SECONDS);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testLockEnterIfWithInvalidMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enterIf();
 monitor2.enterIf(monitor1);
 }

 @Test
 public void testLockEnterIfUninterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardIf());
 monitor.enterIfUninterruptibly();
 monitor.leave();
 }

 @Test
 public void testLockEnterIfUninterruptiblyWithTimeOut() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardIf());
 monitor.enterIfUninterruptibly(1, TimeUnit.SECONDS);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testLockEnterIfUninterruptiblyWithInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardIf());
 monitor.enterIfUninterruptibly();
 Thread.currentThread().interrupt();
 }

 @Test
 public void testLockTryEnterIf() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardIf());
 monitor.tryEnterIf();
 monitor.leave();
 }

 @Test
 public void testLockWaitFor() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWait());
 monitor.enter();
 monitor.waitFor(new MyGuardWait());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testLockWaitForInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWait());
 monitor.enter();
 Thread.currentThread().interrupt();
 monitor.waitFor(new MyGuardWait());
 }

 @Test
 public void testLockWaitForUninterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWait());
 monitor.enter();
 monitor.waitForUninterruptibly(new MyGuardWait());
 monitor.leave();
 }

 @Test
 public void testLockWaitForWithConditionSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWaitSatisfied());
 monitor.enter();
 monitor.waitFor(new MyGuardWaitSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testLockWaitForWithConditionNotSatisfiedAndInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWaitNotSatisfied());
 monitor.enter();
 Thread.currentThread().interrupt();
 monitor.waitFor(new MyGuardWaitNotSatisfied());
 }

 @Test
 public void testLockWaitForWithConditionNotSatisfiedAndTimeout() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWaitNotSatisfied());
 monitor.enter();
 monitor.waitFor(new MyGuardWaitNotSatisfied(), 1, TimeUnit.SECONDS);
 monitor.leave();
 }

 @Test
 public void testLockWaitForUninterruptiblyWithConditionSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWaitSatisfied());
 monitor.enter();
 monitor.waitForUninterruptibly(new MyGuardWaitSatisfied());
 monitor.leave();
 }

 @Test
 public void testLockWaitForUninterruptiblyWithConditionNotSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWaitNotSatisfied());
 monitor.enter();
 monitor.waitForUninterruptibly(new MyGuardWaitNotSatisfied());
 monitor.leave();
 }

 @Test
 public void testLockWaitForUninterruptiblyWithConditionNotSatisfiedAndSignalBeforeWaiting() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWaitNotSatisfied());
 monitor.enter();
 monitor.waitForUninterruptibly(new MyGuardWaitNotSatisfied(), true);
 monitor.leave();
 }

 @Test
 public void testLockWaitForUninterruptiblyWithConditionNotSatisfiedAndSignalBeforeWaitingAndTimeout() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardWaitNotSatisfied());
 monitor.enter();
 monitor.waitForUninterruptibly(new MyGuardWaitNotSatisfied(), true, 1, TimeUnit.SECONDS);
 monitor.leave();
 }

 @Test
 public void testLockAwaitNanos() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardAwaitNanos());
 monitor.enter();
 long timeoutNanos = 1000;
 long enteredNanos = System.nanoTime();
 boolean result = monitor.awaitNanos(new MyGuardAwaitNanos(), timeoutNanos);
 long elapsedNanos = System.nanoTime() - enteredNanos;
 assertTrue(result || elapsedNanos >= timeoutNanos);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testLockAwaitNanosInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardAwaitNanos());
 monitor.enter();
 Thread.currentThread().interrupt();
 monitor.awaitNanos(new MyGuardAwaitNanos(), 1000);
 }

 @Test
 public void testLockAwaitNanosWithSignalBeforeWaiting() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardAwaitNanos());
 monitor.enter();
 monitor.awaitNanos(new MyGuardAwaitNanos(), 1000, true);
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testLockAwaitNanosWithSignalBeforeWaitingAndInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardAwaitNanos());
 monitor.enter();
 Thread.currentThread().interrupt();
 monitor.awaitNanos(new MyGuardAwaitNanos(), 1000, true);
 }

 @Test(expected = InterruptedException.class)
 public void testLockAwaitNanosWithSignalBeforeWaitingAndTimeout() throws InterruptedException {
 Monitor monitor = new Monitor(new MyGuardAwaitNanos());
 monitor.enter();
 monitor.awaitNanos(new MyGuardAwaitNanos(), 1, TimeUnit.SECONDS, true);
 }

 public static class MyGuard implements Guard {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 }

 public static class MyGuardIf implements Guard {
 @Override
 public boolean isSatisfied() {
 return Thread.currentThread().isInterrupted();
 }
 }

 public static class MyGuardWait implements Guard {
 private final Object condition;

 MyGuardWait() {
 this.condition = new Object();
 }

 @Override
 public boolean isSatisfied() {
 return false;
 }
 }

 public static class MyGuardWaitSatisfied extends MyGuardWait {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 }

 public static class MyGuardWaitNotSatisfied extends MyGuardWait {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 }

 public static class MyGuardAwaitNanos implements Guard {
 private final long nanos;

 MyGuardAwaitNanos(long nanos) {
 this.nanos = nanos;
 }

 @Override
 public boolean isSatisfied() {
 return false;
 }
 }
}