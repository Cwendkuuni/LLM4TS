package com.google.common.util.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;
import org.junit.*;
import org.mockito.*;

public class MonitorTest {

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testEnter() {
 Monitor monitor = new Monitor();
 MonitorSpy monitorSpy = spy(Monitor.class);
 doAnswer(invocation -> {
 monitorSpy.enter();
 return null;
 }).when(monitorSpy).enter();

 MonitorGuard guard = new MonitorGuard(monitor);
 monitorSpy.enter();
 assertTrue(guard.isSatisfied());
 verify(monitorSpy).enter();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithDifferentMonitor() {
 Monitor monitor = new Monitor();
 Monitor monitor1 = mock(Monitor.class);
 MonitorGuard guard = new MonitorGuard(monitor);
 monitor1.enter(); // another thread enters the monitor
 monitor.enter();
 }

 @Test
 public void testEnterInterruptiblyWithSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 MonitorGuard guard = new MonitorGuard(monitor);
 monitor.enterInterruptibly();
 assertTrue(guard.isSatisfied());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyWithInterruption() throws InterruptedException {
 Monitor monitor = new Monitor();
 MonitorGuard guard = new MonitorGuard(monitor);
 Thread thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 fail("Should have been interrupted");
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 }
 });
 thread.start();
 thread.join();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 assertTrue(guard.isInterrupted());
 }

 @Test
 public void testEnterWithGuardAndSuccess() throws InterruptedException {
 Monitor monitor = new Monitor();
 MonitorGuard guard = new MonitorGuard(monitor);
 monitor.enterWhen(guard);
 assertTrue(guard.isSatisfied());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithGuardAndDifferentMonitor() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor monitor1 = mock(Monitor.class);
 MonitorGuard guard = new MonitorGuard(monitor);
 monitor1.enter(); // another thread enters the monitor
 monitor.enterWhen(guard);
 }

 @Test
 public void testEnterWithGuardAndFalseCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 MonitorGuard guard = new MonitorGuard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard);
 assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithGuardAndInterruptedCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 MonitorGuard guard = new MonitorGuard(monitor) {
 @Override
 public boolean isSatisfied() {
 throw new InterruptedException();
 }
 };
 Thread thread = new Thread(() -> {
 try {
 monitor.enterWhen(guard);
 fail("Should have been interrupted");
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 }
 });
 thread.start();
 thread.join();
 monitor.enterWhen(guard);
 }

 @Test
 public void testEnterWithGuardAndTimeout() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 MonitorGuard guard = new MonitorGuard(monitor);
 monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
 assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithGuardAndTimeoutInterrupted() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 MonitorGuard guard = new MonitorGuard(monitor);
 Thread thread = new Thread(() -> {
 try {
 monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
 fail("Should have been interrupted");
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 } catch (TimeoutException e) {
 Thread.currentThread().interrupt();
 }
 });
 thread.start();
 thread.join();
 monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testEnterWithGuardAndTimeoutAndSatisfaction() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();MonitorMonitorMonitorGuard guard = new MonitorGuard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard, 10, TimeUnit.MILLISECONDS);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }
}