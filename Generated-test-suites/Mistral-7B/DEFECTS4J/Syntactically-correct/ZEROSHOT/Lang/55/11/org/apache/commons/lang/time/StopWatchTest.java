import org.apache.commons.lang3.time.StopWatch;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    @After
    public void tearDown() {
        stopWatch.reset();
    }

    @Test
    public void testStartAndStop() {
        stopWatch.start();
        stopWatch.stop();
        assertNotNull(stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        assertEquals(stopWatch.getTime(), splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        long originalTime = stopWatch.getTime();
        stopWatch.suspend();
        stopWatch.resume();
        assertEquals(originalTime + (System.currentTimeMillis() - stopWatch.getSplitTime()), stopWatch.getTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeRun() {
        stopWatch.suspend();
    }

    @Test
    public void testGetTime() {
        stopWatch.start();
        long time = stopWatch.getTime();
        stopWatch.stop();
        assertEquals(time, stopWatch.getTime());
    }

    @Test
    public void testToString() {
        stopWatch.start();
        String timeString = stopWatch.toString();
        stopWatch.stop();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getTime()), timeString);
    }

    @Test
    public void testToSplitString() {
        stopWatch.start();
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        stopWatch.unsplit();
        assertEquals(DurationFormatUtils.formatDurationHMS(stopWatch.getSplitTime()), splitTimeString);
    }
}