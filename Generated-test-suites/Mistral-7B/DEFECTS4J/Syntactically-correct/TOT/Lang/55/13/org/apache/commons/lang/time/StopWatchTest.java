package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Test class for StopWatch.
 */
public class StopWatchTest {

    @Test
    public void testBasicFunctionality() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TimeUnit.SECONDS.sleep(1);
        long timeElapsed = stopWatch.getTime();
        stopWatch.stop();
        Assert.assertEquals(1000L, timeElapsed);
    }

    @Test
    public void testSplitFunctionality() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TimeUnit.SECONDS.sleep(1);
        stopWatch.split();
        TimeUnit.MILLISECONDS.sleep(500);
        long splitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        long totalTime = stopWatch.getTime();
        Assert.assertEquals(1500L, totalTime);
        Assert.assertEquals(1000L, splitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        Assert.fail("Expected IllegalStateException when resuming an unsuspended StopWatch.");
    }

    @Test
    public void testMultipleSplits() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        TimeUnit.SECONDS.sleep(1);
        stopWatch.split();
        TimeUnit.MILLISECONDS.sleep(500);
        stopWatch.split();
        TimeUnit.MILLISECONDS.sleep(500);
        stopWatch.unsplit();
        long totalTime = stopWatch.getTime();
        Assert.assertEquals(2550L, totalTime);
        long firstSplitTime = stopWatch.getSplitTime();
        Assert.assertEquals(1000L, firstSplitTime);
        long secondSplitTime = stopWatch.getSplitTime();
        Assert.assertEquals(500L, secondSplitTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStopFunctionalityBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
        Assert.fail("Expected IllegalStateException when stopping an unstarted StopWatch.");
    }

    @Test
    public void testResetFunctionality() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        Assert.assertEquals(0L, stopWatch.getTime());
        Assert.assertEquals(-1L, stopWatch.getSplitTime());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsplitFunctionalityBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.unsplit();
        Assert.fail("Expected IllegalStateException when unsplitting an unsplit StopWatch.");
    }
}