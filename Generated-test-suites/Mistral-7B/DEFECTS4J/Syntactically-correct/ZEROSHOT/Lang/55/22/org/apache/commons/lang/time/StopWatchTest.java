import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertNotEquals(-1, time);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        Assert.assertNotEquals(-1, splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        Assert.assertNotEquals(-1, time);
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
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertEquals(pauseTime, time % pauseTime);
        time -= pauseTime;
        Assert.assertNotEquals(-1, time);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeRun() {
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
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(-1, stopWatch.getTime());
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String timeString = stopWatch.toString();
        Assert.assertNotNull(timeString);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String splitString = stopWatch.toSplitString();
        Assert.assertNotNull(splitString);
    }
}