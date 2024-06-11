package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

 private Monitor monitor;
 private final Object condition = new Object();

 @Before
 public void setup() {
 monitor = new Monitor();
 }

 @Test
 public void testEnter() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Thread thread1 = new Thread(() -> monitor1.enter());
 Thread thread2 = new Thread(() -> monitor2.enter());
 thread1.start();
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertTrue(monitor1.isOccupiedByCurrentThread() && monitor2.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyWithFalseCondition() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 fail("Expected InterruptedException or IllegalMonitorStateException");
 }

 @Test
 public void testEnterInterruptiblyWithTrueCondition() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Thread thread1 = new Thread(() -> monitor1.enterInterruptibly());
 Thread thread2 = new Thread(() -> monitor2.enterInterruptibly());
 thread1.start();
 thread2.start();
 thread1.interrupt();
 thread2.join();
 assertFalse(monitor2.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithConditionSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread thread1 = new Thread(() -> monitor.enterInterruptibly(condition));
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 thread1.start();
 thread1.join();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnter() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 boolean canEnter1 = monitor1.tryEnter();
 boolean canEnter2 = monitor2.tryEnter();
 assertTrue(canEnter1 || canEnter2);
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard);
 monitor.enterWhenUninterruptibly(guard);
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 monitor.enterWhenUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
 monitor.enterWhenInterruptibly(guard, 10, TimeUnit.MILLISECONDS);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard);
 fail("Expected IllegalMonitorStateException");
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenInterruptiblyWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhenInterruptibly(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testEnterIf() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterIf(guard);
 monitor.enterIfUninterruptibly(guard);
 monitor.enterIf(guard, 1, TimeUnit.MILLISECONDS);
 monitor.enterIfUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 monitor.enterIf(guard, 10, TimeUnit.MILLISECONDS);
 monitor.enterIfInterruptibly(guard, 10, TimeUnit.MILLISECONDS);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIf(guard);
 fail("Expected IllegalMonitorStateException");
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterIfInterruptibly(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testTryEnterIf() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 boolean canEnter1 = monitor1.tryEnterIf(new Guard(monitor1) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 boolean canEnter2 = monitor2.tryEnterIf(new Guard(monitor2) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 });
 assertTrue(canEnter1 || canEnter2);
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.waitFor(guard);
 monitor.waitForUninterruptibly(guard);
 monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 monitor.waitFor(guard, 10, TimeUnit.MILLISECONDS);
 monitor.waitForInterruptibly(guard, 10, TimeUnit.MILLISECONDS);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.waitFor(guard);
 fail("Expected IllegalMonitorStateException");
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForInterruptiblyWithFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.waitForInterruptibly(guard);
 fail("Expected InterruptedException");
 }

 @Test
 public void testWaitForWithConditionSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.waitFor(guard);
 monitor.waitForUninterruptibly(guard);
 monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 monitor.waitFor(guard, 10, TimeUnit.MILLISECONDS);
 monitor.waitForInterruptibly(guard, 10, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testIsFair() {
 Monitor monitor1 = new Monitor(true);
 Monitor monitor2 = new Monitor(false);
 assertFalse(monitor1.isFair());
 assertTrue(monitor2.isFair());
 }

 @Test
 public void testIsOccupied() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 Monitor monitor = new Monitor();
 monitor.enter();
 monitor.enter();
 assertEquals(2, monitor.getOccupiedDepth());
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 Monitor monitor = new Monitor();
 Monitor monitor1 = new Monitor();
 Thread thread1 = new Thread(() -> monitor.enter());
 Thread thread2 = new Thread(() -> monitor1.enter());
 thread1.start();
 thread2.start();
 thread1.interrupt();
 thread2.join();
 assertEquals(0, monitor.getQueueLength());
 assertEquals(1, monitor1.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 Monitor monitor = new Monitor();
 Monitor monitor1 = new Monitor();
 Thread thread1 = new Thread(() -> monitor.enter());
 Thread thread2 = new Thread(() -> monitor1.enter());
 thread1.start();
 thread2.start();
 thread1.interrupt();
 thread2.join();
 assertFalse(monitor.hasQueuedThreads());
 assertTrue(monitor1.hasQueuedThreads());
 }

 @Test
 public void testHasQueuedThread() {
 Monitor monitor = new Monitor();
 Monitor monitor1 = new Monitor();
 Thread thread1 = new Thread(() -> monitor.enter());
 Thread thread2 = new Thread(() -> monitor1.enter());
 thread1.start();
 thread2.start();
 thread1.interrupt();
 thread2.join();
 assertFalse(monitor.hasQueuedThread(thread1));
 assertFalse(monitor.hasQueuedThread(thread2));
 assertFalse(monitor1.hasQueuedThread(thread1));
 assertFalse(monitor1.hasQueuedThread(thread2));
 }
}