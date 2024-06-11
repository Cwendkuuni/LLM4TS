package org.apache.commons.lang.time;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link StopWatch}.
 */
public class StopWatchTest {

    private StopWatch stopwatch;

    @Before
    public void setUp() {
        stopwatch = new StopWatch();
    }

    /**
     * Test method for {@link StopWatch#start()}.
     */
    @Test
    public void testStart() {
        stopwatch.start();
        assertTrue(stopwatch.isRunning());
        assertFalse(stopwatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#stop()}.
     */
    @Test
    public void testStop() {
        stopwatch.start();
        long startTime = System.currentTimeMillis();
        stopwatch.stop();

        long elapsedTime = stopwatch.getTime();
        assertEquals(-startTime, elapsedTime);
        assertTrue(stopwatch.isStopped());
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#reset()}.
     */
    @Test
    public void testReset() {
        stopwatch.start();
        stopwatch.split();
        long startTime = System.currentTimeMillis();
        stopwatch.stop();
        stopwatch.reset();

        assertEquals(0, stopwatch.getTime());
        assertNull(stopwatch.getSplitTime());
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.isStopped());
        assertFalse(stopwatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#split()}.
     */
    @Test
    public void testSplit() {
        stopwatch.start();
        stopwatch.split();
        long startTime = System.currentTimeMillis();
        stopwatch.stop();

        long elapsedTime = stopwatch.getTime();
        assertEquals(-startTime, elapsedTime);
        long splitTime = stopwatch.getSplitTime();
        assertEquals(-(System.currentTimeMillis() - stopTime), splitTime);
        assertTrue(stopwatch.isStopped());
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#unsplit()}.
     */
    @Test
    public void testUnsplit() {
        stopwatch.start();
        stopwatch.split();
        stopwatch.stop();
        stopwatch.unsplit();

        long elapsedTime = stopwatch.getTime();
        assertEquals(-(System.currentTimeMillis() - stopTime), elapsedTime);
        assertNull(stopwatch.getSplitTime());
        assertTrue(stopwatch.isStopped());
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#suspend()}.
     */
    @Test
    public void testSuspend() {
        stopwatch.start();
        stopwatch.suspend();

        assertTrue(stopwatch.isSuspended());
        assertFalse(stopwatch.isRunning());
        assertFalse(stopwatch.isStopped());
    }

    /**
     * Test method for {@link StopWatch#resume()}.
     */
    @Test
    public void testResume() {
        stopwatch.start();
        stopwatch.suspend();
        stopwatch.resume();

        assertTrue(stopwatch.isRunning());
        assertFalse(stopwatch.isSuspended());
        assertFalse(stopwatch.isStopped());
    }

    /**
     * Test method for {@link StopWatch#getTime()}.
     */
    @Test
    public void testGetTime() {
        stopwatch.start();
        long elapsedTime = stopwatch.getTime();
        assertEquals(0, elapsedTime);

        long startTime = System.currentTimeMillis();
        stopwatch.stop();
        elapsedTime = stopwatch.getTime();
        assertEquals(-startTime, elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#getSplitTime()}.
     */
    @Test
    public void testGetSplitTime() {
        stopwatch.start();
        stopwatch.split();
        long elapsedTime = stopwatch.getTime();
        assertEquals(0, elapsedTime);

        long startTime = System.currenttimeTimeMillis();
        stopwatch.stop();
        elapsedTime = stopwatch.getSplitTime();
        assertEquals(-(System.currenttimeTimeMillis() - stopTime), elapsedTime);
    }

    /**
     * Test method for {@link StopWatch#toString()}.
     */
    @Test
    public void testToString() {
        stopwatch.start();
        stopwatch.split();
        long startTime = System.currenttimeTimeMillis();
        stopwatch.stop();

        String result = stopwatch.toString();
        String expected = "0s";
        assertEquals(expected, result);

        stopwatch.reset();
        stopwatch.start();
        stopwatch.split();
        startTime = System.currenttimeTimeMillis();
        stopwatch.stop();

        result = stopwatch.toString();
        expected = DurationFormatUtils.formatDurationHMS(stopwatch.getTime());
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link StopWatch#isRunning()}.
     */
    @Test
    public void testIsRunning() {
        assertTrue(stopwatch.isRunning());
        stopwatch.stop();
        assertFalse(stopwatch.isRunning());
    }

    /**
     * Test method for {@link StopWatch#isStopped()}.
     */
    @Test
    public void testIsStopped() {
        assertFalse(stopwatch.isStopped());
        stopwatch.stop();
        assertTrue(stopwatch.isStopped());
    }

    /**
     * Test method for {@link StopWatch#isSuspended()}.
     */
    @Test
    public void testIsSuspended() {
        assertFalse(stopwatch.isSuspended());
        stopwatch.suspend();
        assertTrue(stopwatch.isSuspended());
    }

    /**
     * Test method for {@link StopWatch#isSplit()}.
     */
    @Test
    public void testIsSplit() {
        assertFalse(stopwatch.isSplit());
        stopwatch.split();
        assertTrue(stopwatch.isSplit());
    }

    /**
     * Test method for {@link StopWatch#start()}.
     * Throws an exception when called twice without reset.
     */
    @Test
    public void testStartTwiceWithoutReset() {
        stopwatch.start();

        assertThrows(IllegalStateException.class, () -> {
            stopwatch.start();
        });
    }

    /**
     * Test method for {@link StopWatch#split()}.
     * Throws an exception when called twice without unsplit.
     */
    @Test
    public void testSplitTwiceWithoutUnsplit() {
        stopwatch.start();
        stopwatch.split();

        assertThrows(IllegalStateException.class, () -> {
            stopwatch.split();
        });
    }

    /**
     * Test method for {@link StopWatch#resume()}.
     * Throws an exception when called without suspend.
     */
    @Test
    public void testResumeWithoutSuspend() {
        stopwatch.start();

        assertThrows(IllegalStateException.class, () -> {
            stopwatch.resume();
        });
    }

    /**
     * Test method for {@link StopWatch#unsplit()}.
     * Throws an exception when called without split.
     */
    @Test
    public void testUnsplitWithoutSplit() {
        stopwatch.start();

        assertThrows(IllegalStateException.class, () -> {
            stopwatch.unsplit();
        });
    }
}