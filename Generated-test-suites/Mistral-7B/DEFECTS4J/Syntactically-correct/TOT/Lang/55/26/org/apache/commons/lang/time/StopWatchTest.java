package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

    @Test
    public void testBasicUsage() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long timeElapsed = stopWatch.getTime();
        stopWatch.stop();
        Assert.assertEquals("Time elapsed should be greater than 0", timeElapsed, 1000L);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStartBeforeStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.stop();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        stopWatch.unsplit();
        long totalTime = stopWatch.getTime();
        Assert.assertEquals("Total time should be equal to the sum of the split time and the remaining time", totalTime, splitTime + totalTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.suspend();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.resume();
        long totalTime = stopWatch.getTime();
        Assert.assertEquals("Total time should be less than the sum of the suspended time and the remaining time", totalTime, 1000L);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalSuspendBeforeRun() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long timeElapsed = stopWatch.getTime();
        stopWatch.reset();
        Assert.assertEquals("Reset should have set the time to 0", 0L, stopWatch.getTime());
    }
}