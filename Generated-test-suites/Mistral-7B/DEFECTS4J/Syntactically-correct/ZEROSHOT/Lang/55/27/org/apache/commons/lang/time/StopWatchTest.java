package org.apache.commons.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.time.format.DurationFormatUtils;

import java.text.ParseException;

/**
 * Test class for StopWatch class.
 *
 * @author John Doe
 */
public class StopWatchTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        StopWatch stopWatch = new StopWatch();
        assertNotNull(stopWatch);
    }

    /**
     * Test start method.
     */
    @org.junit.Test
    public void testStart() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        assertTrue(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
        assertTrue(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNotNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        String time = stopWatch.toString();
        assertEquals("0:0:0.0", time);
        String splitTime = stopWatch.toSplitString();
        assertEquals("0:0:0.0", splitTime);
    }

    /**
     * Test start method when stopwatch is already running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testStartWhenRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.start();
    }

    /**
     * Test reset method.
     */
    @org.junit.Test
    public void testReset() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.start();
        stopWatch.split();
        stopWatch.stop();
        stopWatch.reset();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    /**
     * Test split method.
     */
    @org.junit.Test
    public void testSplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertTrue(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNotNull(stopWatch.getSplitTime());
        String time = stopWatch.toString();
        assertEquals("0:0:0.0", time);
        String splitTime = stopWatch.toSplitString();
        assertEquals("0:0:0.0", splitTime);

        stopWatch.stop();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNotNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        time = stopWatch.toString();
        String expectedTime = "0:0:X";
        int index = time.indexOf(expectedTime);
        assertTrue(index != -1);
        splitTime = stopWatch.toSplitString();
        assertEquals("0:0:0.0", splitTime);
    }

    /**
     * Test unsplit method.
     */
    @org.junit.Test
    public void testUnsplit() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.stop();
        stopWatch.reset();
        stopWatch.start();
        stopWatch.split();
        stopWatch.unsplit();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    /**
     * Test split method when stopwatch is not running.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testSplitWhenNotRunning() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.split();
    }

    /**
     * Test suspend method.
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
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.resume();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        String time = stopWatch.toString();
        assertEquals("0:0:0.0", time);
        String splitTime = stopWatch.toSplitString();
        assertEquals("0:0:0.0", splitTime);
    }

    /**
     * Test resume method.
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
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());

        stopWatch.stop();
        stopWatch.reset();
        stopWatch.start();
        stopWatch.suspend();
        stopWatch.resume();
        assertFalse(stopWatch.isRunning());
        assertFalse(stopWatch.isSuspended());
        assertFalse(stopWatch.isStopped());
        assertFalse(stopWatch.isSplit());
        assertNull(stopWatch.getTime());
        assertNull(stopWatch.getSplitTime());
        assertNull(stopWatch.toString());
        assertNull(stopWatch.toSplitString());
    }

    /**
     * Test resume method when stopwatch is not suspended.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testResumeWhenNotSuspended() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.resume();
    }

    /**
     * Test getTime method.
     */
    @org.junit.Test
    public void testGetTime() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long elapsedTime = 1000;
        long expectedTime = System.currentTimeMillis() - stopWatch.getStartTime();
        assertEquals("The elapsed time is not equal to the expected time.", elapsedTime, expectedTime);
        stopWatch.stop();
        elapsedTime = stopWatch.getTime();
        assertEquals("The elapsed time is not equal to the expected time.", elapsedTime, expectedTime);

        stopWatch.reset();
        stopWatch.start();
        elapsedTime = 2000;
        stopWatch.split();
        expectedTime = System.currentTimeMillis() - stopWatch.getStartTime();
        assertEquals("The elapsed time is not equal to the expected time.", elapsedTime, expectedTime);
        long splitTime = stopWatch.getSplitTime();
        assertEquals("The split time is not equal to the expected time.", splitTime, elapsedTime);
        stopWatch.stop();
        elapsedTime = stopWatch.getTime();
        String timeString = stopWatch.toString();
        String splitTimeString = stopWatch.toSplitString();
        long totalTime = expectedTime + splitTime;
        assertEquals("The elapsed time is not equal to the expected time.", elapsedTime, totalTime);
        assertEquals("The time string is not equal to the expected time string.", timeString, DurationFormatUtils.formatDurationHMS(totalTime));
        assertEquals("The split time string is not equal to the expected split time string.", splitTimeString, DurationFormatUtils.formatDurationHMS(splitTime));
    }

    /**
     * Test getSplitTime method.
     */
    @org.junit.Test
    public void testGetSplitTime() throws ParseException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long elapsedTime = 1000;
        long expectedTime = System.currentTimeMillis() - stopWatch.getStartTime();
        assertEquals("The elapsed time is not equal to the expected time.", elapsedTime, expectedTime);
        stopWatch.split();
        long splitTime = stopWatch.getSplitTime();
        assertEquals("The split time is not equal to the expected time.", splitTime, elapsedTime / 2);
        stopWatch.stop();
        elapsedTime = stopWatch.getTime();
        assertEquals("The elapsed time is not equal to the expected time.", elapsedTime, expectedTime + splitTime);

        stopWatch.reset();
        stopWatch.start();
        elapsedTime = 2000;
        stopWatch.suspend();
        long suspendedTime = 500;
        stopWatch.resume();
        expectedTime = System.currentTimeMillis() - stopWatch.getStartTime();
        assertEquals("The elapsed time is not equal to the expected time.", elapsedTime, expectedTime);
        splitTime = stopWatch.getSplitTime();
        assertEquals("The split time is not equal to the expected split time.", splitTime, suspendedTime);
        stopWatch.stop();
        elapsedTime = stopWatch.getTime();
        String timeString = stopWatch.toString();
        String splitTimeString = stopWatch.toSplitString();
        long totalTime = expectedTime + splitTime;
        assertEquals("The elapsed time is not equal to the expected time.", elapsedTime, totalTime);
        assertEquals("The time string is not equal to the expected time string.", timeString, DurationFormatUtils.formatDurationHMS(totalTime));
        assertEquals("The split time string is not equal to the expected split time string.", splitTimeString, DurationFormatUtils.formatDurationHMS(splitTime));
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        long elapsedTime = 1000;
        long expectedTime = System.currentTimeMillis() - stopWatch.getStartTime();
        String timeString = stopWatch.toString();
        String expectedTimeString = DurationFormatUtils.formatDurationHMS(expectedTime);
        assertEquals("The time string is not equal to the expected time string.", timeString, expectedTimeString);
        stopWatch.split();
        String splitTimeString = stopWatch.toSplitString();
        String expectedSplitTimeString = DurationFormatUtils.formatDurationHMS(elapsedTime / 2);
        assertEquals("The split time string is not equal to the expected split time string.", splitTimeString, expectedSplitTimeString);
        stopWatch.stop();
        elapsedTime = stopWatch.getTime();
        timeString = stopWatch.toString();
        String totalTimeString = DurationFormatUtils.formatDurationHMS(elapsedTime);
        assertEquals("The time string is not equal to the expected time string.", timeString, totalTimeString);

        stopWatch.reset();
        stopWatch.start();
        elapsedTime = 2000;
        stopWatch.suspend();
        long suspendedTime = 500;
        stopWatch.resume();
        stopWatch.split();
        timeString = stopWatch.toString();
        String expectedTimeString2 = DurationFormatUtils.formatDurationHMS(elapsedTime);
        String expectedSplitTimeString2 = DurationFormatUtils.formatDurationHMS(suspendedTime);
        String expectedTotalTimeString = DurationFormatUtils.formatDurationHMS(elapsedTime + suspendedTime);
        assertEquals("The time string is not equal to the expected time string.", timeString, expectedTimeString2 + " (" + expectedTotalTimeString + ")");
        String expectedSplitTimeString3 = DurationFormatUtils.formatDurationHMS(suspendedTime / 2);
        String expectedSplitTimeString4 = DurationFormatUtils.formatDurationHMS(suspendedTime / 2);
        String expectedSplitTimeString5 = DurationFormatUtils.formatDurationHMS(suspendedTime);
        String expectedSplitTimeString6 = DurationFormatUtils.formatDurationHMS(suspendedTime);
        String expectedSplitTimeString7 = DurationFormatUtils.formatDurationHMS(suspendedTime / 2);
        String expectedSplitTimeString8 = DurationFormatUtils.formatDurationHMS(suspendedTime / 2);
        String[] expectedSplitTimeStrings = new String[] {expectedSplitTimeString3, expectedSplitTimeString4, expectedSplitTimeString5, expectedSplitTimeString6, expectedSplitTimeString7, expectedSplitTimeString8};
        String[] splitTimeStrings = timeString.split("\\s+");
        for (int i = 0; i < expectedSplitTimeStrings.length; i++) {
            assertEquals("The split time string at index " + i + " is not equal to the expected split time string.", expectedSplitTimeStrings[i], splitTimeStrings[i]);
        }
    }

}