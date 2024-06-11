package org.apache.commons.lang.time;

import static org.junit.Assert.*;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.text.ParseException;

/**
 * Tests for {@link StopWatch}.
 */
public class StopWatchTest {

    @org.junit.Before
    public void setUp() {
        StopWatch stopWatch = new StopWatch();
    }

    /**
     * Test start() and stop().
     */
    @org.junit.Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long time = stopWatch.getTime();
        String formattedTime = DurationFormatUtils.formatDurationHMS(time);
        assertEquals("0m:0s:0ms", formattedTime);
    }

    /**
     * Test split() and unsplit().
     */
    @org.junit.Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        long time = stopWatch.getTime();
        String formattedTime = DurationFormatUtils.formatDurationHMS(time);
        assertEquals("0m:0s:0ms", formattedTime);
    }

    /**
     * Test suspend() and resume().
     */
    @org.junit.Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        long time = stopWatch.getTime();
        String formattedTime = DurationFormatUtils.formatDurationHMS(time);
        assertEquals("0m:0s:0ms", formattedTime);
    }

    /**
     * Test illegal calls to methods.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testIllegalCalls() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
        stopWatch.stop();
    }

    /**
     * Test split() and stop() at the same time.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitAndStopAtTheSameTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.split();
    }

    /**
     * Test split() and unsplit() at the same time.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitAndUnsplitAtTheSameTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    /**
     * Test resume() and suspend() at the same time.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeAndSuspendAtTheSameTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.suspend();
    }

    /**
     * Test reset().
     */
    @org.junit.Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        long time = stopWatch.getTime();
        String formattedTime = DurationFormatUtils.formatDurationHMS(time);
        assertEquals("0m:0s:0ms", formattedTime);
    }

    /**
     * Test formatDurationHMS() parsing.
     */
    @org.junit.Test
    public void testFormatDurationHMSParsing() {
        String time = "1m:1s:123ms";
        long parsedTime = 0;
        try {
            parsedTime = Long.parseLong(DurationFormatUtils.parseDuration(time));
        } catch (ParseException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        assertEquals(1 * 60 * 60 * 1000 + 1 * 60 * 1000 + 123, parsedTime);
    }
}