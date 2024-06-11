package org.apache.commons.lang.time;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Test class for the StopWatch class.
 */
public class StopWatchTest {

    private StopWatch stopWatch;

    /**
     * Setup method for each test case.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setup() {
        stopWatch = new StopWatch();
    }

    /**
     * Test method for the start() and stop() methods.
     */
    @Test
    public void testStartAndStop() {
        stopWatch.start();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // the test itself takes some time, so the actual time should be greater than 0
        Assert.assertTrue("Time should be greater than 0", actualTime > expectedTime);
        stopWatch.reset();
    }

    /**
     * Test method for the split() and unsplit() methods.
     */
    @Test
    public void testSplitAndUnsplit() {
        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        long actualSplitTime = stopWatch.getSplitTime();
        long expectedSplitTime = 0; // the test itself takes some time, so the split time should be greater than 0
        Assert.assertTrue("Split time should be greater than 0", actualSplitTime > expectedSplitTime);
        stopWatch.unsplit();
        long actualTime = stopWatch.getTime();
        Assert.assertEquals("Total time should be the sum of the start time and the split time", actualTime, actualSplitTime);
        stopWatch.reset();
    }

    /**
     * Test method for the suspend() and resume() methods.
     */
    @Test
    public void testSuspendAndResume() {
        stopWatch.start();
        stopWatch.suspend();
        Date startDate = new Date(System.currentTimeMillis());
        stopWatch.resume();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = (new Date(System.currentTimeMillis())).getTime() - startDate.getTime(); // get the elapsed time since the resume call
        Assert.assertEquals("Total time should be the elapsed time between resume and stop", actualTime, expectedTime);
        stopWatch.reset();
    }

    /**
     * Test method for the illegal state exceptions.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptions() {
        stopWatch.start();
        stopWatch.stop(); // should throw an IllegalStateException since start() and stop() cannot be called twice without calling reset() first
        stopWatch.start(); // should throw an IllegalStateException since start() cannot be called twice without calling reset()

        stopWatch.split();
        stopWatch.unsplit(); // should throw an IllegalStateException since unsplit() may only be called if the watch has been split()
        stopWatch.split(); // should throw an IllegalStateException since split() cannot be invoked twice

        stopWatch.suspend();
        stopWatch.resume(); // should throw an IllegalStateException since resume() may only be called if the watch has been suspend()
        stopWatch.suspend(); // should throw an IllegalStateException since suspend() cannot be invoked twice
    }
}