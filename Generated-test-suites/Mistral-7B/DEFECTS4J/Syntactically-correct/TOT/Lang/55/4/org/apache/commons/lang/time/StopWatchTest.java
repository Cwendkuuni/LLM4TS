import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testBasicFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some computation or other task here
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 42L; // replace with actual time of computation
        Assert.assertEquals("Time taken was incorrect.", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStartBeforeStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop(); // should be an error here
        stopWatch.start();
    }

    @Test
    public void testSplitFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some computation or other task here
        stopWatch.split();
        long actualSplitTime = stopWatch.getSplitTime();
        long expectedSplitTime = 21L; // replace with actual time of split
        Assert.assertEquals("Split time taken was incorrect.", expectedSplitTime, actualSplitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split(); // should be an error here
    }

    @Test
    public void testSuspendResumeFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some computation or other task here
        stopWatch.suspend();
        // simulate some external operation here
        stopWatch.resume();
        long actualResumedTime = stopWatch.getTime();
        long expectedResumedTime = 63L; // replace with actual time after resume
        Assert.assertEquals("Resumed time taken was incorrect.", expectedResumedTime, actualResumedTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalSuspendBeforeRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend(); // should be an error here
    }

    @Test
    public void testResetFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // perform some computation or other task here
        stopWatch.stop();
        stopWatch.reset();
        long actualResetTime = stopWatch.getTime();
        Assert.assertEquals("Reset time taken was not zero.", 0, actualResetTime);
    }
}