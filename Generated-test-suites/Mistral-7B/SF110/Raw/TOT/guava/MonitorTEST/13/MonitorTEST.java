package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Guard;
import com.google.common.util.concurrent.Monitor;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.Ignore;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

@Category(IntegrationTests.class)
public class MonitorTest {

 private Monitor monitor;
 private ReentrantLock reentrantLock;
 private Guard guard;

 @Before
 public void setUp() {
 monitor = new Monitor();
 reentrantLock = new ReentrantLock();
 guard = new AllowPassingGuard(monitor);
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyThrowsException() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.enterInterruptibly();
 monitor.leave();
 monitor.leave();
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 }

 @Test
 public void testEnterInterruptiblySucceedsWhenLockIsNotHeld() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.leave();
 monitor.enterInterruptibly();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyFailsWhenLockIsHeldByCurrentThread() throws InterruptedException {
 monitor.enter();
 monitor.enterInterruptibly();
 monitor.leave();
 monitor.leave();
 }

 @Test
 public void testEnterIfSucceedsWhenGuardIsSatisfied() {
 monitor.enterWhen(guard);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfThrowsExceptionWhenGuardIsNotMonitored() {
 Guard notMonitoredGuard = new NotMonitoredGuard(monitor);
 monitor.enterWhen(notMonitoredGuard);
 }

 @Test
 public void testEnterIfFailsWhenGuardIsNotSatisfied() {
 monitor.enterWhen(guard);
 assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyFailsWhenGuardIsNotSatisfiedAndInterrupted() throws InterruptedException {
 monitor.enterWhen(guard);
 Thread.currentThread().interrupt();
 monitor.enterIfInterruptibly(guard);
 }

 @Test
 public void testEnterIfInterruptiblySucceedsWhenGuardIsSatisfied() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.leave();
 monitor.enterIfInterruptibly(guard);
 }

 @Ignore
 @Test(expected = InterruptedException.class)
 public void testEnterWhenThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.leave();
 Thread.currentThread().interrupt();
 monitor.enterWhen(guard);
 }

 @Test
 public void testEnterWhenSucceedsWhenGuardIsSatisfied() {
 monitor.enterWhen(guard);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenThrowsExceptionWhenGuardIsNotMonitored() {
 Guard notMonitoredGuard = new NotMonitoredGuard(monitor);
 monitor.enterWhen(notMonitoredGuard);
 }

 @Test
 public void testEnterWhenFailsWhenGuardIsNotSatisfied() {
 monitor.enterWhen(guard);
 assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithTimeSucceedsWhenGuardIsSatisfied() throws InterruptedException {
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenWithTimeFailsWhenGuardIsNotSatisfiedAndInterrupted() throws InterruptedException {
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
 Thread.currentThread().interrupt();
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
 }

 @Test
 public void testEnterWhenWithTimeSucceedsWhenGuardIsSatisfiedAndLockIsHeldByCurrentThread() throws InterruptedException {
 monitor.enterWhen(guard);
 monitor.leave();
 reentrantLock.lock();
 monitor.enterWhen(guard, 1, TimeUnit.SECONDS);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 reentrantLock.unlock();
 }

 @Test
 public void testEnterWhenUninterruptibilitySucceedsWhenGuardIsSatisfied() {
 monitor.enterWhenUninterruptibly(guard);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenUninterruptiblyThrowsExceptionWhenGuardIsNotMonitored() {
 Guard notMonitoredGuard = new NotMonitoredGuard(monitor);
 monitor.enterWhenUninterruptibly(notMonitoredGuard);
 }

 @Test
 public void testEnterWhenUninterruptiblyFailsWhenGuardIsNotSatisfied() {
 monitor.enterWhenUninterruptibly(guard);
 assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenUninterruptiblyThrowsInterruptedExceptionWhenInterrupted() throws InterruptedException {
 monitor.enterWhenUninterruptibly(guard);
 Thread.currentThread().interrupt();
 monitor.enterWhenUninterruptibly(guard);
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfUninterruptiblyThrowsInterruptedExceptionWhenGuardIsNotSatisfiedAndInterrupted() throws InterruptedException {
 monitor.enterIfUninterruptibly(guard);
 Thread.currentThread().interrupt();
 monitor.enterIfUninterruptibly(guard);
 }

 @Test
 public void testEnterIfUninterruptiblySucceedsWhenGuardIsSatisfied() throws InterruptedException {
 monitor.enterIfUninterruptibly(guard);
 monitor.leave();
 monitor.enterIfUninterruptibly(guard);
 }

 @Test
 public void testTryEnterSucceedsWhenLockIsNotHeld() {
 monitor.tryEnter();
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testTryEnterFailsWhenLockIsHeldByCurrentThreadAndInterrupted() throws InterruptedException {
 monitor.enter();
 Thread.currentThread().interrupt();
 monitor.tryEnter();
 }

 @Test
 public void testTryEnterFailsWhenLockIsHeldByAnotherThread() {
 monitor.enter();
 reentrantLock.lock();
 monitor.tryEnter();
 Thread.currentThread().interrupt();
 monitor.leave();
 reentrantLock.unlock();
 monitor.tryEnter();
 }

 @Test
 public void testTryEnterSucceedsWhenLockIsNotHeldAndGuardIsSatisfied() {
 monitor.enterWhen(guard);
 monitor.leave();
 monitor.tryEnterIf(guard);
 assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testTryEnterFailsWhenLockIsNotHeldButGuardIsNotSatisfied() {
 monitor.enterWhen(guard);
 monitor.leave();
 monitor.tryEnterIf(guard);
 assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testTryEnterFailsWhenLockIsHeldByAnotherThread() {
 monitor.enter();
 reentrantLock.lock();
 monitor.tryEnter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 reentrantLock.unlock();
 monitor.tryEnter();
 }
}