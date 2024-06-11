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
 Assert.assertNotEquals(-1, stopWatch.getTime());
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
 stopWatch.unsplit();
 Assert.assertNotEquals(-1, stopWatch.getTime());
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
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.stop();
 Assert.assertNotEquals(-1, stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendBeforeStart() {
 stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeBeforeSuspend() {
 stopWatch = new StopWatch();
 stopWatch.resume();
 }

 @Test
 public void testReset() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.reset();
 Assert.assertEquals(-1, stopWatch.getTime());
 }

 @Test
 public void testGetTimeAndSplitTime() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 Assert.assertNotEquals(-1, stopWatch.getTime());
 Assert.assertNotEquals(-1, stopWatch.getSplitTime());
 }

 @Test
 public void testToStringAndToSplitString() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 Assert.assertNotNull(stopWatch.toString());
 Assert.assertNotNull(stopWatch.toSplitString());
 }
}