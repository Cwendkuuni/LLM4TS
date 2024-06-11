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
 Assert.assertNotNull(stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testStartTwice() {
 stopWatch.start();
 stopWatch.start();
 Assert.fail("Expected IllegalStateException but got none.");
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch.start();
 long splitTime = stopWatch.split();
 Assert.assertNotNull(splitTime);
 stopWatch.unsplit();
 Assert.assertNull(stopWatch.getSplitTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 Assert.fail("Expected IllegalStateException but got none.");
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch.start();
 long suspendTime = stopWatch.suspend();
 Assert.assertNotNull(suspendTime);
 stopWatch.resume();
 Assert.assertEquals(stopWatch.getTime(), stopWatch.getTime() + (System.currentTimeMillis() - suspendTime));
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendTwice() {
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.suspend();
 Assert.fail("Expected IllegalStateException but got none.");
 }
}