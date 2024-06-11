import com.google.common.util.concurrent.Monitor;
import com.google.common.util.concurrent.Monitor.Guard;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MonitorTest {

 private Monitor monitor;
 private Guard guard;
 private Thread thread;

 @Before
 public void setUp() {
 monitor = new Monitor();
 guard = monitor.newGuard();
 thread = new Thread(() -> {});
 }

 @Test
 public void testEnter() {
 monitor.enter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterInterruptibly() {
 monitor.enterInterruptibly();
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWithTimeOut() {
 monitor.enter();
 monitor.enterInterruptibly();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenGuardIsNotSatisfied() {
 monitor.enterWhen(guard);
 fail("Expected IllegalMonitorStateException");
 }

 @Test
 public void testEnterWhenGuardIsSatisfied() {
 monitor.enterWhen(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testEnterWhenInterruptiblyWithGuardIsSatisfied() {
 monitor.enterWhenInterruptibly(guard);
 assertTrue(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test(expected = IllegalMonitorStateException.class)
 public void testEnterWhenInterruptiblyWithGuardIsNotSatisfied() {
 monitor.enterWhenInterruptibly(guard);
 fail("Expected IllegalMonitorStateException");
 }

 @Test
 public void testTryEnter() {
 monitor.tryEnter();
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testTryEnterWithGuardIsSatisfied() {
 monitor.enter();
 assertTrue(monitor.tryEnter());
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testTryEnterWithGuardIsNotSatisfied() {
 monitor.enter();
 assertFalse(monitor.tryEnter());
 assertFalse(monitor.isOccupiedByCurrentThread());
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testWaitFor() {
 monitor.enter();
 guard.condition.signal();
 monitor.waitFor(guard);
 assertFalse(monitor.isOccupied());
 monitor.leave();
 }

 @Test
 public void testWaitForWithTimeOut() {
 monitor.enter();
 thread.start();
 try {
 monitor.waitFor(guard, 1, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException");
 } catch (InterruptedException e) {
 // expected
 } finally {
 monitor.leave();
 thread.join();
 }
 }

 @Test
 public void testWaitForUninterruptibly() {
 monitor.enter();
 guard.condition.signal();
 monitor.waitForUninterruptibly(guard);
 assertFalse(monitor.isOccupied());
 monitor.leave();
 }

 @Test
 public void testWaitForUninterruptiblyWithTimeOut() {
 monitor.enter();
 thread.start();
 try {
 monitor.waitForUninterruptibly(guard, 1, TimeUnit.MILLISECONDS);
 fail("Expected InterruptedException");
 } catch (InterruptedException e) {
 // expected
 } finally {
 monitor.leave();
 thread.join();
 }
 }

 @Test
 public void testLeave() {
 monitor.enter();
 monitor.leave();
 assertFalse(monitor.isOccupied());
 }

 @Test
 public void testIsFair() {
 assertTrue(monitor.isFair());
 }

 @Test
 public void testIsOccupied() {
 monitor.enter();
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
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getOccupiedDepth());
 }

 @Test
 public void testGetQueueLength() {
 monitor.enter();
 monitor.enter();
 monitor.enter();
 monitor.leave();
 monitor.leave();
 monitor.leave();
 assertEquals(0, monitor.getQueueLength());
 }

 @Test
 public void testHasQueuedThreads() {
 monitor.enter();
 Guard guard2 = monitor.newGuard();
 monitor.enterWhen(guard2);
 assertTrue(monitor.hasQueuedThreads(guard2));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThreads(guard2));
 }

 @Test
 public void testHasQueuedThread() {
 monitor.enter();
 Guard guard2 = monitor.newGuard();
 monitor.enterWhen(guard2);
 assertTrue(monitor.hasQueuedThread(thread));
 monitor.leave();
 monitor.leave();
 assertFalse(monitor.hasQueuedThread(thread));
 }
}