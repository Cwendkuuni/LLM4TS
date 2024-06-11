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
 Long actual = stopWatch.getTime();
 Long expected = 0L; // test case for empty timing
 Assert.assertEquals(expected, actual);

 stopWatch.reset();
 stopWatch.start();
 stopWatch.stop();
 actual = stopWatch.getTime();
 Long expectedTime = 1L; // assuming some minimal time difference between start and stop
 Assert.assertEquals(expectedTime, actual);
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
 stopWatch.stop();
 Long actual = stopWatch.getTime();
 Long expected = 0L; // test case for empty timing before split
 Long expectedSplitTime = 1L; // assuming some minimal time difference between start and split
 Assert.assertEquals(expected, actual);
 Assert.assertNotNull(stopWatch.getSplitTime());
 Assert.assertEquals(expectedSplitTime, stopWatch.getSplitTime());

 stopWatch.reset();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.stop();
 actual = stopWatch.getTime();
 expectedTime = 2 * expectedSplitTime; // assuming the same timing before and after unsplit
 Assert.assertEquals(expectedTime, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 long currentTime = System.currentTimeMillis();
 stopWatch.suspend();
 // suspend for some time
 long suspendedTime = System.currentTimeMillis() - currentTime;
 currentTime = System.currentTimeMillis();
 stopWatch.resume();
 stopWatch.stop();
 Long actual = stopWatch.getTime();
 Long expectedTime = stopWatch.getTime() - suspendedTime;
 Assert.assertEquals(expectedTime, actual);
 }
}