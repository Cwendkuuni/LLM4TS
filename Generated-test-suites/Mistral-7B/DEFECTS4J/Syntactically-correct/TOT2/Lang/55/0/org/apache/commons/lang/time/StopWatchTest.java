import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long time = stopWatch.getTime();
 Assert.assertGreaterThan(time, 0);
 }

 @Test
 public void testSplitAndUnsplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 Assert.assertGreaterThan(splitTime, 0);
 stopWatch.unsplit();
 long finalTime = stopWatch.getTime();
 Assert.assertEquals(splitTime, finalTime);
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 long pausedTime = System.currentTimeMillis() - stopWatch.getStartTime();
 stopWatch.resume();
 long resumedTime = stopWatch.getTime() - stopWatch.getStartTime();
 Assert.assertEquals(pausedTime, resumedTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalStartState() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.stop();
 stopWatch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalSplitState() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalSuspendState() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testIllegalResumeState() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 }

 @Test
 public void testReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.reset();
 long timeAfterReset = stopWatch.getTime();
 Assert.assertEquals(timeAfterReset, 0);
 }
}