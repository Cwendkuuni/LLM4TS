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
 long expectedTime = 0; // assume the test has been run in zero time
 assertEquals(expectedTime, actualTime);
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
 long splitTime1 = stopWatch.split();
 long splitTime2 = stopWatch.split(); // should throw an exception
 long actualTime1 = stopWatch.getSplitTime();
 long expectedTime1 = 0; // assume the first split has been recorded in zero time
 assertEquals(expectedTime1, actualTime1);
 stopWatch.unsplit();
 long actualTime2 = stopWatch.getTime();
 long expectedTime2 = 0; // assume the test has been run in zero time after unsplit
 assertEquals(expectedTime2, actualTime2);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsplitBeforeSplit() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.unsplit();
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.start();
 long suspendTime = stopWatch.suspend();
 long resumeTime = stopWatch.resume();
 long actualTime1 = stopWatch.getTime();
 long expectedTime1 = 0; // assume the test has been run in zero time before suspend
 assertEquals(expectedTime1, actualTime1);
 long actualTime2 = stopWatch.getTime();
 long expectedTime2 = suspendTime - expectedTime1;
 assertEquals(expectedTime2, actualTime2);
 long actualTime3 = stopWatch.getTime();
 long expectedTime3 = resumeTime - expectedTime2;
 assertEquals(expectedTime3, actualTime3);
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeBeforeSuspend() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.resume();
 }

 @Test(expected = IllegalStateException.class)
 public void testStartAfterStop() {
 StopWatch stopWatch = new StopWatch();
 stopWatch.stop();
 stopWatch.start();
 }
}