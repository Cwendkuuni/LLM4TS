package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCountingWhenTimerIsRunning() {
        timer.start();
        long startTime = System.currentTimeMillis();
        // Wait for some time less than the duration
        long elapsedTime = System.currentTimeMillis() - startTime;

        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCountingWhenTimerHasFinished() {
        timer.start();
        long startTime = System.currentTimeMillis();
        // Wait for some time more than the duration
        long elapsedTime = System.currentTimeMillis() - startTime + timer.durationMillis;

        assertFalse(timer.isCounting());
    }
}