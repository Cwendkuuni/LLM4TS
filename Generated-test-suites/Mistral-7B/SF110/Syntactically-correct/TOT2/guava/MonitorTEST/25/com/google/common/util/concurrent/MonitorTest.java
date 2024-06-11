import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor.Guard;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class MonitorTest {

 private Monitor monitor;
 private Lock lock;
 private Condition condition;
 private Guard guard;

 @Before
 public void setUp() {
 monitor = new Monitor();
 lock = monitor.lock;
 condition = monitor.newCondition();
 }

 @Test
 public void testEnter() {
 guard = monitor.newGuard();
 monitor.enter();
 lock.unlock();
 monitor.enterWhen(guard);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyThrowsExceptionIfNotHeldLock() throws InterruptedException {
 monitor.enterInterruptibly();
 }

 @Test
 public void testEnterWithTimeout() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.enter(timeout, TimeUnit.MILLISECONDS);
 lock.unlock();
 monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyThrowsInterruptedException() throws InterruptedException {
 monitor.enterInterruptibly();
 }

 @Test
 public void testTryEnter() {
 Assert.assertTrue(monitor.tryEnter());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenThrowsExceptionIfGuardIsNotMonitor() {
 Monitor otherMonitor = new Monitor();
 monitor.enterWhen(otherMonitor.newGuard());
 }

 @Test
 public void testEnterWhenWithTimeout() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.enterWhen(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWhenInterruptiblyThrowsInterruptedException() throws InterruptedException {
 monitor.enterWhenInterruptibly(guard);
 }

 @Test
 public void testEnterIf() {
 Guard ifGuard = monitor.newGuard();
 monitor.enterIf(ifGuard);
 if (monitor.isOccupiedByCurrentThread()) {
 monitor.leave();
 }
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfThrowsExceptionIfGuardIsNotMonitor() {
 Monitor otherMonitor = new Monitor();
 monitor.enterIf(otherMonitor.newGuard());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfThrowsExceptionIfNotHeldLock() {
 monitor.enterIf(guard);
 monitor.leave();
 monitor.enterIf(guard);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfThrowsExceptionIfConditionIsNotSatisfied() {
 monitor.enterIf(guard);
 }

 @Test
 public void testEnterIfWithTimeout() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.enterIf(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyThrowsInterruptedException() throws InterruptedException {
 monitor.enterIfInterruptibly(guard);
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeout() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.enterIfInterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfInterruptiblyThrowsExceptionIfConditionIsNotSatisfied() throws InterruptedException {
 monitor.enterIfInterruptibly(guard);
 monitor.leave();
 monitor.enterIfInterruptibly(guard);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfInterruptiblyThrowsExceptionIfNotHeldLock() throws InterruptedException {
 monitor.enterIfInterruptibly(guard);
 monitor.leave();
 monitor.enterIfInterruptibly(guard);
 }

 @Test
 public void testEnterIfTryLock() {
 monitor.enterIfTryLock(guard);
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfTryLockThrowsExceptionIfConditionIsNotSatisfied() throws InterruptedException {
 monitor.enterIfTryLock(guard);
 monitor.leave();
 monitor.enterIfTryLock(guard);
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.waitFor(guard);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForThrowsExceptionIfMonitorIsNotHeld() throws InterruptedException {
 monitor.waitFor(guard);
 monitor.leave();
 monitor.waitFor(guard);
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.waitForUninterruptibly(guard);
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForUninterruptiblyThrowsExceptionIfMonitorIsNotHeld() throws InterruptedException {
 monitor.waitForUninterruptibly(guard);
 monitor.leave();
 monitor.waitForUninterruptibly(guard);
 }

 @Test
 public void testWaitForConditionIsSatisfied() throws InterruptedException {
 monitor.waitFor(guard);
 guard.monitor = null;
 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 public void testWaitForWithTimeout() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test(expected = TimeoutException.class)
 public void testWaitForWithTimeoutThrowsTimeoutException() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 monitor.waitFor(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testWaitForUninterruptiblyWithConditionIsSatisfied() {
 monitor.waitForUninterruptibly(guard);
 guard.monitor = null;
 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeout() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.waitForUninterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForInterruptiblyThrowsInterruptedException() throws InterruptedException {
 monitor.waitForInterruptibly(guard);
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForInterruptiblyWithTimeoutThrowsInterruptedException() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.waitForInterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForInterruptiblyWithTimeoutThrowsInterruptedExceptionIfConditionIsNotSatisfied() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.waitForInterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 monitor.leave();
 monitor.waitForInterruptibly(guard, timeout, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testAwaitNanos() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.awaitNanos(guard, timeout);
 }

 @Test(expected = TimeoutException.class)
 public void testAwaitNanosThrowsTimeoutException() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.awaitNanos(guard, timeout);
 monitor.leave();
 monitor.awaitNanos(guard, timeout);
 }

 @Test(expected = InterruptedException.class)
 public void testAwaitNanosInterruptiblyThrowsInterruptedException() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.awaitNanos(guard, timeout);
 monitor.leave();
 monitor.awaitNanosInterruptibly(guard, timeout);
 }

 @Test(expected = InterruptedException.class)
 public void testAwaitNanosInterruptiblyThrowsInterruptedExceptionIfConditionIsNotSatisfied() throws InterruptedException, TimeoutException {
 long timeout = 10;
 monitor.awaitNanosInterruptibly(guard, timeout);
 monitor.leave();
 monitor.awaitNanosInterruptibly(guard, timeout);
 }

 @Test
 public void testLeave() {
 monitor.leave();
 }

 @Test
 public void testIsFair() {
 Monitor fairMonitor = new Monitor(true);
 Monitor unfairMonitor = new Monitor(false);
 Assert.assertTrue(fairMonitor.isFair());
 Assert.assertFalse(unfairMonitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 monitor.enter();
 Assert.assertTrue(monitor.isOccupied());
 monitor.leave();
 Assert.assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 monitor.enter();
 Assert.assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 Assert.assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 monitor.enter();
 monitor.enter();
 Assert.assertEquals(2, monitor.getOccupiedDepth());
 monitor.leave();
 monitor.leave();
 Assert.assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 monitor.leave();
 Assert.assertEquals(1, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.enter();
 Assert.assertTrue(monitor.hasQueuedThreads(condition));
 monitor.leave();
 Assert.assertFalse(monitor.hasQueuedThreads(condition));
 }

 @Test
 public void testHasQueuedThread() {
 Thread thread = new Thread();
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.enter();
 monitor.leave();
 Assert.assertTrue(monitor.hasQueuedThread(thread));
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testHasQueuedThreadThrowsExceptionIfMonitorIsNotHeld() {
 monitor.leave();
 Assert.assertFalse(monitor.hasQueuedThread(Thread.currentThread()));
 }
}