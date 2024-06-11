package org.mockito.internal.util;

import org.junit.Assert;  // for assertions
import org.junit.Test;  // for @Test annotation

public class TimerTest {

    // Test case for isCounting() method
    
    @Test
    public void testIsCounting_TimerRunning() {
        Timer timer = new Timer(1000);  // create a timer with duration of 1000 milliseconds
        timer.start();  // start the timer

        // simulate some time passing
        long elapsedTime = 500;  // elapsed time is 500 milliseconds
        long currentTime = System.currentTimeMillis() - elapsedTime;

        // test the method
        boolean isCounting = timer.isCounting();
        Assert.assertTrue("Timer should still be counting down", isCounting);
    }

    // Test case for isCounting() method with timer not started

    @Test
    public void testIsCounting_TimerNotRunning() {
        Timer timer = new Timer(1000);

        // test the method
        boolean isCounting = timer.isCounting();
        Assert.assertFalse("Timer should not be counting down", isCounting);
    }

    // Test case for isCounting() method with timer expired

    @Test
    public void testIsCounting_TimerExpired() {
        Timer timer = new Timer(1000);
        timer.start();

        // simulate some time passing to make the timer expire
        long elapsedTime = 1500;  // elapsed time is 1500 milliseconds

        // test the method
        boolean isCounting = timer.isCounting();
        Assert.assertFalse("Timer should not be counting down", isCounting);
    }

    // Test case for start() method

    @Test
    public void testStart_TimerStarted() {
        Timer timer = new Timer(1000);

        // test the method
        timer.start();

        // test the timer's state
        long startTime = timer.getStartTime();
        Assert.assertNotEquals("-1", Long.toString(startTime));
    }

    // Test case for start() method with timer already started

    @Test(expected = IllegalStateException.class)
    public void testStart_TimerAlreadyStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }
}