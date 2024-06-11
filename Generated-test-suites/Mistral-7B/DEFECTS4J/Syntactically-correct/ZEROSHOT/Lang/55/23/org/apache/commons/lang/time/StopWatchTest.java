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
 }

 @Test
 public void testSplitAndUnsplit() {
 stopWatch.start();
 stopWatch.split();
 Assert.assertNotNull(stopWatch.getSplitTime());
 stopWatch.unsplit();
 Assert.assertNotNull(stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testSplitTwice() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 }

 @Test
 public void testSuspendAndResume() {
 stopWatch.start();
 stopWatch.suspend();
 Thread.yield();
 stopWatch.resume();
 stopWatch.stop();
 Assert.assertNotNull(stopWatch.getTime());
 }

 @Test(expected = IllegalStateException.class)
 public void testSuspendTwice() {
 stopWatch.start();
 stopWatch.suspend();
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
 public void testToString() {
 stopWatch.start();
 stopWatch.stop();
 Assert.assertNotNull(stopWatch.toString());
 }

 @Test
 public void testToSplitString() {
 stopWatch.start();
 stopWatch.split();
 stopWatch.stop();
 Assert.assertNotNull(stopWatch.toSplitString());
 }
}