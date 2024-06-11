package org.junit.test;

import org.junit.Assert;
import org.mockito.internal.util.Timer;

import static org.mockito.Mockito.spy;

public class TimerTest {

    @org.junit.Test
    public void testTimerWithValidDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testTimerWithNegativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @org.junit.Test
    public void testTimerIsCountingFalseAfterDurationExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long waitTime = 1500;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertFalse(timer.isCounting());
    }

    @org.junit.Test
    public void testTimerIsCountingTrueBeforeDurationExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long waitTime = 500;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(timer.isCounting());
    }

    @org.junit.Test
    public void testTimerIsCountingTrueImmediatelyAfterStarting() {
        Timer timer = new Timer(1000);
        timer.start();
        Assert.assertTrue(timer.isCounting());
    }

    @org.junit.Test
    public void testTimerIsNotCountingBeforeStarting() {
        Timer timer = new Timer(1000);
        Assert.assertFalse(timer.isCounting());
    }

    @org.junit.Test
    public void testSpyTimer() {
        Timer timerSpy = spy(new Timer(1000));
        timerSpy.start();
        Assert.assertTrue(timerSpy.isCounting());
    }

    @org.junit.Test
    public void testSpyTimerWithNegativeDuration() {
        Timer timerSpy = spy(new Timer(-1000));
    }

}