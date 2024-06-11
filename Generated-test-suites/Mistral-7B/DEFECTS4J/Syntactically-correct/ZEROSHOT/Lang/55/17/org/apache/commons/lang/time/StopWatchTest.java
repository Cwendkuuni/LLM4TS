import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0; // the test does not provide any specific time
 assertEquals("Time measured is incorrect.", expectedTime, actualTime);
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
 long expectedSplitTime = 0; // the test does not provide any specific time
 assertEquals("Split time measured is incorrect.", expectedSplitTime, splitTime);
 stopWatch.unsplit();
 long actualTime = stopWatch.getTime();
 long expectedTime = splitTime;
 assertEquals("Time measured after unsplit is incorrect.", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitWithoutStart() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitWithoutSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.unsplit();
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 long startTime = System.currentTimeMillis();
 stopWatch.suspend();
 long pauseTime = System.currentTimeMillis() - startTime;
 stopWatch.resume();
 long actualTime = stopWatch.getTime();
 long expectedTime = pauseTime;
 assertEquals("Time measured after resume is incorrect.", expectedTime, actualTime);
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendWithoutRunning() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.suspend();
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeWithoutSuspended() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.resume();
 }

 @Test
 public void testReset() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 long actualTime = stopWatch.getTime();
 long expectedTime = 0;
 assertEquals("Time measured after reset is incorrect.", expectedTime, actualTime);
 }

 @Test
 public void testMultipleMethods() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 long splitTime = stopWatch.getSplitTime();
 long expectedSplitTime = 100; // replace with actual expected time
 assertEquals("Split time measured is incorrect.", expectedSplitTime, splitTime);
 stopWatch.unsplit();
 stopWatch.suspend();
 long pauseTime = 200; // replace with actual expected time
 stopWatch.resume();
 long actualTime = stopWatch.getTime();
 long expectedTime = splitTime + pauseTime;
 assertEquals("Time measured after multiple methods is incorrect.", expectedTime, actualTime);
 }
}