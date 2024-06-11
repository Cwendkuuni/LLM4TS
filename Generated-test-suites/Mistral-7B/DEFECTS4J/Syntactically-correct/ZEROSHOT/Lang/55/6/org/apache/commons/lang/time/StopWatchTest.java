import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some code execution here
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // replace with the expected time for the code execution
        Assert.assertEquals("The time should be equal to the expected time.", expectedTime, actualTime);
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
        long expectedTime = 0; // replace with the expected time for the code execution before and after the split
        Assert.assertEquals("The time should be equal to the expected time.", expectedTime, actualTime);
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
        stopWatch.suspend();
        // some code execution here
        stopWatch.resume();
        // some code execution here
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // replace with the expected time for the code execution before and after the suspend
        Assert.assertEquals("The time should be equal to the expected time.", expectedTime, actualTime);
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
        // some code execution here
        stopWatch.reset();
        long actualTime = stopWatch.getTime();
        Assert.assertEquals("The time should be zero after reset.", 0, actualTime);
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some code execution here
        stopWatch.stop();
        String actualString = stopWatch.toString();
        String expectedString = "0m:0s:0.000ms"; // replace with the expected format and time
        Assert.assertEquals("The stopwatch toString should be equal to the expected string.", expectedString, actualString);
    }
}