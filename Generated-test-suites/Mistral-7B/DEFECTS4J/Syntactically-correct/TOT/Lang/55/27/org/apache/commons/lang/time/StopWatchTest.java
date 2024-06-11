package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * Test class for {@link StopWatch}.
 */
public class StopWatchTest {

    /**
     * Tests constructor.
     */
    @Test
    public void testConstructor() {
        new StopWatch();
    }

    /**
     * Tests {@code start()} method.
     */
    @Test
    public void testStart() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        assertTrue(stopwatch.isRunning());
        assertFalse(stopwatch.isStopped());
        assertFalse(stopwatch.isSuspended());
    }

    /**
     * Tests {@code start()} method with already running stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.start();
    }

    /**
     * Tests {@code stop()} method.
     */
    @Test
    public void testStop() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        assertTrue(stopwatch.isStopped());
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.isSuspended());
        assertEquals(0, stopwatch.getTime());
        assertNull(stopwatch.getSplitTime());
    }

    /**
     * Tests {@code split()} method.
     */
    @Test
    public void testSplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        assertTrue(stopwatch.isSplit());
        assertTrue(stopwatch.isRunning());
        assertEquals(0, stopwatch.getTime());
        assertNotNull(stopwatch.getSplitTime());
    }

    /**
     * Tests {@code split()} method with already split stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testSplitAlreadySplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.split();
    }

    /**
     * Tests {@code unsplit()} method.
     */
    @Test
    public void testUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        stopwatch.unsplit();
        assertTrue(stopwatch.isRunning());
        assertFalse(stopwatch.isSplit());
        assertEquals(0, stopwatch.getTime());
        assertNull(stopwatch.getSplitTime());
    }

    /**
     * Tests {@code unsplit()} method with unsplit stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsplitUnsplit() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.unsplit();
    }

    /**
     * Tests {@code suspend()} method.
     */
    @Test
    public void testSuspend() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        assertTrue(stopwatch.isSuspended());
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.isStopped());
    }

    /**
     * Tests {@code resume()} method.
     */
    @Test
    public void testResume() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();
        assertTrue(stopwatch.isRunning());
        assertFalse(stopwatch.isSuspended());
        assertFalse(stopwatch.isStopped());
    }

    /**
     * Tests {@code resume()} method with unsuspened stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testResumeUnsuspended() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.resume();
    }

    /**
     * Tests {@code getTime()} method.
     */
    @Test
    public void testGetTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        long time = System.currentTimeMillis();
        stopwatch.stop();
        assertEquals(time - stopwatch.getStartTime(), stopwatch.getTime());
    }

    /**
     * Tests {@code getSplitTime()} method.
     */
    @Test
    public void testGetSplitTime() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        long time = System.currentTimeMillis();
        stopwatch.stop();
        assertEquals(time - stopwatch.getStartTime(), stopwatch.getSplitTime());
    }

    /**
     * Tests {@code toString()} method.
     */
    @Test
    public void testToString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        long time = System.currentTimeMillis();
        stopwatch.stop();
        String result = stopwatch.toString();
        String expected = DurationFormatUtils.formatDurationHMS(stopwatch.getTime());
        assertEquals(expected, result);
    }

    /**
     * Tests {@code toSplitString()} method.
     */
    @Test
    public void testToSplitString() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.split();
        long time = System.currentTimeMillis();
        stopwatch.stop();
        String result = stopwatch.toSplitString();
        String expected = DurationFormatUtils.formatDurationHMS(stopwatch.getSplitTime());
        assertEquals(expected, result);
    }

    /**
     * Tests {@code reset()} method.
     */
    @Test
    public void testReset() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        stopwatch.stop();
        stopwatch.reset();
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.isStopped());
        assertFalse(stopwatch.isSuspended());
        assertNull(stopwatch.getStartTime());
        assertNull(stopwatch.getStopTime());
    }
}