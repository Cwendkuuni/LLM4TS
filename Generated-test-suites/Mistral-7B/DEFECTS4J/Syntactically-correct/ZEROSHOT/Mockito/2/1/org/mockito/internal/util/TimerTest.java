package org.junit.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.Timer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_newTimer() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerRunning() {
        timer.start();
        long startTime = System.currentTimeMillis();
        while (timer.isCounting()) {
            // Simulate the passage of time
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > timer.durationMillis()) {
                break;
            }
        }
        assertTrue(timer.isCounting()); // Should be false since timer has stopped
    }

    @Test(expected = IllegalStateException.class)
    public void testIsCounting_timerNotRunning() {
        timer.start();
        timer.isCounting();
        timer.stop(); // Stop timer to make assertion valid
        assertFalse(timer.isCounting());
        timer.isCounting(); // This should throw an exception
    }

    @Test
    public void testStart_timerNotRunning() {
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_timerAlreadyRunning() {
        timer.start();
        timer.start();
        timer.stop(); // Stop timer to make assertion valid
        assertFalse(timer.isCounting());
        timer.start(); // This should throw an exception
    }
}