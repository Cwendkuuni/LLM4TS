package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class TimerTest {

    @Test
    public void testTimerIsCountingWhenTimerIsRunning() {
        Timer timer = new Timer(1000); // timer sets to count down for 1 second
        timer.start();
        Assert.assertTrue("Timer should be counting", timer.isCounting());
    }

    @Test
    public void testTimerIsNotCountingWhenTimerIsFinished() {
        Timer timer = new Timer(1000);
        timer.start();
        try {
            Thread.sleep(1050); // sleep for 1.05 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse("Timer should not be counting", timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerThrowsExceptionWhenStartedTwice() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerThrowsExceptionWhenNegativeDurationIsProvided() {
        Timer timer = new Timer(-1000);
    }

    @Test(expected = IllegalStateException.class)
    public void testTimerThrowsExceptionWhenStartIsCalledBeforeDurationIsSet() {
        Timer timer = new Timer(-1);
        timer.start();
    }

    @Test
    public void testTimerIsCountingWhenTimerIsRestarted() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue("Timer should be counting", timer.isCounting());
        timer.start();
        Assert.assertTrue("Timer should still be counting", timer.isCounting());
    }

}