package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor.Guard;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;

@Beta
public class MonitorTest {

 private Monitor monitor;
 private LockSupport lockSupport;
 private Condition condition;
 private Guard guard;

 @Before
 public void setUp() {
 monitor = new Monitor();
 lockSupport = new LockSupport();
 condition = new ConditionObject();
 guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return lockSupport.isSignaled();
 }
 };
 }

 @After
 public void tearDown() {
 monitor = null;
 lockSupport = null;
 condition = null;
 guard = null;
 }

 @Test
 public void testEnter() {
 monitor.enter();
 monitor.enter();
 monitor.enter();

 monitor.leave();
 monitor.leave();
 monitor.leave();

 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.enterInterruptibly();
 monitor.enterInterruptibly();

 Thread thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 }
 });
 thread.start();
 monitor.enterInterruptibly();
 thread.join();

 monitor.leave();
 monitor.leave();
 monitor.leave();

 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenWithSatisfiedCondition() throws InterruptedException {
 monitor.enterInterruptibly();
 lockSupport.signal();
 monitor.enterWhen(guard);

 monitor.leave();

 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenWithUnsatisfiedCondition() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.enterWhen(guard);

 Thread thread = new Thread(() -> monitor.leave());
 thread.start();
 thread.join();

 monitor.leave();

 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenWithTimeout() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);

 Thread thread = new Thread(() -> {
 try {
 monitor.enter();
 } finally {
 monitor.leave();
 }
 });
 thread.start();
 thread.join();

 monitor.leave();

 Assert.assertFalse(monitor.isOccupied());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithInvalidMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enterWhen(otherMonitor.guard);
 }
}