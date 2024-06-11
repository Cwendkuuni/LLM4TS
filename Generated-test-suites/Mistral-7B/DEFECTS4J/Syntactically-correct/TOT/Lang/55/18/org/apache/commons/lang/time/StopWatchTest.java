package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Random;

/**
 * Test case for {@link StopWatch} class.
 *
 * @author John Doe
 */
public class StopWatchTest {

    private static final int MAX_TEST_ITERATIONS = 100;

    @org.junit.BeforeEach
    public void setUp() {
        new Random().nextInt(); // init random seed for random sleep time
    }

    /**
     * Test for {@link StopWatch#start()} method.
     */
    @org.junit.Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    /**
     * Test for {@link StopWatch#stop()} method.
     */
    @org.junit.Test
    public void testStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        long time = stopWatch.getTime();
        assertNotEquals(-1, time);
        assertEquals(0, time);
        String result = stopWatch.toString();
        assertEquals("0.000:00.000.002", result);
        result = stopWatch.toSplitString();
        assertNull(result);
    }

    /**
     * Test for {@link StopWatch#split()} method.
     */
    @org.junit.Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertTrue(stopWatch.isSplit());
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        long splitTime = stopWatch.getSplitTime();
        assertNotEquals(-1, splitTime);
        assertEquals(0, splitTime);
        String result = stopWatch.toString();
        assertEquals("0.000:00.000.002", result);
        result = stopWatch.toSplitString();
        assertEquals("0.000:00.000.002", result);
    }

    /**
     * Test for {@link StopWatch#unsplit()} method.
     */
    @org.junit.Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        String result = stopWatch.toString();
        assertEquals("0.000:00.000.000", result);
        result = stopWatch.toSplitString();
        assertNull(result);
    }

    /**
     * Test for {@link StopWatch#suspend()} method.
     */
    @org.junit.Test
    public void testSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        String result = stopWatch.toString();
        assertEquals("0.000:00.000.000", result);
        stopWatch.resume();
        time = stopWatch.getTime();
        assertEquals(-1, time);
        result = stopWatch.toString();
        assertEquals("0.000:00.000.002", result);
    }

    /**
     * Test for {@link StopWatch#resume()} method.
     */
    @org.junit.Test
    public void testResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        long time = stopWatch.getTime();
        assertEquals(-1, time);
        String result = stopWatch.toString();
        assertEquals("0.000:00.000.002", result);
    }

    /**
     * Test for {@link StopWatch#reset()} method.
     */
    @org.junit.Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        long time = stopWatch.getTime();
        assertEquals(0, time);
        String result = stopWatch.toString();
        assertEquals("0.000:00.000.000", result);
        result = stopWatch.toSplitString();
        assertNull(result);
    }

    /**
     * Test for {@link StopWatch#getTime()} method.
     */
    @org.junit.Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.stop();
        long time = stopWatch.getTime();
        assertEquals(splitTime + time, stopWatch.getTime());
    }

    /**
     * Test for {@link StopWatch#getSplitTime()} method.
     */
    @org.junit.Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long time = stopWatch.getTime();
        assertEquals(stopWatch.getSplitTime(), time);
        stopWatch.stop();
        time = stopWatch.getTime();
        assertEquals(time - stopWatch.getSplitTime(), stopWatch.getTime());
        stopWatch.reset();
        stopWatch.start();
        stopWatch.split();
        assertThrows(IllegalStateException.class, () -> stopWatch.getSplitTime());
    }

    /**
     * Test for invalid method calls.
     */
    @org.junit.Test
    public void testInvalidCalls() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
        assertThrows(IllegalStateException.class, () -> stopWatch.stop());
        stopWatch.reset();
    }

    /**
     * Test for {@link StopWatch#toString()} method.
     */
    @org.junit.Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String result = stopWatch.toString();
        String expected = "0.000:00.000.002";
        assertEquals(expected, result);
    }

    /**
     * Test for {@link StopWatch#toSplitString()} method.
     */
    @org.junit.Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        String result = stopWatch.toSplitString();
        String expected = "0.000:00.000.002";
        assertEquals(expected, result);
    }
}