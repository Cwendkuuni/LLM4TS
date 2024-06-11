package com.google.common.util.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.MoreObjects;
import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor$Guard;

/**
 * Unit tests for {@link Monitor} class.
 */
public class MonitorTest {

 @Test
 public void testMonitorEnterAndLeave() {
 Monitor monitor = new Monitor();
 Monitor$Guard guard = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 monitor.enter();
 monitor.leave();

 // Verify that monitor is not locked
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testMonitorEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor$Guard guard = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 monitor.enterInterruptibly();
 monitor.leave();

 // Verify that monitor is not locked
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testMonitorEnterWithTimeOut() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor$Guard guard = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 long timeout = 100;
 boolean entered = monitor.enter(timeout, TimeUnit.MILLISECONDS);
 assertTrue(entered);
 monitor.leave();

 // Verify that monitor is not locked
 assertFalse(monitor.isOccupied());
 }

 @Test(expected = InterruptedException.class)
 public void testMonitorEnterInterruptiblyWithInterruption() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor$Guard guard = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.leave();
 }

 @Test
 public void testMonitorEnterWithCondition() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor$Guard guard1 = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 Monitor$Guard guard2 = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 monitor.enterWhen(guard1);
 monitor.enterWhen(guard2);
 monitor.leave();

 // Verify that monitor is not locked
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testMonitorEnterWithConditionAndTimeOut() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor$Guard guard1 = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 Monitor$Guard guard2 = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 long timeout = 100;
 monitor.enterWhen(guard1, timeout, TimeUnit.MILLISECONDS);
 // Timeout should have occurred, monitor should not be locked
 assertFalse(monitor.isOccupied());

 monitor.enterWhen(guard2);
 monitor.leave();
 }

 @Test
 public void testMonitorEnterWithConditionAndInterruption() throws InterruptedException {
 Monitor monitor = new Monitor();
 Monitor$Guard guard1 = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 Monitor$Guard guard2 = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 monitor.enterWhen(guard1);
 Thread.currentThread().interrupt();
 monitor.leave();

 // Monitor should not be locked
 assertFalse(monitor.isOccupied());

 monitor.enterWhen(guard2);
 monitor.leave();
 }

}