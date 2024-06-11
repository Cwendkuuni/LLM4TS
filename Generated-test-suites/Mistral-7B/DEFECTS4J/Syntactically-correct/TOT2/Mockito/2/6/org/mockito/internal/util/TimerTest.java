package org.mockito.internal.util;

import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.internal.util.Timer;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting_TimerIsRunning() {
        timer.start();
        long currentTime = System.currentTimeMillis() - 500; // current time is 500ms less than start time
        assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerIsNotRunning() {
        timer = new Timer(1000);
        long currentTime = System.currentTimeMillis() + 1000; // current time is 1000ms greater than start time
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_TimerIsRunningAtStart() {
        timer.start();
        long currentTime = System.currentTimeMillis();
        assertEquals(currentTime, timer.getStartTime());
        assertTrue(timer.isCounting());
    }

    @Test
    public void testStart_TimerStartsCounting() {
        timer = new Timer(1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_NegativeDuration() {
        timer = new Timer(-1000);
        timer.start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStart_TimerStartsWithZeroDuration() {
        timer = new Timer(0);
        timer.start();
    }

    @Test
    public void testStart_TimerStartsMultipleTimes() {
        timer = new Timer(1000);
        timer.start();
        timer.start();
        long currentTime = System.currentTimeMillis();
        assertTrue(timer.isCounting());
        assertTrue(timer.isCounting());
    }
}