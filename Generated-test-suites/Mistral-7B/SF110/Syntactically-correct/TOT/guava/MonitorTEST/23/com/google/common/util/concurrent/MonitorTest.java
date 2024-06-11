package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Monitor;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class MonitorTest {

 private static final Monitor MONITOR = new Monitor();
 private static final Condition condition = MONITOR.lock.newCondition();

 @Test
 public void testEnter() throws InterruptedException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 monitor.enterInterruptibly();
 assertTrue(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());

 Thread thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 fail();
 } catch (InterruptedException e) {
 // Expected
 }
 });
 thread.start();
 thread.join();

 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWithInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());

 Thread thread = new Thread(() -> {
 try {
 monitor.enterInterruptibly();
 fail();
 } catch (InterruptedException e) {
 monitor.enter();
 }
 });
 thread.start();
 thread.interrupt();
 thread.join();

 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());

 long timeout = 10L;
 boolean entered = monitor.enter(timeout, TimeUnit.MILLISECONDS);
 assertFalse(entered);
 assertFalse(monitor.isOccupied());
 assertFalse(monitor.isOccupiedByCurrentThread());

 Thread thread = new Thread(() -> {
 try {
 monitor.enter(timeout, TimeUnit.MILLISECONDS);
 fail();
 } catch (InterruptedException e) {
 // Expected
 } catch (TimeoutException e) {
 monitor.enter();
 }
 });
 thread.start();
 thread.interrupt();
 thread.join();

 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());

 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 monitor.enterWhen(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenWithInterrupt() throws InterruptedException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());

 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard);
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());

 Thread thread = new Thread(() -> {
 try {
 monitor.enterWhen(guard);
 fail();
 } catch (InterruptedException e) {
 monitor.enter();
 }
 });
 thread.start();
 thread.interrupt();
 thread.join();

 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenWithTimeout() throws InterruptedException {
 Monitor monitor = new Monitor();
 assertFalse(monitor.isOccupied());

 Guard guard = new Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());

 Thread thread = new Thread(() -> {
 try {
 monitor.enterWhen(guard, 1, TimeUnit.MILLISECONDS);
 fail();
 } catch (InterruptedException e) {
 monitor.enter();
 } catch (TimeoutException e) {
 monitor.enter();
 }
 });
 thread.start();
 thread.interrupt();
 thread.join();

 assertFalse(monitor.isOccupiedByCurrentThread());
 assertFalse(monitor.isOccupied());
 }
}