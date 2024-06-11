package org.apache.commons.lang.time;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.DurationFormatUtils;
import java.util.Date;

public class StopWatchTest {

    @Test
    public void testStartAndStop() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // assuming the test is run instantly
        Assert.assertEquals(expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStartTwice() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    @Test
    public void testSplitAndUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long splitTime = stopWatch.split();
        Assert.assertNotEquals(-1, splitTime);
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // assuming the test is run instantly
        Assert.assertEquals(expectedTime, actualTime - splitTime);
        stopWatch.unsplit();
        actualTime = stopWatch.getTime();
        Assert.assertEquals(expectedTime + splitTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSplitBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    @Test
    public void testSuspendAndResume() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long suspendTime = stopWatch.suspend();
        stopWatch.resume();
        long actualTime = stopWatch.getTime();
        long expectedTime = suspendTime;
        Assert.assertEquals(expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testSuspendBeforeStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.suspend();
    }

    @Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long actualTime = stopWatch.getTime();
        stopWatch.reset();
        long expectedTime = 0;
        Assert.assertEquals(expectedTime, actualTime);
    }

    @Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long actualTime = stopWatch.getTime();
        long expectedTime = 0; // assuming the test is run instantly
        Assert.assertEquals(expectedTime, actualTime);
    }

    @Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long millis = stopWatch.getTime();
        String actualString = stopWatch.toString();
        String expectedString = DurationFormatUtils.formatDurationHMS(millis);
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testToSplitString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long millis = stopWatch.getTime();
        long splitMillis = millis / 2;
        stopWatch.split();
        String actualString = stopWatch.toSplitString();
        String expectedString = DurationFormatUtils.formatDurationHMS(splitMillis);
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testParseDurationFormatUtils() throws ParseException {
        String durationString = "1:2:3.456";
        long millis = DurationFormatUtils.parseDuration(durationString);
        Assert.assertEquals(1 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000 + 3 * 60 * 1000 + 456 * 1000, millis);
    }

    @Test
    public void testGetSplitTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long expectedTime = 0; // assuming the test is run instantly
        long actualTime = stopWatch.getSplitTime();
        Assert.assertEquals(expectedTime, actualTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetSplitTimeBeforeSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.getSplitTime();
    }
}