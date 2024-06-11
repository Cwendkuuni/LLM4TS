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
long actual = stopWatch.getTime();
long expected = 0; // Test case for empty timing
Assert.assertEquals(expected, actual);

stopWatch.reset();
stopWatch.start();
long elapsed = 1000; // Test case for a short timing
System.out.println("Elapsed time: " + elapsed + " ms");
Thread.yield();
Thread.yield();
Thread.yield();
stopWatch.stop();
actual = stopWatch.getTime();
expected = elapsed;
Assert.assertEquals(expected, actual);
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
long elapsedBeforeSplit = stopWatch.getTime();
 Assert.assertTrue(elapsedBeforeSplit > 0);
 stopWatch.unsplit();
 long elapsedAfterUnsplit = stopWatch.getTime();
 Assert.assertEquals(elapsedBeforeSplit, elapsedAfterUnsplit);
}

@Test(expected = IllegalStateException.class)
public void testSplitBeforeStart() {
stopWatch = new StopWatch();
stopWatch.split();
}

@Test(expected = IllegalStateException.class)
public void testUnsplitBeforeSplit() {
stopWatch = new StopWatch();
stopWatch.unsplit();
}

@Test
public void testSuspendAndResume() {
stopWatch = new StopWatch();
stopWatch.start();
Thread.sleep(500);
stopWatch.suspend();
Thread.sleep(1000);
stopWatch.resume();
long elapsedBeforeSuspend = stopWatch.getTime();
long elapsedAfterResume = stopWatch.getTime();
Assert.assertEquals(elapsedBeforeSuspend + 1000, elapsedAfterResume);
}

@Test(expected = IllegalStateException.class)
public void testSuspendBeforeRun() {
stopWatch = new StopWatch();
stopWatch.suspend();
}

@Test(expected = IllegalStateException.class)
public void testResumeBeforeSuspend() {
stopWatch = new StopWatch();
stopWatch.resume();
}
}