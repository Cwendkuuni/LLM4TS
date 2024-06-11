import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testBasicUsage() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some code to test
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 123; // replace with actual time measured
        Assert.assertEquals("Time should be equal", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testStopBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        long actualTime = stopWatch.getTime();
        long expectedTime = splitTime + actualTime; // replace with actual times
        Assert.assertEquals("Time should be equal", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
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
        stopWatch.suspend();
        long pausedTime = System.currentTimeMillis() - stopWatch.getStartTime();
        stopWatch.resume();
        long actualTime = stopWatch.getTime();
        long expectedTime = pausedTime + actualTime; // replace with actual times
        Assert.assertEquals("Time should be equal", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long actualTime = stopWatch.getTime();
        stopWatch.reset();
        long expectedTime = 0;
        Assert.assertEquals("Time should be equal", expectedTime, actualTime);
    }
}