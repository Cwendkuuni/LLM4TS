package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

public class MonitorTest {

 @BeforeClass
 public static void setup() {
 System.out.println("Setup - Before Class");
 }

 @AfterClass
 public static void teardown() {
 System.out.println("Teardown - After Class");
 }

 @Before
 public void before() {
 System.out.println("Before - Each Test Method");
 }

 @After
 public void after() {
 System.out.println("After - Each Test Method");
 }

 @Test
 public void testMonitorConstruction() {
 Monitor monitor = new Monitor();
 Monitor fairMonitor = new Monitor(true);
 assertEquals(false, monitor.isFair());
 assertEquals(true, fairMonitor.isFair());
 }

 @Test
 public void testEnter() {
 Monitor monitor = new Monitor();
 Thread thread = new Thread();
 monitor.enter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyWhenMonitorIsNotHeld() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread thread = new Thread();
 monitor.enterInterruptibly();
 thread.interrupt();
 monitor.enterInterruptibly();
 thread.join();
 fail("Expected IllegalMonitorStateException");
 }

 @Test
 public void testEnterInterruptiblyWhenMonitorIsHeld() throws InterruptedException {
 Monitor monitor = new Monitor();
 Thread thread = new Thread();
 monitor.enter();
 thread.interrupt();
 monitor.enterInterruptibly();
 assertFalse(thread.isInterrupted());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenGuardIsNotSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhen(guard);
 thread.join();
 fail("Expected InterruptedException");
 }

 @Test
 public void testEnterWhenGuardIsSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhen(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterWhenUninterruptiblyGuardIsNotSatisfied() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 assertFalse(guard.isSatisfied());
 }

 @Test
 public void testEnterWhenUninterruptiblyGuardIsSatisfied() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testEnterIfGuardIsSatisfied() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterIf(guard);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testEnterIfGuardIsNotSatisfied() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterIf(guard);
 assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testEnterIfInterruptiblyGuardIsSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterIfInterruptibly(guard);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyGuardIsNotSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterIfInterruptibly(guard);
 thread.interrupt();
 monitor.enterIfInterruptibly(guard);
 thread.join();
 fail("Expected InterruptedException");
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyWhenGuardIsNotSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterIfInterruptibly(guard);
 monitor.enterWhen(guard);
 thread.interrupt();
 monitor.enterIfInterruptibly(guard);
 thread.join();
 fail("Expected InterruptedException");
 }

 @Test
 public void testEnterIfInterruptiblyWhenGuardIsSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enterIfInterruptibly(guard);
 monitor.leave();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testTryEnter() {
 Monitor monitor = new Monitor();
 Thread thread = new Thread();
 assertTrue(monitor.tryEnter());
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertFalse(monitor.tryEnter());
 }

 @Test
 public void testTryEnterWhenMonitorIsOccupiedByAnotherThread() {
 Monitor monitor = new Monitor();
 Thread thread1 = new Thread();
 Thread thread2 = new Thread();
 monitor.enter();
 assertFalse(monitor.tryEnter());
 monitor.leave();
 assertTrue(monitor.tryEnter());
 }

 @Test
 public void testTryEnterWhenMonitorIsOccupiedByCurrentThread() {
 Monitor monitor = new Monitor();
 Thread thread = new Thread();
 monitor.enter();
 assertFalse(monitor.tryEnter());
 }

 @Test
 public void testAwait() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 monitor.waitFor(guard);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testAwaitUninterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 monitor.waitForUninterruptibly(guard);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testAwaitNanos() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 long nanos = monitor.awaitNanos(guard, 1);
 assertTrue(nanos < 0);
 monitor.leave();
 }

 @Test
 public void testAwaitNanosWhenGuardIsSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 long nanos = monitor.awaitNanos(guard, 100);
 assertTrue(nanos > 0);
 monitor.leave();
 }

 @Test
 public void testAwaitNanosWhenGuardIsNotSatisfied() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 long nanos = monitor.awaitNanos(guard, 1);
 assertTrue(nanos < 0);
 monitor.leave();
 }

 @Test
 public void testIsFair() {
 Monitor monitor = new Monitor();
 assertTrue(monitor.isFair());
 Monitor fairMonitor = new Monitor(false);
 assertFalse(fairMonitor.isFair());
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
 int depth = monitor.getOccupiedDepth();
 assertEquals(0, depth);
 monitor.enter();
 depth = monitor.getOccupiedDepth();
 assertEquals(1, depth);
 monitor.leave();
 depth = monitor.getOccupiedDepth();
 assertEquals(0, depth);
 }

 @Test
 public void testGetQueueLength() {
 Monitor monitor = new Monitor();
 int length = monitor.getQueueLength();
 assertEquals(0, length);
 monitor.enter();
 monitor.enter();
 length = monitor.getQueueLength();
 assertEquals(0, length);
 }

 @Test
 public void testHasQueuedThreads() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.hasQueuedThreads(new Object()));
 monitor.enter();
 assertTrue(monitor.hasQueuedThreads(new Object()));
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads(new Object()));
 }

 @Test
 public void testSignalNextWaiter() {
 Monitor monitor = new Monitor();
 Guard guard1 = new GuardMonitor();
 Guard guard2 = new GuardMonitor();
 monitor.enter();
 monitor.await(guard1);
 monitor.signalNextWaiter();
 monitor.await(guard2);
 assertEquals(guard2, guard1.next);
 monitor.leave();
 }

 @Test
 public void testIsSatisfied() {
 Guard guard = new GuardMonitor();
 assertTrue(guard.isSatisfied());
 }

 @Test
 public void testIsSatisfiedWhenMonitorIsNotHeld() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 assertFalse(monitor.isSatisfied(guard));
 }

 @Test
 public void testIsSatisfiedWhenMonitorIsHeld() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.enter();
 assertTrue(monitor.isSatisfied(guard));
 monitor.leave();
 }

 @Test
 public void testAwaitWhenMonitorIsNotHeld() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.await(guard);
 fail("Expected InterruptedException");
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testAwaitWhenMonitorIsNotHeldAndGuardIsNotMonitor() throws InterruptedException {
 Monitor monitor = new Monitor();
 Object object = new Object();
 monitor.await(object);
 fail("Expected IllegalMonitorStateException");
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testAwaitUninterruptiblyWhenMonitorIsNotHeld() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor();
 monitor.awaitUninterruptibly(guard);
 fail("Expected IllegalMonitorStateException");
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testAwaitUninterruptiblyWhenMonitorIsNotHeldAndGuardIsNotMonitor() {
 Monitor monitor = new Monitor();
 Object object = new Object();
 monitor.awaitUninterruptibly(object);
 fail("Expected IllegalMonitorStateException");
 }
}

class GuardMonitor extends Monitor.Guard {
 Monitor monitor;
 volatile boolean isSatisfied = true;

 public GuardMonitor() {
 this.monitor = new Monitor();
 }

 @Override
 public boolean isSatisfied() {
 return isSatisfied;
 }
}