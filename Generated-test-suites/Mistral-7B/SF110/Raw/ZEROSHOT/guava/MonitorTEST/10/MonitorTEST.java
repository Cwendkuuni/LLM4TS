package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class MonitorTest {

 @BeforeClass
 public static void setUpClass() {
 // Set up any static resources or initialize any static data here
 }

 @AfterClass
 public static void tearDownClass() {
 // Release any static resources and perform any other clean-up operations here
 }

 @Before
 public void setUp() {
 // Set up any instance variables or initialize any instance data here
 }

 @After
 public void tearDown() {
 // Release any instance resources and perform any other clean-up operations here
 }

 @Test
 public void testConstructor() {
 Monitor monitor = new Monitor();
 assertNull(monitor.activeGuards);
 assertFalse(monitor.isFair());
 ReentrantLock lock = monitor.lock;
 assertTrue(lock.isFair());
 }

 @Test
 public void testConstructorWithFair() {
 Monitor monitor = new Monitor(true);
 assertNull(monitor.activeGuards);
 assertTrue(monitor.isFair());
 ReentrantLock lock = monitor.lock;
 assertTrue(lock.isFair());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullMonitorInGuardConstructor() {
 Monitor monitor = new Monitor();
 try {
 new Guard(null);
 fail("Expected IllegalMonitorStateException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testEnter() {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enter(); // should throw exception
 fail("Expected IllegalMonitorStateException");
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWithInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 Thread interrupter = Thread.currentThread();
 interrupter.interrupt();
 assertFalse(Thread.interrupted());
 monitor.leave();
 assertTrue(Thread.interrupted());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithInterruptedAndLockAcquired() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enterInterruptibly();
 Thread interrupter = Thread.currentThread();
 monitor.enterInterruptibly(); // should throw InterruptedException
 interrupter.interrupt();
 fail("Expected InterruptedException");
 }

 @Test
 public void testEnterWithTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 long timeout = 1;
 assertFalse(monitor.enter(timeout, TimeUnit.MILLISECONDS));
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWithTimeoutAndLockAcquired() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 long timeout = 1;
 monitor.enter();
 assertTrue(monitor.enter(timeout, TimeUnit.MILLISECONDS));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 long timeout = 1;
 assertFalse(monitor.enterInterruptibly(timeout, TimeUnit.MILLISECONDS));
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterInterruptiblyWithTimeoutAndLocked() throws InterruptedException {
 Monitor monitor = new Monitor();
 monitor.enter();
 assertFalse(monitor.enterInterruptibly(1, TimeUnit.MILLISECONDS));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnter() {
 Monitor monitor = new Monitor();
 assertFalse(monitor.tryEnter());
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertTrue(monitor.tryEnter());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterWithDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 assertFalse(monitor2.tryEnter());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testTryEnterWithDifferentMonitorAndLocked() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 try {
 monitor2.tryEnter();
 fail("Expected IllegalMonitorStateException");
 } catch (IllegalMonitorStateException e) {
 // Expected exception
 }
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 monitor.enterWhen(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithDifferentMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor(monitor1);
 monitor1.enter();
 monitor2.enter();
 monitor1.enterWhen(guard); // should throw exception
 fail("Expected IllegalMonitorStateException");
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 long timeout = 1;
 Guard guard = new GuardMonitor(monitor);
 assertFalse(monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS));
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeoutAndLocked() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 assertFalse(monitor.enterWhenUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIf() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 assertFalse(monitor.enterIf(guard));
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertTrue(monitor.enterIf(guard));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfWithDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor(monitor1);
 monitor1.enter();
 assertFalse(monitor2.enterIf(guard));
 }

 @Test
 public void testEnterIfWithTimeout() {
 Monitor monitor = new Monitor();
 long timeout = 1;
 Guard guard = new GuardMonitor(monitor);
 assertFalse(monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS));
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testEnterIfWithTimeoutAndLocked() throws InterruptedException {
 Monitor monitor = new Monitor();
 long timeout = 1;
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 assertFalse(monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterIf() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 assertFalse(monitor.tryEnterIf(guard));
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.enter();
 assertTrue(monitor.tryEnterIf(guard));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testTryEnterIfWithDifferentMonitor() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor(monitor1);
 monitor1.enter();
 assertFalse(monitor2.tryEnterIf(guard));
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 monitor.waitFor(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForWithDifferentMonitor() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Guard guard = new GuardMonitor(monitor1);
 monitor1.enter();
 monitor2.waitFor(guard); // should throw exception
 fail("Expected IllegalMonitorStateException");
 }

 @Test
 public void testWaitForUninterruptibly() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 monitor.waitForUninterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 long timeout = 1;
 Guard guard = new GuardMonitor(monitor);
 assertFalse(monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS));
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeoutAndLocked() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 long timeout = 1;
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 assertFalse(monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForWithInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 Thread interrupter = Thread.currentThread();
 monitor.waitFor(guard);
 interrupter.interrupt();
 assertFalse(Thread.interrupted());
 monitor.leave();
 assertTrue(Thread.interrupted());
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForWithInterruptedAndLocked() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 Thread interrupter = Thread.currentThread();
 monitor.waitFor(guard);
 interrupter.interrupt();
 interrupter.interrupt(); // should throw InterruptedException
 fail("Expected InterruptedException");
 }

 @Test
 public void testWaitForUninterruptiblyWithInterrupted() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 Thread interrupter = Thread.currentThread();
 monitor.waitForUninterruptibly(guard);
 interrupter.interrupt();
 assertFalse(Thread.interrupted());
 monitor.leave();
 assertTrue(Thread.interrupted());
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForUninterruptiblyWithInterruptedAndLocked() throws InterruptedException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 Thread interrupter = Thread.currentThread();
 monitor.waitForUninterruptibly(guard);
 interrupter.interrupt();
 interrupter.interrupt(); // should throw InterruptedException
 fail("Expected InterruptedException");
 }

 @Test
 public void testWaitForWithTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 long timeout = 1;
 assertTrue(monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS));
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testWaitForWithTimeoutAndLocked() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 long timeout = 1;
 monitor.enter();
 assertTrue(monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS));
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = TimeoutException.class)
 public void testWaitForWithTimeoutAndLockedAndTimeoutExpired() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 long timeout = 1;
 monitor.enter();
 monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testIsFair() {
 Monitor monitor1 = new Monitor();
 assertTrue(monitor1.isFair());
 Monitor monitor2 = new Monitor(true);
 assertTrue(monitor2.isFair());
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
 assertFalse(monitor.isOccupied());
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
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 Monitor monitor = new Monitor();
 Guard guard = new GuardMonitor(monitor);
 monitor.enter();
 monitor.enterWhen(guard);
 assertTrue(monitor.hasQueuedThreads(guard));
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads(guard));
 }

 @Test
 public void testHasQueuedThread() {
 Monitor monitor = new Monitor();
 Guard guard1 = new GuardMonitor(monitor);
 Guard guard2 = new GuardMonitor(monitor);
 monitor.enter();
 monitor.enterWhen(guard1);
 monitor.enterWhen(guard2);
 assertTrue(monitor.hasQueuedThread(Thread.currentThread()));
 monitor.leave();
 assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
 }
}

 class GuardMonitor extends Guard {
 private Monitor monitor;

 public GuardMonitor(Monitor monitor) {
 this.monitor = monitor;
 }

 @Override
 public boolean isSatisfied() {
 return true;
 }
}