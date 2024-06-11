import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

 private StopWatch stopWatch;

 @Before
 public void setUp() {
 stopWatch = new StopWatch();
 }

 @Test
 public void testStartAndStop() {
 stopWatch.start();
 stopWatch.stop();
 long actual = stopWatch.getTime();
 Assert.assertNotEquals(-1, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch.start();
 stopWatch.start();
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 Assert.assertNotEquals(-1, splitTime);
 stopWatch.unsplit();
 long actual = stopWatch.getTime();
 Assert.assertEquals(splitTime, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitBeforeStart() {
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch.start();
 stopWatch.suspend();
 long startTime = stopWatch.getTime();
 stopWatch.resume();
 long actual = stopWatch.getTime();
 Assert.assertEquals(startTime, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendBeforeRun() {
 stopWatch.suspend();
 }

 @Test
 public void testReset() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 stopWatch.reset();
 Assert.assertEquals(-1, stopWatch.getTime());
 }

 @Test
 public void testGetTime() {
 stopWatch.start();
 long time = stopWatch.getTime();
 Assert.assertEquals(-1, time);
 stopWatch.split();
 time = stopWatch.getTime();
 Assert.assertEquals(-1, time);
 stopWatch.stop();
 time = stopWatch.getTime();
 Assert.assertNotEquals(-1, time);
 }

 @Test
 public void testToString() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 String actual = stopWatch.toString();
 Assert.assertNotNull(actual);
 }

 @Test
 public void testToSplitString() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 String actual = stopWatch.toSplitString();
 Assert.assertNotNull(actual);
 }
}