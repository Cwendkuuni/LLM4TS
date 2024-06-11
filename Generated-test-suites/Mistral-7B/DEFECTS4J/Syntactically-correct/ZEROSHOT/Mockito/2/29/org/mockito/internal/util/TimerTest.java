package org.junit.test; // replace with your testing package name

import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testTimerWithValidDuration() {
        Timer timer = new Timer(1000);
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimerWithNegativeDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testTimerIsCountingFalseAfterExpiration() {
        Timer timer = new Timer(100);
        timer.start();
        timer.start(); // start it again to test expiration
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerIsCountingTrueBeforeExpiration() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.getStartTime();
        assertTrue(elapsedTime <= timer.getDurationMillis());
        assertTrue(timer.isCounting());
    }

}