package org.junit.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.Timer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_whenTimerIsNotStarted_thenReturnsFalse() {
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
        timer.start();
        long startTime = System.currentTimeMillis();
        // Wait for a short while to let the timer start counting
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime <= timer.durationMillis);
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_whenTimerIsNotInitialized_thenThrowsAnException() {
        new Timer(-1);
    }

    @Test
    public void testStart_whenTimerIsAlreadyRunning_thenDoesNotAffectTheTimer() {
        timer.start();
        timer.start();
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_whenTimerIsNotRunning_thenStartsTheTimer() {
        timer.start();
        long startTime = System.currentTimeMillis();
        // Wait for a short while to let the timer start counting
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(timer.isCounting());
        assertTrue(elapsedTime <= timer.durationMillis);
    }

    @Test
    public void testIsCounting_whenTimerHasExpired_thenReturnsFalse() {
        timer.start();
        long startTime = System.currentTimeMillis();
        // Wait for the timer to expire
        long elapsedTime = System.currentTimeMillis() - startTime;
        assertTrue(elapsedTime >= timer.durationMillis);
        assertFalse(timer.isCounting());
    }

}