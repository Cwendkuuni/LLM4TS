package org.junit.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.Timer;

import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void testTimerIsCountingInitially() {
        assertFalse("Timer should not be counting initially", timer.isCounting());
    }

    @Test
    public void testTimerStartsCounting() {
        timer.start();
        long startTime = timer.startTime;
        assertTrue("Timer should be counting after start()", timer.isCounting());
        assertEquals("Current time minus start time should be less than or equal to duration",
                System.currentTimeMillis() - startTime, timer.durationMillis);
    }

    @Test(expected = AssertionError.class)
    public void testTimerThrowsAssertionErrorIfIsCountingIsCalledBeforeStart() {
        timer.isCounting();
    }

    @Test
    public void testTimerStopsCountingWhenDurationReachesZero() {
        timer.start();
        long startTime = timer.startTime;
        assertTrue("Timer should be counting after start()", timer.isCounting());
        Thread.yield(); // yielding the current thread to give some time for the timer to count down
        assertFalse("Timer should not be counting after duration has been reached", timer.isCounting());
        assertEquals("Current time minus start time should be equal to duration",
                System.currentTimeMillis() - startTime, timer.durationMillis);
    }

    @Test
    public void testTimerDoesNotCountWhenDurationHasBeenReachedAndIsCountingIsCalled() {
        timer.start();
        long startTime = timer.startTime;
        assertTrue("Timer should be counting after start()", timer.isCounting());
        Thread.yield(); // yielding the current thread to give some time for the timer to count down
        timer.isCounting();
        Thread.yield(); // yielding the current thread again to make sure the next assertion does not interfere with the timer
        assertFalse("Timer should not be counting after duration has been reached", timer.isCounting());
    }

    @Test
    public void testTimerDoesNotCountWhenStartIsCalledAfterIsCounting() {
        timer.isCounting();
        timer.start();
        assertFalse("Timer should not be counting after start() if isCounting() has been called before", timer.isCounting());
    }

}