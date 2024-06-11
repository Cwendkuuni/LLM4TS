package org.junit.test; // replace with your package name if needed

import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    @Test
    public void testTimerIsCounting() {
        Timer timer = new Timer(1000); // create a timer with duration of 1 second
        timer.start(); // start the timer

        assertTrue("Timer should be counting", timer.isCounting()); // check if timer is counting
    }

    @Test
    public void testTimerIsNotCounting_ElapsedTimeGreaterThanDuration() {
        Timer timer = new Timer(1000);
        timer.start(); // start the timer
        long waitTime = timer.durationMillis * 2; // wait more than the duration
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse("Timer should not be counting", timer.isCounting()); // check if timer is not counting
    }

    @Test(expected = AssertionError.class)
    public void testAssertStartTimeSet() {
        Timer timer = new Timer(1000);
        timer.isCounting(); // this will throw an AssertionError as startTime is not set
    }

}