import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor$Guard;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import java.util.concurrent.locks.ReentrantLock;
import static org.mockito.Mockito.*;

public class MonitorTest {

 private Monitor monitor;
 private ReentrantLock mockLock;
 private Monitor$Guard guard;

 @Before
 public void setup() {
 monitor = new Monitor();
 mockLock = mock(ReentrantLock.class);
 when(mockLock.tryLock()).thenReturn(true);
 when(mockLock.isHeldByCurrentThread()).thenReturn(false);
 guard = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return true;
 }
 };
 }

 @Test
 public void testEnter() {
 monitor.enter();
 Assert.assertTrue(mockLock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWithInvalidMonitor() {
 Monitor otherMonitor = new Monitor();
 monitor.enter();
 otherMonitor.enter();
 monitor.enter(); // should throw an exception
 }

 @Test
 public void testEnterInterruptibly() throws InterruptedException {
 monitor.enterInterruptibly();
 Assert.assertTrue(mockLock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterInterruptiblyWithInterruptedThread() throws InterruptedException {
 Thread.currentThread().interrupt();
 monitor.enterInterruptibly();
 }

 @Test
 public void testTryEnter() {
 Assert.assertTrue(monitor.tryEnter());
 }

 @Test
 public void testTryEnterWithOccupiedMonitor() {
 monitor.enter();
 Assert.assertFalse(monitor.tryEnter());
 }

 @Test
 public void testEnterWhen() throws InterruptedException {
 monitor.enterWhen(guard);
 Assert.assertTrue(mockLock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenWithInvalidMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.enterWhen(guard);
 otherMonitor.enter();
 monitor.enterWhen(guard); // should throw an exception
 }

 @Test
 public void testEnterWhenUninterruptibly() {
 monitor.enterWhenUninterruptibly(guard);
 Assert.assertTrue(mockLock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterWhenUninterruptiblyWithTimeout() {
 long timeout = 1;
 Monitor otherMonitor = new Monitor();
 otherMonitor.enter();
 monitor.enterWhenUninterruptibly(guard, timeout);
 Assert.assertFalse(mockLock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterIf() {
 monitor.enterIf(guard);
 Assert.assertTrue(mockLock.isHeldByCurrentThread());
 }

 @Test
 public void testEnterIfWithOccupiedMonitor() {
 monitor.enter();
 Assert.assertFalse(monitor.enterIf(guard));
 }

 @Test
 public void testEnterIfInterruptibly() throws InterruptedException {
 monitor.enterIfInterruptibly(guard);
 Assert.assertTrue(mockLock.isHeldByCurrentThread());
 }

 @Test(expected = InterruptedException.class)
 public void testEnterIfInterruptiblyWithInterruptedThread() throws InterruptedException {
 Thread.currentThread().interrupt();
 monitor.enterIfInterruptibly(guard);
 }

 @Test
 public void testEnterIfWithTimeout() {
 long timeout = 1;
 Assert.assertTrue(monitor.enterIf(guard, timeout));
 }

 @Test
 public void testEnterIfWithTimeoutAndOccupiedMonitor() {
 monitor.enter();
 Assert.assertFalse(monitor.enterIf(guard, 1));
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeout() throws InterruptedException {
 long timeout = 1;
 Thread.currentThread().interrupt();
 Assert.assertFalse(monitor.enterIfInterruptibly(guard, timeout));
 }

 @Test
 public void testEnterIfInterruptiblyWithTimeoutAndHeldByCurrentThread() throws InterruptedException {
 monitor.enter();
 long timeout = 1;
 Thread.currentThread().interrupt();
 Assert.assertFalse(monitor.enterIfInterruptibly(guard, timeout));
 }

 @Test
 public void testTryEnterIf() {
 Assert.assertTrue(monitor.tryEnterIf(guard));
 }

 @Test
 public void testTryEnterIfWithOccupiedMonitor() {
 monitor.enter();
 Assert.assertFalse(monitor.tryEnterIf(guard));
 }

 @Test
 public void testWaitFor() throws InterruptedException {
 monitor.waitFor(guard);
 Assert.assertTrue(mockLock.isHeldByCurrentThread());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testWaitForWithInvalidMonitor() throws InterruptedException {
 Monitor otherMonitor = new Monitor();
 monitor.waitFor(guard);
 otherMonitor.enter();
 monitor.waitFor(guard); // should throw an exception
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.waitForUninterruptibly(guard);
 Assert.assertTrue(mockLock.isHeldByCurrentThread());
 }

 @Test
 public void testWaitForWithTimeout() throws InterruptedException {
 long timeout = 1;
 Assert.assertFalse(monitor.waitFor(guard, timeout));
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeout() {
 long timeout = 1;
 Assert.assertTrue(monitor.waitForUninterruptibly(guard, timeout));
 }

 @Test
 public void testWaitForWithTimeoutAndOccupiedMonitor() {
 monitor.enter();
 long timeout = 1;
 Assert.assertFalse(monitor.waitFor(guard, timeout));
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeoutAndOccupiedMonitor() {
 monitor.enter();
 long timeout = 1;
 Assert.assertFalse(monitor.waitForUninterruptibly(guard, timeout));
 }

 @Test
 public void testLeave() {
 monitor.leave();
 Assert.assertFalse(mockLock.isHeldByCurrentThread());
 }

 @Test
 public void testIsFair() {
 Assert.assertEquals(false, monitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 monitor.enter();
 Assert.assertEquals(true, monitor.isOccupied());
 }

 @Test
 public void testIsOccupiedByCurrentThread() {
 monitor.enter();
 Assert.assertEquals(true, monitor.isOccupiedByCurrentThread());
 }

 @Test
 public void testGetOccupiedDepth() {
 monitor.enter();
 monitor.enter();
 Assert.assertEquals(2, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 monitor.enter();
 monitor.enter();
 monitor.enter();
 Assert.assertEquals(3, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 monitor.enter();
 Assert.assertEquals(true, monitor.hasQueuedThreads(guard));
 }

 @Test
 public void testHasQueuedThread() {
 monitor.enter();
 Thread thread = new Thread(() -> {});
 monitor.enter();
 Assert.assertEquals(true, monitor.hasQueuedThread(thread));
 }

 @Test
 public void testHasWaiters() {
 monitor.enter();
 Guard guard = new Monitor$Guard(monitor) {
 @Override
 public boolean isSatisfied() {
 return false;
 }
 };
 monitor.waitFor(guard);
 Assert.assertEquals(true, monitor.hasWaiters(guard));
 }
}