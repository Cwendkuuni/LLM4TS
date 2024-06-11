import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the StopWatch class.
 */
public class StopWatchTest {

    @Test
    public void testBasicTiming() {
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.stop();
        long time = watch.getTime();
        Assert.assertGreaterThan(time, 0);
        System.out.println("Time elapsed: " + DurationFormatUtils.formatDurationHMS(time));
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.split();
        long splitTime = watch.getSplitTime();
        Assert.assertGreaterThan(splitTime, 0);
        watch.unsplit();
        long time = watch.getTime();
        Assert.assertGreaterThan(time, splitTime);
        System.out.println("Split time: " + DurationFormatUtils.formatDurationHMS(splitTime));
        System.out.println("Total time: " + DurationFormatUtils.formatDurationHMS(time));
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitWithoutStart() {
        StopWatch watch = new StopWatch();
        watch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch watch = new StopWatch();
        watch.unsplit();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.suspend();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watch.resume();
        long time = watch.getTime();
        Assert.assertGreaterThan(time, 0);
        System.out.println("Time elapsed: " + DurationFormatUtils.formatDurationHMS(time));
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        StopWatch watch = new StopWatch();
        watch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspended() {
        StopWatch watch = new StopWatch();
        watch.resume();
    }

    @Test
    public void testReset() {
        StopWatch watch = new StopWatch();
        watch.start();
        watch.reset();
        long time = watch.getTime();
        Assert.assertEquals(time, 0);
    }
}