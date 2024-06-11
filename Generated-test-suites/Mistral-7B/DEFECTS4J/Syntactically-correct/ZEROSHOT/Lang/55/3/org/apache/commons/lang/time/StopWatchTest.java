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
 long actual = stopWatch.getTime();
 Assert.assertNotEquals(-1, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testStartSplitStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 long actual = stopWatch.getTime();
 Assert.assertNotEquals(-1, actual);
 long splitTime = stopWatch.getSplitTime();
 Assert.assertNotEquals(-1, splitTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.stop();
 long actual = stopWatch.getTime();
 Assert.assertNotEquals(-1, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitWithoutSplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.unsplit();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 long pauseTime = System.currentTimeMillis() - stopWatch.getTime();
 Assert.assertNotEquals(0, pauseTime);
 stopWatch.resume();
 stopWatch.stop();
 long actual = stopWatch.getTime();
 Assert.assertNotEquals(-1, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeWithoutSuspend() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.resume();
 }

 @Test
 public void testReset() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 stopWatch.reset();
 Assert.assertEquals(-1, stopWatch.getTime());
 Assert.assertEquals(-1, stopWatch.getSplitTime());
 }
}