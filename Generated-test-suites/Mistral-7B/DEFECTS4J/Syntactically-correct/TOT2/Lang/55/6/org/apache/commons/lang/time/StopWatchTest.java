import org.apache.commons.lang.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0L);
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0L);
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        long time = stopWatch.getTime();
        Assert.assertGreaterThan(time, 0L);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitWithoutSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.unsplit();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        long time = stopWatch.getTime();
        Assert.assertEquals(time, 0L);
    }
}