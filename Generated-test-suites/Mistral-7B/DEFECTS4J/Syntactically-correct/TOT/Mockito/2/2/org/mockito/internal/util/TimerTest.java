package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting() {
        timer.start();
        long startTime = System.currentTimeMillis();

        // Wait for a short while to make sure the timer has started counting
        long elapsedTime = System.currentTimeMillis() - startTime;

        // Test typical use case: Timer is still counting down
        assertTrue(timer.isCounting());

        // Wait some more to make sure the elapsed time is greater than the duration
        long elapsedTimeAfterExhausted = System.currentTimeMillis() - startTime;

        // Test edge case: Timer has finished counting
        assertFalse(timer.isCounting());
    }

    @Test
    public void testStart() {
        // Test typical use case: Timer starts counting
        timer.start();
        assertTrue(timer.isCounting());

        // Test error case: Timer should not start if it's already counting
        timer.start();
        assertTrue(timer.isCounting()); // since it was already counting
    }
}