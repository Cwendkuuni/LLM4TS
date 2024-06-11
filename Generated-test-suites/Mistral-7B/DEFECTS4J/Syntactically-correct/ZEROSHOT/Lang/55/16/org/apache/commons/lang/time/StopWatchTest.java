import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for StopWatch class.
 */
public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long time = stopWatch.getTime();
 Assert.assertNotEquals(-1, time);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSplitAndUnsplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 Assert.assertNotEquals(-1, splitTime);
 stopWatch.unsplit();
 long totalTime = stopWatch.getTime();
 Assert.assertEquals(splitTime, totalTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitBeforeStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 long currentTime = System.currentTimeMillis();
 stopWatch.resume();
 long elapsedTime = stopWatch.getTime();
 Assert.assertEquals(currentTime - stopWatch.getStartTime(), elapsedTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendBeforeRun() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.suspend();
 }
}