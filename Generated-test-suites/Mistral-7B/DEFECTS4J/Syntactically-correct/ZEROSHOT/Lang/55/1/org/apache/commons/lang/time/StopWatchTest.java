package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Test class for the StopWatch class.
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @Test
    public void testStartAndStop() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertEquals(0, stopWatch.getTime(), 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertEquals(0, stopWatch.getTime(), 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitTwice() {
        stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        stopWatch = new StopWatch();
        stopWatch.start();
        long startTime = System.currentTimeMillis();
        stopWatch.suspend();
        long pauseTime = System.currentTimeMillis() - startTime;
        stopWatch.resume();
        stopWatch.stop();
        long totalTime = stopWatch.getTime();
        long expectedTime = pauseTime + stopWatch.getTime(stopWatch.getSplitTime());
        assertEquals(expectedTime, totalTime, 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendWithoutRunning() {
        stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test(expected = IllegalStateException.class)
    public void testResumeWithoutSuspended() {
        stopWatch = new StopWatch();
        stopWatch.resume();
    }

    @Test
    public void testGetTimeAndSplitTime() {
        stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = System.currentTimeMillis();
        stopWatch.split();
        long timeElapsed = stopWatch.getTime();
        long splitTimeElapsed = stopWatch.getSplitTime();
        long expectedTime = TimeUnit.MILLISECONDS.toMillis(TimeUnit.MINUTES.toMillis(1)) +
                TimeUnit.MILLISECONDS.toMillis(500);
        assertEquals(expectedTime, timeElapsed, 1);
        long expectedSplitTime = TimeUnit.MILLISECONDS.toMillis(TimeUnit.SECONDS.toMillis(2)) +
                TimeUnit.MILLISECONDS.toMillis(350);
        assertEquals(expectedSplitTime, splitTimeElapsed, 1);
    }

    @Test
    public void testToStringAndtoSplitString() {
        stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = System.currentTimeMillis();
        stopWatch.split();
        String timeString = stopWatch.toString();
        String splitString = stopWatch.toSplitString();
        String format = "%d:%02d:%02d.%03d";
        String expectedTimeFormat = String.format(format, 1, 0, 5, 999);
        String expectedSplitFormat = String.format(format, 0, 2, 3, 598);
        assertTrue(timeString.contains(expectedTimeFormat));
        assertTrue(splitString.contains(expectedSplitFormat));
    }
}