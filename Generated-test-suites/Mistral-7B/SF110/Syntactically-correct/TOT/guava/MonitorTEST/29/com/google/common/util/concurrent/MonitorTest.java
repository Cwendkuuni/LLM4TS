import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor.Guard;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MonitorTest {

 private Monitor monitor;
 private Guard guard1, guard2;

 @Before
 public void setup() {
 monitor = new Monitor();
 guard1 = monitor.newGuard();
 guard2 = monitor.newGuard();
 }

 @Test
 public void testEnter() {
 monitor.enter();
 Assert.assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 Assert.assertFalse(monitor.isOccupiedByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterInterruptiblyInterrupted() throws InterruptedException {
 monitor.enterInterruptibly();
 Thread.currentThread().interrupt();
 monitor.leave();
 }

 @Test
 public void testEnterInterruptiblyNoInterruption() throws InterruptedException {
 monitor.enterInterruptibly();
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testEnterWithInterruption() throws InterruptedException {
 monitor.enter();
 Thread.currentThread().interrupt();
 monitor.leave();
 }

 @Test
 public void testEnterWhenSatisfied() {
 monitor.enterWhen(guard1);
 Assert.assertTrue(guard1.isSatisfied());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenUnsatisfied() {
 monitor.enterWhen(guard2);
 Assert.assertFalse(guard2.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithInterruption() throws InterruptedException {
 monitor.enterWhen(guard1);
 Thread.currentThread().interrupt();
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithTimeOut() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.enterWhen(guard, 100, TimeUnit.MILLISECONDS);
 Thread.sleep(110);
 Assert.assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testEnterWhenWithInterruptionAndTimeOut() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.enterWhen(guard, 100, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 Thread.sleep(110);
 Assert.assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testEnterIfSatisfied() {
 monitor.enterIf(guard1);
 Assert.assertTrue(guard1.isSatisfied());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterIfWithUnsatisfiedGuard() {
 monitor.enterIf(guard2);
 monitor.leave();
 }

 @Test
 public void testEnterIfWithInterruption() throws InterruptedException {
 monitor.enterIf(guard1);
 Thread.currentThread().interrupt();
 monitor.leave();
 }

 @Test
 public void testEnterIfWithTimeOut() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.enterIf(guard, 100, TimeUnit.MILLISECONDS);
 Thread.sleep(110);
 Assert.assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testEnterIfWithInterruptionAndTimeOut() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.enterIf(guard, 100, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 Thread.sleep(110);
 Assert.assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testTryEnterIfSatisfied() {
 monitor.tryEnterIf(guard1);
 Assert.assertTrue(guard1.isSatisfied());
 monitor.leave();
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testTryEnterIfWithUnsatisfiedGuard() {
 monitor.tryEnterIf(guard2);
 }

 @Test
 public void testTryEnterIfWithInterruption() throws InterruptedException {
 monitor.tryEnterIf(guard1);
 Thread.currentThread().interrupt();
 monitor.leave();
 }

 @Test
 public void testTryEnterIfWithTimeOut() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.tryEnterIf(guard, 100, TimeUnit.MILLISECONDS);
 Thread.sleep(110);
 Assert.assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testTryEnterIfWithInterruptionAndTimeOut() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.tryEnterIf(guard, 100, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 Thread.sleep(110);
 Assert.assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.waitFor(guard1);
 Assert.assertTrue(guard1.isSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForInterrupted() throws InterruptedException {
 monitor.waitFor(guard1);
 Thread.currentThread().interrupt();
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.waitForUninterruptibly(guard1);
 Assert.assertTrue(guard1.isSatisfied());
 monitor.leave();
 }

 @Test(expected = InterruptedException.class)
 public void testWaitForUninterruptiblyInterrupted() throws InterruptedException {
 monitor.waitForUninterruptibly(guard1);
 Thread.currentThread().interrupt();
 }

 @Test
 public void testWaitForWithTimeOut() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.waitFor(guard, 100, TimeUnit.MILLISECONDS);
 Thread.sleep(110);
 Assert.assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testWaitForWithTimeOutSatisfied() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.waitFor(guard, 100, TimeUnit.MILLISECONDS);
 Assert.assertTrue(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testWaitForWithInterruptionAndTimeOut() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.waitFor(guard, 100, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 Thread.sleep(110);
 Assert.assertFalse(guard.isSatisfied());
 monitor.leave();
 }

 @Test
 public void testWaitForWithInterruptionAndTimeOutSatisfied() throws InterruptedException, TimeoutException {
 Monitor monitor = new Monitor();
 Guard guard = monitor.newGuard();
 monitor.waitFor(guard, 100, TimeUnit.MILLISECONDS);
 Thread.currentThread().interrupt();
 Assert.assertTrue(guard.isSatisfied());
 monitor.leave();
 }
}