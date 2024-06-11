package org.junit.test; // Replace with your test package if necessary

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @Test
    public void testTimerCreation() {
        Timer timer = new Timer(1000);
        Assert.assertNotNull(timer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testIsCountingFalseBeforeStart() {
        Timer timer = new Timer(1000);
        Assert.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCountingTrueAfterStart() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 500;
        long currentTime = startTime + elapsedTime;

        Assert.assertTrue(timer.isCounting());
        Assert.assertTrue(System.currentTimeMillis() - startTime <= timer.durationMillis);
        Assert.assertTrue(currentTime - startTime <= timer.durationMillis);
    }

    @Test
    public void testIsCountingFalseAfterDurationReached() {
        Timer timer = new Timer(1000);
        timer.start();
        long startTime = System.currentTimeMillis();
        long elapsedTime = timer.durationMillis + 500;

        timer.start(); // Start the timer again to make the test last longer than the duration

        long currentTime = startTime + elapsedTime;

        Assert.assertFalse(timer.isCounting());
        Assert.assertFalse(System.currentTimeMillis() - startTime <= timer.durationMillis);
        Assert.assertFalse(currentTime - startTime <= timer.durationMillis);
    }
}