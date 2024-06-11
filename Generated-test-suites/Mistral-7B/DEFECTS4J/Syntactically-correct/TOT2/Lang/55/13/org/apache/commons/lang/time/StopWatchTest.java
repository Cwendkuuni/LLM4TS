import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import static org.junit.Assert.*;

public class StopWatchTest {

 @Test
 public void testStartAndStop() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.stop();
 Long time = watch.getTime();
 assertNotNull(time);
 assertEquals(0, time); // The first start and stop should reset the timer
 }

 @Test
 public void testSplitAndUnsplit() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.split();
 watch.stop();
 Long splitTime = watch.getSplitTime();
 Long totalTime = watch.getTime();
 assertNotNull(splitTime);
 assertNotNull(totalTime);
 assertTrue(totalTime > splitTime);
 watch.unsplit();
 Long newTotalTime = watch.getTime();
 assertEquals(totalTime, newTotalTime); // The unsplit should resume timing from the original start
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwiceWithoutReset() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.start();
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.split();
 watch.split();
 }

 @Test(expected = IllegalStateException.class)
 public void testStopWithoutStart() {
 StopWatch watch = new StopWatch();
 watch.stop();
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendWithoutRunning() {
 StopWatch watch = new StopWatch();
 watch.suspend();
 }

 @Test(expected = IllegalStateException.class)
 public void testResumeWithoutSuspend() {
 StopWatch watch = new StopWatch();
 watch.resume();
 }

 @Test
 public void testSuspendAndResume() {
 StopWatch watch = new StopWatch();
 watch.start();
 watch.suspend();
 watch.resume();
 watch.stop();
 Long totalTime = watch.getTime();
 Long suspendedTime = System.currentTimeMillis() - watch.getStartTime();
 Long resumedTime = totalTime - suspendedTime;
 assertEquals(totalTime, resumedTime); // The time between suspend and resume should not be included in the total
 }

}