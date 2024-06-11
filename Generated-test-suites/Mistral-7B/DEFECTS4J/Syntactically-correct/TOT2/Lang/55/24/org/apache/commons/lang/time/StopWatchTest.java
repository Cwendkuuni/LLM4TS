import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 @Test
 public void testStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.getSplitState());
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitBeforeStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.split();
 }

 @Test
 public void testUnsplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitBeforeSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.unsplit();
 }

 @Test
 public void testSuspend() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
 }

 @Test
 public void testResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeBeforeSuspend() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.resume();
 }

 @Test
 public void testToString() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 Assert.assertEquals("00:00:01.000", stopWatch.toString());
 }

 @Test
 public void testGetTime() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 long time = stopWatch.getTime();
 stopWatch.stop();
 Assert.assertEquals(time, stopWatch.getTime());
 }

 @Test
 public void testGetSplitTime() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long time = stopWatch.getSplitTime();
 stopWatch.unsplit();
 Assert.assertEquals(time, stopWatch.getTime());
 }

 @Test
 public void testReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.reset();
 Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
 }
}