import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testNormalUsage() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some code execution here
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 500; // replace with the expected time
        Assert.assertEquals("Time should be equal", expectedTime, actualTime);
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
        // some code execution here
        stopWatch.split();
        // some code execution here
        stopWatch.unsplit();
        long actualTime = stopWatch.getTime();
        long expectedTime = 1000; // replace with the expected time
        Assert.assertEquals("Time should be equal", expectedTime, actualTime);
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
        // some code execution here
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        // some code execution here
        stopWatch.resume();
        long endTime = System.currentTimeMillis();
        long actualTime = stopWatch.getTime();
        long expectedTime = endTime - startTime;
        Assert.assertEquals("Time should be equal", expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some code execution here
        stopWatch.reset();
        long actualTime = stopWatch.getTime();
        Assert.assertEquals("Time should be equal", 0, actualTime);
    }

    @Test
    public void testGetTimeAfterStartButBeforeStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long actualTime = stopWatch.getTime();
        Assert.assertTrue("Time should be greater than zero", actualTime > 0);
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some code execution here
        stopWatch.stop();
        String actualString = stopWatch.toString();
        String expectedString = "500ms"; // replace with the expected string
        Assert.assertEquals("String representation should be equal", expectedString, actualString);
    }
}