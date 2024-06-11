import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 private StopWatch stopWatch;

 @Test
 public void testStartAndStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0; // assume the test runs instantly
 Assert.assertEquals("The time should be zero as the test ran instantly", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 long expectedSplitTime = 0; // assume the test runs instantly
 Assert.assertEquals("The split time should be zero as the test ran instantly", expectedSplitTime, splitTime);
 stopWatch.unsplit();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0; // assume the test runs instantly after the split and unsplit
 Assert.assertEquals("The time should be zero as the test ran instantly after the split and unsplit", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitWithoutStart() {
 stopWatch = new StopWatch();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 long startTime = stopWatch.getTime();
 stopWatch.suspend();
 long pauseTime = System.currentTimeMillis() - startTime;
 stopWatch.resume();
 long actualTime = stopWatch.getTime();
 long expectedTime = startTime + pauseTime;
 Assert.assertEquals("The time should be the sum of the start time and the pause time", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendWithoutRun() {
 stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test
 public void testReset() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.reset();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0;
 Assert.assertEquals("The time should be zero after a reset", expectedTime, actualTime);
 }

 @Test
 public void testGetTimeWithRunningStopwatch() {
 stopWatch = new StopWatch();
 stopWatch.start();
 long actualTime = stopWatch.getTime();
 long expectedTime = System.currentTimeMillis() - stopWatch.getStartTime();
 Assert.assertEquals("The time should be the elapsed time since the stopwatch was started", expectedTime, actualTime);
 }

 @Test
 public void testToString() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Thread.yield();
 Thread.sleep(100);
 stopWatch.stop();
 String actualString = stopWatch.toString();
 String expectedString = "0:0:1.009"; // assume the test runs for 1.009 seconds
 Assert.assertEquals("The toString() method should return the time formatted as a string", expectedString, actualString);
 }

 @Test
 public void testToSplitStringWithSplitStopwatch() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Thread.yield();
 Thread.sleep(100);
 stopWatch.split();
 Thread.yield();
 Thread.sleep(150);
 stopWatch.unsplit();
 String actualString = stopWatch.toSplitString();
 String expectedString = "0:0:1.519"; // assume the split takes 1.519 seconds
 Assert.assertEquals("The toSplitString() method should return the split time formatted as a string", expectedString, actualString);
 }
}