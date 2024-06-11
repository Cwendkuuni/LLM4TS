package org.apache.commons.lang.time;

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;

/**
 * JUnit 4 test file for the StopWatch class
 */
public class StopWatchTest {

    /**
     * Instance of StopWatch under test
     */
    private StopWatch stopWatch;

    /**
     * Setup method for creating a new instance of StopWatch for testing
     */
    @Before
    public void setup() {
        stopWatch = new StopWatch();
    }

    /**
     * Test the start() method
     */
    @Test
    public void shouldStartAndStopWatch() {
        stopWatch.start();
        stopWatch.stop();
        long actual = stopWatch.getTime();
        long expected = 0; // Test starts with 0 time before starting the watch
        Assert.assertEquals("Time should be equal to expected value", expected, actual);

        stopWatch.reset();
        stopWatch.start();
        stopWatch.stop();
        actual = stopWatch.getTime();
        long elapsed = System.currentTimeMillis() - stopWatch.getStartTime();
        Assert.assertEquals("Time should be equal to elapsed time", elapsed, actual);
    }

    /**
     * Test the split() method
     */
    @Test
    public void shouldSplitAndUnsplitWatch() {
        stopWatch.start();
        long startTime = stopWatch.getStartTime();
        stopWatch.split();
        stopWatch.stop();

        long actual = stopWatch.getTime();
        long expectedSplitTime = System.currentTimeMillis() - startTime;
        long expectedTotalTime = System.currentTimeMillis() - stopWatch.getStartTime();
        Assert.assertEquals("Split time should equal the time between start and split", expectedSplitTime, actual);
        Assert.assertEquals("Total time should equal the time between start and stop", expectedTotalTime, stopWatch.getTime());

        stopWatch.unsplit();
        actual = stopWatch.getTime();
        expectedTotalTime = System.currentTimeMillis() - stopWatch.getStartTime();
        Assert.assertEquals("Total time should be equal to the time between start and stop after unsplit", expectedTotalTime, actual);
    }

    /**
     * Test the suspend() and resume() methods
     */
    @Test
    public void shouldSuspendAndResumeWatch() {
        stopWatch.start();
        long startTime = stopWatch.getStartTime();
        stopWatch.suspend();
        long suspendTime = System.currentTimeMillis();
        stopWatch.resume();
        stopWatch.stop();

        long actual = stopWatch.getTime();
        long expectedTotalTime = System.currentTimeMillis() - startTime;
        long elapsed = System.currentTimeMillis() - (startTime + suspendTime);
        long expectedSuspendedTime = System.currentTimeMillis() - (startTime + suspendTime) - stopWatch.getTime();

        Assert.assertEquals("Total time should be equal to elapsed time between start and stop", expectedTotalTime, actual);
        Assert.assertEquals("Suspended time should be equal to the time between suspend and resume", expectedSuspendedTime, stopWatch.getSplitTime());
    }

    /**
     * Test the toString() and toSplitString() methods
     */
    @Test
    public void shouldDisplayTimeCorrectly() {
        stopWatch.start();
        stopWatch.stop();

        String actual = stopWatch.toString();
        String expected = "0:0:0.001"; // Test starts with 1 millisecond time
        Assert.assertEquals("ToString method should display time correctly", expected, actual);

        stopWatch.reset();
        stopWatch.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopWatch.stop();

        actual = stopWatch.toString();
        long actualTime = Long.parseLong(actual.split(":")[2]) * 1000;
        long expectedTime = 1000;
        Assert.assertEquals("ToString method should display time correctly for longer times", expectedTime, actualTime);

        stopWatch.split();
        stopWatch.stop();

        actual = stopWatch.toSplitString();
        actualTime = Long.parseLong(actual.split(":")[2]) * 1000;
        expectedTime = 1000;
        Assert.assertEquals("ToSplitString method should display split time correctly for longer times", expectedTime, actualTime);
    }
}