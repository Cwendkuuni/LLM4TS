package org.apache.commons.lang.time;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link StopWatch} class.
 */
public class StopWatchTest {

    /**
     * Test for {@link StopWatch#start()}, {@link StopWatch#stop()}, and {@link StopWatch#getTime()}.
     */
    @Test
    public void testStartStopGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // some processing here
        stopWatch.stop();
        long actual = stopWatch.getTime();
        long expected = 42L; // replace with actual processing time
        Assert.assertEquals("Unexpected time elapsed.", expected, actual);
    }

    /**
     * Test for {@link StopWatch#start()}, {@link StopWatch#split()}, {@link StopWatch#unsplit()}, and {@link StopWatch#getTime()}.
     */
    @Test
    public void testStartSplitUnsplitGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long firstSplit = System.currentTimeMillis();
        stopWatch.split();
        // some processing here
        stopWatch.unsplit();
        long actual = stopWatch.getTime();
        long expected = 84L; // replace with actual processing time + time between split and unsplit
        Assert.assertEquals("Unexpected time elapsed.", expected, actual);
    }

    /**
     * Test for {@link StopWatch#start()}, {@link StopWatch#suspend()}, {@link StopWatch#resume()}, and {@link StopWatch#getTime()}.
     */
    @Test
    public void testStartSuspendResumeGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long firstSuspend = System.currentTimeMillis();
        stopWatch.suspend();
        // some processing here
        stopWatch.resume();
        long actual = stopWatch.getTime();
        long expected = 67L; // replace with actual processing time + time between suspend and resume
        Assert.assertEquals("Unexpected time elapsed.", expected, actual);
    }

    /**
     * Test for {@link StopWatch#start()}, {@link StopWatch#stop()}, and {@link StopWatch#getTime()} when {@link StopWatch} is being used multiple times.
     */
    @Test
    public void testMultipleInstances() {
        StopWatch stopWatch1 = new StopWatch();
        StopWatch stopWatch2 = new StopWatch();

        stopWatch1.start();
        // some processing here
        stopWatch1.stop();
        long time1 = stopWatch1.getTime();

        stopWatch2.start();
        // some processing here
        stopWatch2.stop();
        long time2 = stopWatch2.getTime();

        // Assert that both instances worked independently
        Assert.assertNotEquals("Unexpected same time elapsed.", time1, time2);
    }

    /**
     * Test for {@link StopWatch#start()}, {@link StopWatch#split()}, and {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void testStartSplitTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.split();
    }

    /**
     * Test for {@link StopWatch#stop()}, {@link StopWatch#getTime()}, and {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void testStopGetTimeBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
        stopWatch.getTime();
    }

    /**
     * Test for {@link StopWatch#resume()}, {@link StopWatch#getTime()}, and {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void testResumeGetTimeBeforeSuspend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
        stopWatch.resume();
        stopWatch.resume();
    }

    /**
     * Test for {@link StopWatch#unsplit()}, {@link StopWatch#getTime()}, and {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void testUnsplitGetTimeBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
        stopWatch.unsplit();
        stopWatch.unsplit();
    }
}