package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.StopWatch;

import org.junit.Test;

/**
 * Tests for the {@link StopWatch} class.
 */
public class StopWatchTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final StopWatch stopWatch = new StopWatch();
        assertNotNull(stopWatch);
    }

    /**
     * Test start().
     */
    @Test
    public void testStart() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
    }

    /**
     * Test split().
     */
    @Test
    public void testSplit() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isRunning());
        assertTrue(stopWatch.isSplit());
    }

    /**
     * Test split() - error.
     */
    @Test(expected = IllegalStateException.class)
    public void testSplitError() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        stopWatch.split();
    }

    /**
     * Test unsplit().
     */
    @Test
    public void testUnsplit() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isSplit());
        assertTrue(stopWatch.isRunning());
    }

    /**
     * Test unsplit() - error.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsplitError() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    /**
     * Test suspend().
     */
    @Test
    public void testSuspend() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
    }

    /**
     * Test resume().
     */
    @Test
    public void testResume() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
    }

    /**
     * Test resume() - error.
     */
    @Test(expected = IllegalStateException.class)
    public void testResumeError() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    /**
     * Test stop().
     */
    @Test
    public void testStop() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
    }

    /**
     * Test stop() - error.
     */
    @Test(expected = IllegalStateException.class)
    public void testStopError() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
        stopWatch.start();
    }

    /**
     * Test reset().
     */
    @Test
    public void testReset() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSplit());
        assertFalse(stopWatch.isStopped());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
    }

    /**
     * Test getTime().
     */
    @Test
    public void testGetTime() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.yield();
        Thread.yield();
        final long time = stopWatch.getTime();
        assertEquals(2 * Long.MAX_VALUE % 1000L, time);
    }

    /**
     * Test getSplitTime().
     */
    @Test
    public void testGetSplitTime() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        final long splitTime = stopWatch.getSplitTime();
        assertEquals(Long.MAX_VALUE % 1000L, splitTime);
    }

    /**
     * Test toString().
     */
    @Test
    public void testToString() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.yield();
        Thread.yield();
        final String result = stopWatch.toString();
        assertTrue(DurationFormatUtils.isParsable(result));
    }

    /**
     * Test toSplitString().
     */
    @Test
    public void testToSplitString() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        final String result = stopWatch.toSplitString();
        assertTrue(DurationFormatUtils.isParsable(result));
    }
}