package org.apache.commons.lang.time;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Unit tests for {@link StopWatch} class.
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    @Before
    public void setUp() {
        stopWatch = new StopWatch();
    }

    /**
     * Test that start method initializes the stopwatch and returns the correct state.
     */
    @Test
    public void testStart() {
        stopWatch.start();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        Assert.assertEquals(-1, stopWatch.getStartTime());
        Assert.assertEquals(-1, stopWatch.getStopTime());
    }

    /**
     * Test that stop method correctly records the stop time and sets the state to STATE_STOPPED.
     */
    @Test
    public void testStop() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        stopWatch.stop();
        Assert.assertEquals(StopWatch.STATE_STOPPED, stopWatch.getRunningState());
        Assert.assertEquals(-1, stopWatch.getStartTime());
        Assert.assertNotEquals(-1, stopWatch.getStopTime());
        Assert.assertTrue(stopWatch.getTime() > 0);
    }

    /**
     * Test that the reset method clears internal values and sets the state to STATE_UNSTARTED.
     */
    @Test
    public void testReset() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(StopWatch.STATE_UNSTARTED, stopWatch.getRunningState());
        Assert.assertEquals(-1, stopWatch.getStartTime());
        Assert.assertEquals(-1, stopWatch.getStopTime());
    }

    /**
     * Test that split method records the split time and sets the state to STATE_SPLIT.
     */
    @Test
    public void testSplit() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        long splitTime = stopWatch.split();
        Assert.assertEquals(StopWatch.STATE_SPLIT, stopWatch.getRunningState());
        Assert.assertNotEquals(-1, stopWatch.getStartTime());
        Assert.assertNotEquals(-1, stopWatch.getStopTime());
        Assert.assertTrue(stopWatch.getTime() > 0);
        Assert.assertTrue(stopWatch.getSplitTime() > 0);
        Assert.assertEquals(splitTime, stopWatch.getStartTime());
    }

    /**
     * Test that unsplit method correctly removes the effect of the split and sets the state to STATE_RUNNING.
     */
    @Test
    public void testUnsplit() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        long splitTime = stopWatch.split();
        stopWatch.unsplit();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        Assert.assertEquals(-1, stopWatch.getStartTime());
        Assert.assertEquals(-1, stopWatch.getStopTime());
        Assert.assertTrue(stopWatch.getTime() > 0);
    }

    /**
     * Test that suspend method correctly suspends the stopwatch and sets the state to STATE_SUSPENDED.
     */
    @Test
    public void testSuspend() {
        stopWatch.start();
        stopWatch.suspend();
        Assert.assertEquals(StopWatch.STATE_SUSPENDED, stopWatch.getRunningState());
        Assert.assertNotEquals(-1, stopWatch.getStartTime());
        Assert.assertNotEquals(-1, stopWatch.getStopTime());
        Assert.assertTrue(stopWatch.getTime() > 0);
    }

    /**
     * Test that resume method correctly resumes the stopwatch and sets the state to STATE_RUNNING.
     */
    @Test
    public void testResume() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        stopWatch.suspend();
        TimeUnit.MILLISECONDS.sleep(100);
        stopWatch.resume();
        Assert.assertEquals(StopWatch.STATE_RUNNING, stopWatch.getRunningState());
        Assert.assertEquals(-1, stopWatch.getStartTime());
        Assert.assertEquals(-1, stopWatch.getStopTime());
        Assert.assertTrue(stopWatch.getTime() > 0);
    }

    /**
     * Test that getTime method returns the correct time.
     */
    @Test
    public void testGetTime() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(200);
        stopWatch.stop();
        Assert.assertEquals(200, stopWatch.getTime());
    }

    /**
     * Test that getSplitTime method returns the correct split time.
     */
    @Test
    public void testGetSplitTime() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        long splitTime = stopWatch.split();
        Assert.assertEquals(100, stopWatch.getSplitTime());
    }

    /**
     * Test that toString method returns the correct time string.
     */
    @Test
    public void testToString() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(200);
        stopWatch.stop();
        Assert.assertEquals("200ms", stopWatch.toString());
    }

    /**
     * Test that toSplitString method returns the correct split time string.
     */
    @Test
    public void testToSplitString() {
        stopWatch.start();
        TimeUnit.MILLISECONDS.sleep(100);
        long splitTime = stopWatch.split();
        Assert.assertEquals("100ms", stopWatch.toSplitString());
    }
}