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
        Assert.assertNotNull(time);
        Assert.assertEquals(0, stopWatch.getSplitTime());
        Assert.assertEquals("0.000:00.000.001", stopWatch.toString());
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
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        Assert.assertNotNull(time);
        Assert.assertEquals(0, stopWatch.getSplitTime());
        Assert.assertEquals("0.000:00.000.002", stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        long pausedTime = System.currentTimeMillis();
        stopWatch.resume();
        stopWatch.stop();
        long totalTime = stopWatch.getTime();
        long pauseTime = System.currentTimeMillis() - pausedTime;
        Assert.assertNotNull(totalTime);
        Assert.assertEquals(pauseTime, stopWatch.getSplitTime());
        Assert.assertEquals("0.000:00.000.001", stopWatch.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRun() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(0, stopWatch.getTime());
        Assert.assertEquals(0, stopWatch.getSplitTime());
    }

    @Test
    public void testToStringAndToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String timeString = stopWatch.toString();
        String splitString = stopWatch.toSplitString();
        Assert.assertNotNull(timeString);
        Assert.assertNotNull(splitString);
        Assert.assertEquals("0.000:00.000.002", timeString);
        Assert.assertEquals("0.000:00.000.001", splitString);
    }
}