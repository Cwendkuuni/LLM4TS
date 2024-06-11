import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        String expected = "0:0:0.001"; // 1ms
        String actual = stopWatch.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        String expected = "0:0:0.001"; // 1ms
        String actual = stopWatch.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.stop();
        String expected = "0:0:0.001"; // 1ms
        String actual = stopWatch.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsuspendBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsuspend();
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = stopWatch.getTime();
        assertEquals(time, 0);
        stopWatch.stop();
        time = stopWatch.getTime();
        String expected = "0:0:0.001"; // 1ms
        String actual = DurationFormatUtils.formatDurationHMS(time);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTimeBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        long time = stopWatch.getTime();
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long time = stopWatch.getSplitTime();
        String expected = "0:0:0.001"; // 1ms
        String actual = DurationFormatUtils.formatDurationHMS(time);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        long time = stopWatch.getSplitTime();
    }
}