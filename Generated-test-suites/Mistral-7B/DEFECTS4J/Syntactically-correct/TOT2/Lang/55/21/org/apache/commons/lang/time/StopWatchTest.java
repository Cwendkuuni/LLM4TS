package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.time.StopWatch;
import org.junit.Test;

/**
 * Test cases for the StopWatch class.
 *
 * @author Expert1
 */
public class StopWatchTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        StopWatch stopWatch = new StopWatch();
        assertNotNull(stopWatch);
    }

    /**
     * Test start method.
     */
    @Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
    }

    /**
     * Test start method with already running stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test stop method.
     */
    @Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
        assertEquals(-1, stopWatch.getTime());
    }

    /**
     * Test split method.
     */
    @Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertTrue(stopWatch.isSplitted());
        assertEquals(-1, stopWatch.getTime());
        assertEquals(-1, stopWatch.getSplitTime());
    }

    /**
     * Test split method with already splitted stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testSplitAlreadySplitted() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    /**
     * Test unsplit method.
     */
    @Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isSplitted());
        assertEquals(-1, stopWatch.getSplitTime());
        assertTrue(stopWatch.isRunning());
        assertEquals(-1, stopWatch.getTime());
    }

    /**
     * Test unsplit method with unsplitted stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsplitUnsplitted() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
    }

    /**
     * Test suspend method.
     */
    @Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertFalse(stopWatch.isRunning());
    }

    /**
     * Test suspend method with already suspended stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testSuspendAlreadySuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.suspend();
    }

    /**
     * Test resume method.
     */
    @Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertTrue(stopWatch.isRunning());
    }

    /**
     * Test resume method with already running stopwatch.
     */
    @Test(expected = IllegalStateException.class)
    public void testResumeAlreadyRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
    }

    /**
     * Test resume method with unsuspended stopwatch.
     */
    @Test
    public void testResumeUnsuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.resume();
    }

    /**
     * Test getTime method.
     */
    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long time = System.currentTimeMillis();
        stopWatch.stop();
        assertEquals(time - stopWatch.getStartTime(), stopWatch.getTime());
    }

    /**
     * Test getSplitTime method.
     */
    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long time = System.currentTimeMillis();
        stopWatch.stop();
        assertEquals(time - stopWatch.getStartTime(), stopWatch.getSplitTime());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long time = System.currentTimeMillis();
        stopWatch.stop();
        String expected = "0m:0s:0ms.0ms";
        String actual = stopWatch.toString();
        String actualSplit = stopWatch.toSplitString();
        assertEquals(expected, actual);
        assertEquals("0m:0s:0ms.0ms", actualSplit);
    }
}