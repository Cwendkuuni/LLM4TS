package org.junit.platform.commons.internal.test.ignore;

import org.junit.Test;
import org.mockito.internal.util.Timer;
import static org.junit.Assert.*;

public class TimerTest {

    @Test
    public void testIsCounting_timerNotStarted() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerInProgress() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.startTime;
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerHasExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.startTime - timer.durationMillis;
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_timerHasNegativeDuration() {
        Timer timer = new Timer(-1000);
        timer.start();
        timer.isCounting();
    }

    @Test
    public void testStart_startTimer() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_startTimerWithZeroDuration() {
        Timer timer = new Timer(0);
        assertFalse(timer.isCounting());
        timer.start();
        assertTrue(timer.isCounting());
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.startTime;
        assertEquals("Timer should have expired with zero duration", 0, elapsedTime);
    }

    @Test(expected = IllegalStateException.class)
    public void testStart_timerAlreadyStarted() {
        Timer timer = new Timer(1000);
        timer.start();
        timer.start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_timerWithNegativeDuration() {
        Timer timer = new Timer(-1000);
        timer.start();
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_timerHasExpired() {
        Timer timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - timer.startTime - timer.durationMillis;
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerNotStartedButStartTimeIsSet() {
        Timer timer = new Timer(1000);
        timer.startTime = System.currentTimeMillis() - 1500;
        assertFalse(timer.isCounting());
    }
}