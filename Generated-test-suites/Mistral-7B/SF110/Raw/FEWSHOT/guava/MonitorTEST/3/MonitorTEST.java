package com.google.common.util.concurrent;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.annotations.*;
import com.google.common.base.*;
import com.google.common.util.concurrent.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import org.junit.Test;

@Beta
public class MonitorTest {

 final Monitor monitor = new Monitor();
 final Condition condition = monitor.lock.newCondition();
 final Guard guard = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };

 @Test
 public void testEnter() {
 monitor.enter();
 monitor.leave();
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.leave();
 }

 @Test
 public void testEnterWithGuard() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.leave();
 }

 @Test
 public void testEnterUninterruptiblyWithGuard() {
 monitor.enterWhenUninterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testEnterWithGuardAndTime() throws InterruptedException {
 long timeout = 50;
 boolean success = monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 assertTrue(success);
 }

 @Test
 public void testEnterUninterruptiblyWithGuardAndTime() {
 long timeout = 50;
 monitor.enterWhenUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test
 public void testTryEnter() {
 assertTrue(monitor.tryEnter());
 monitor.leave();
 }

 @Test
 public void testTryEnterWithGuard() {
 assertTrue(monitor.enterIf(guard));
 monitor.leave();
 }

 @Test
 public void testTryEnterWithGuardAndTime() {
 long timeout = 50;
 assertTrue(monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS));
 monitor.leave();
 }

 @Test
 public void testTryEnterWithGuardInterruptibly() throws InterruptedException {
 assertThrows(InterruptedException.class, () -> monitor.enterIfInterruptibly(guard));
 monitor.leave();
 }

 @Test
 public void testTryEnterWithGuardAndTimeInterruptibly() throws InterruptedException {
 long timeout = 50;
 assertThrows(InterruptedException.class, () -> monitor.enterIfInterruptibly(guard, timeout, TimeUnit.MILLISECONDS));
 monitor.leave();
 }

 @Test
 public void testTryEnterWithGuardInterruptiblyWithTime() throws InterruptedException {
 long timeout = 50;
 assertThrows(InterruptedException.class, () -> monitor.tryEnterIf(guard, timeout, TimeUnit.MILLISECONDS));
 monitor.leave();
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.waitFor(guard);
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.waitForUninterruptibly(guard);
 monitor.leave();
 }

 @Test
 public void testWaitForWithTime() throws InterruptedException {
 long timeout = 50;
 monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptiblyWithTime() {
 long timeout = 50;
 monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 }

 @Test
 public void testIsFair() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor(true);
 assertThat(monitor1.isFair()).isFalse();
 assertThat(monitor2.isFair()).isTrue();
 }

 @Test
 public void testIsOccupied() {
 monitor.enter();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertTrue(monitor.isOccupied());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 monitor.enter();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 monitor.enter();
 monitor.enter();
 monitor.enter();
 assertThat(monitor.getOccupiedDepth()).isEqualTo(3);
 monitor.leave();
 monitor.leave();
 monitor.leave();
 assertThat(monitor.getOccupiedDepth()).isEqualTo(0);
 }

 @Test
 public void testGetQueueLength() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enter();
 monitor1.leave();
 monitor2.leave();
 Monitor monitor3 = new Monitor();
 monitor3.enter();
 monitor3.leave();
 Monitor monitor4 = new Monitor();
 monitor4.enter();
 Monitor monitor5 = new Monitor();
 monitor5.enter();
 Monitor monitor6 = new Monitor();
 monitor6.enter();
 assertThat(monitor1.getQueueLength()).isEqualTo(0);
 assertThat(monitor2.getQueueLength()).isEqualTo(0);
 assertThat(monitor3.getQueueLength()).isEqualTo(0);
 assertThat(monitor4.getQueueLength()).isEqualTo(0);
 assertThat(monitor5.getQueueLength()).isEqualTo(1);
 assertThat(monitor6.getQueueLength()).isEqualTo(1);
 }

 @Test
 public void testHasQueuedThreads() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enter();
 monitor1.leave();
 monitor2.leave();
 Monitor monitor3 = new Monitor();
 monitor3.enter();
 Monitor monitor4 = new Monitor();
 monitor4.enter();
 Monitor monitor5 = new Monitor();
 monitor5.enter();
 Monitor monitor6 = new Monitor();
 monitor6.enter();
 Monitor monitor7 = new Monitor();
 monitor7.enter();
 assertThat(monitor1.hasQueuedThreads(guard)).isFalse();
 assertThat(monitor2.hasQueuedThreads(guard)).isFalse();
 assertThat(monitor3.hasQueuedThreads(guard)).isFalse();
 assertThat(monitor4.hasQueuedThreads(guard)).isFalse();
 assertThat(monitor5.hasQueuedThreads(guard)).isFalse();
 assertThat(monitor6.hasQueuedThreads(guard)).isFalse();
 assertThat(monitor7.hasQueuedThreads(guard)).isTrue();
 }

 @Test
 public void testHasQueuedThread() {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enter();
 monitor1.leave();
 monitor2.leave();
 Monitor monitor3 = new Monitor();
 monitor3.enter();
 Monitor thread = Thread.currentThread();
 monitor3.enter();
 monitor3.leave();
 Monitor monitor4 = new Monitor();
 monitor4.enter();
 Monitor monitor5 = new Monitor();
 monitor5.enter();
 Monitor monitor6 = new Monitor();
 monitor6.enter();
 Monitor monitor7 = new Monitor();
 monitor7.enter();
 assertThat(monitor1.hasQueuedThread(thread)).isFalse();
 assertThat(monitor2.hasQueuedThread(thread)).isFalse();
 assertThat(monitor3.hasQueuedThread(thread)).isTrue();
 assertThat(monitor4.hasQueuedThread(thread)).isFalse();
 assertThat(monitor5.hasQueuedThread(thread)).isFalse();
 assertThat(monitor6.hasQueuedThread(thread)).isFalse();
 assertThat(monitor7.hasQueuedThread(thread)).isFalse();
 }

 @Test
 public void testIsSatisfied() throws InterruptedException {
 Guard guard1 = new Monitor.Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 monitor1.enter();
 monitor2.enterWhen(guard1);
 monitor1.leave();
 monitor2.leave();
 assertFalse(monitor1.isSatisfied(guard1));
 }

 @Test
 public void testSignalNextWaiter() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Monitor monitor3 = new Monitor();
 Monitor monitor4 = new Monitor();
 monitor1.enter();
 monitor2.enterWhen(guard);
 monitor3.enterWhen(guard);
 monitor4.enterWhen(guard);
 monitor1.signalNextWaiter();
 monitor2.leave();
 monitor3.leave();
 monitor4.leave();
 monitor1.leave();
 }

 @Test
 public void testSignalAllWaiters() throws InterruptedException {
 Monitor monitor1 = new Monitor();
 Monitor monitor2 = new Monitor();
 Monitor monitor3 = new Monitor();
 Monitor monitor4 = new Monitor();
 monitor1.enter();
 monitor2.enterWhen(guard);
 monitor3.enterWhen(guard);
 monitor4.enterWhen(guard);
 monitor1.signalAllWaiters();
 monitor2.leave();
 monitor3.leave();
 monitor4.leave();
 monitor1.leave();
 }
}