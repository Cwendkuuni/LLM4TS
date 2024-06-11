package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

 @BeforeClass
 public static void setup() {
 System.out.println("### Monitor Test Started ###");
 }

 @AfterClass
 public static void teardown() {
 System.out.println("### Monitor Test Completed ###");
 }

 @Test
 public void testMonitorConstruction() {
 Monitor monitor = new Monitor();
 Monitor fairMonitor = new Monitor(true);
 }

 @Test
 public void testEnter() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyWithFalseLock() {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 fail("Expected IllegalMonitorStateException");
 }

 @Test
 public void testEnterInterruptiblyWithLockedMonitor() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterInterruptibly();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterWhen(new MyGuardFalseCondition());
 fail("Expected InterruptedException or timeout");
 }

 @Test
 public void testEnterWithTrueCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterWhen(new MyGuardTrueCondition());
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWithTimeoutCondition() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterWhen(new MyGuardTimeoutCondition(), 5, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithFalseCondition() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterWhenUninterruptibly(new MyGuardFalseCondition());
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTrueCondition() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterWhenUninterruptibly(new MyGuardTrueCondition());
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeoutCondition() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterWhenUninterruptibly(new MyGuardTimeoutCondition(), 5, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIf() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterIf(new MyGuardTrueCondition());
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfWithFalseCondition() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterIf(new MyGuardFalseCondition());
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfWithTimeoutCondition() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterIf(new MyGuardTimeoutCondition(), 5, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterIfInterruptibly(new MyGuardTrueCondition());
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptiblyWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterIfInterruptibly(new MyGuardFalseCondition());
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeoutCondition() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterIfInterruptibly(new MyGuardTimeoutCondition(), 5, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfInterruptiblyWithLockedMonitor() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enterIfInterruptibly(new MyGuardTrueCondition());
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnter() {
 Monitor monitor = new Monitor();
 monitor.tryEnter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterWithLockedMonitor() {
 Monitor monitor = new Monitor();
 monitor.enter();
 boolean locked = monitor.tryEnter();
 assertTrue(locked);
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterWithTimeoutCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 boolean locked = monitor.tryEnter(1, TimeUnit.SECONDS);
 assertFalse(locked);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testLeave() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testIsFair() {
 Monitor monitor = new Monitor();
 assertTrue(monitor.isFair());
 Monitor fairMonitor = new Monitor(true);
 assertTrue(fairMonitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 monitor.enter();
 assertTrue(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 monitor.enter();
 assertTrue(monitor.getOccupiedDepth() == 1);
 monitor.enter();
 assertTrue(monitor.getOccupiedDepth() == 2);
 monitor.leave();
 assertTrue(monitor.getOccupiedDepth() == 1);
 }

 @Test
 public void testGetQueueLength() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.getQueueLength() > 0);
 monitor.enterWhen(new MyGuardFalseCondition());
 assertFalse(monitor.getQueueLength() > 0);
 Monitor anotherMonitor = new Monitor();
 monitor.enterWhen(new MyCondition(){
 @Override public boolean isSatisfied() {
 anotherMonitor.enter();
 return true;
 }});
 assertTrue(monitor.getQueueLength() == 1);
 monitor.leave();
 assertFalse(monitor.getQueueLength() > 0);
 }

 @Test
 public void testHasQueuedThreads() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.hasQueuedThreads(new MyCondition(){
 @Override public boolean isSatisfied() {
 return false;
 }}));
 monitor.enterWhen(new MyCondition(){
 @Override public boolean isSatisfied() {
 return false;
 }});
 assertTrue(monitor.hasQueuedThreads(new MyCondition(){
 @Override public boolean isSatisfied() {
 return false;
 } }));
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads(new MyCondition(){
 @Override public boolean isSatisfied() {
 return false;
 } }));
 }

 @Test
 public void testSignalNextWaiter() {
 Monitor monitor = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor.enter();
 monitor2.enterWhen(monitor);
 monitor.signalNextWaiter();
 monitor2.enter();
 monitor.leave();
 assertTrue(monitor2.isOccupiedByCurrentThread());
 }

 @Test
 public void testIsSatisfied() {
 Guard guard = new MyGuardTrueCondition();
 assertTrue(guard.isSatisfied());
 Guard guardFalse = new MyGuardFalseCondition();
 assertFalse(guardFalse.isSatisfied());
 }
}

class MyGuardTrueCondition extends com.google.common.util.concurrent.Monitor.Guard {
 protected MyGuardTrueCondition() {
 super(MonitorTest.this.monitor);
 }

 @Override
 public boolean isSatisfied() {
 return true;
 }
}

class MyGuardFalseCondition extends com.google.common.util.concurrent.Monitor.Guard {
 protected MyGuardFalseCondition() {
 super(MonitorTest.this.monitor);
 }

 @Override
 public boolean isSatisfied() {
 return false;
 }
}

class MyGuardTimeoutCondition extends com.google.common.util.concurrent.Monitor.Guard {
 private final Monitor monitor;
 private final long timeout;
 private final TimeUnit timeUnit;

 protected MyGuardTimeoutCondition(Monitor monitor, long timeout, TimeUnit timeUnit) {
 super(monitor);
 this.monitor = monitor;
 this.timeout = timeout;
 this.timeUnit = timeUnit;
 }

 @Override
 public boolean isSatisfied() throws TimeoutException {
 long currentTime = System.nanoTime();
 long deadline = currentTime + this.timeUnit.toNanos(this.timeout);
 while (!this.isSatisfied()) {
 if (currentTime > deadline) {
 throw new TimeoutException();
 }
 currentTime = System.nanoTime();
 try {
 this.monitor.awaitNanos(this, 1, TimeUnit.NANOSECONDS);
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 }
 return true;
 }
}