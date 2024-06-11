import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 Assert.assertNotEquals(-1, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testStartSplitStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 long actualSplitTime = stopWatch.getSplitTime();
 Assert.assertNotEquals(-1, actualSplitTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test
 public void testStartSuspendResumeStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 Assert.assertNotEquals(-1, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendBeforeStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test
 public void testReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.reset();
 long actualTime = stopWatch.getTime();
 Assert.assertEquals(-1, actualTime);
 }

 @Test
 public void testGetTimeAfterStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 long actualTime = stopWatch.getTime();
 Assert.assertTrue(actualTime > 0);
 }

 @Test
 public void testGetSplitTimeAfterSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long actualSplitTime = stopWatch.getSplitTime();
 Assert.assertTrue(actualSplitTime > 0);
 }

 @Test
 public void testToString() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 String actualToString = stopWatch.toString();
 Assert.assertNotNull(actualToString);
 }

 @Test
 public void testToSplitString() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 String actualToSplitString = stopWatch.toSplitString();
 Assert.assertNotNull(actualToSplitString);
 }

 @Test
 public void testIllegalStateException() {
 StopWatch stopWatch = new StopWatch();
 // Test illegal state exceptions for all methods
 stopWatch.start();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.unsplit();
 stopWatch.stop();
 stopWatch.reset();
 }
}