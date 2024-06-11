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
        Assert.assertEquals(0, stopWatch.getSplitTime());
        Assert.assertEquals("0.0", stopWatch.toString());
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
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = System.currentTimeMillis();
        stopWatch.unsplit();
        long endTime = System.currentTimeMillis();
        long time = stopWatch.getTime();
        Assert.assertEquals(endTime - startTime, time);
        Assert.assertEquals(splitTime - startTime, stopWatch.getSplitTime());
        Assert.assertEquals(DurationFormatUtils.formatDurationHMS(time), "0.0");
        Assert.assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), "0.0");
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long suspendTime = System.currentTimeMillis();
        stopWatch.resume();
        long endTime = System.currentTimeMillis();
        long time = stopWatch.getTime();
        Assert.assertEquals(endTime - startTime, time);
        Assert.assertEquals(suspendTime - startTime, stopWatch.getSplitTime());
        Assert.assertEquals(DurationFormatUtils.formatDurationHMS(time), "0.0");
        Assert.assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), "0.0");
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.split();
        long splitTime = System.currentTimeMillis();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(0, stopWatch.getTime());
        Assert.assertEquals(0, stopWatch.getSplitTime());
        Assert.assertEquals("0.0", stopWatch.toString());
    }
}