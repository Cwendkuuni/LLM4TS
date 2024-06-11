package org.mockito.internal.util;

import org.junit.*;
import static org.junit.Assert.*;

public class TimerTest {

    private Timer timer;
    private long startTime;

    @Before
    public void setUp() {
        timer = new Timer(1000);
    }

    @Test
    public void testIsCounting() {
        // Test typical use case - timer is not counting
        assertFalse(timer.isCounting());

        // Test edge case - timer has not been started
        timer.start();
        assertFalse(timer.isCounting());

        // Test typical use case - timer is counting
        timer.start();
        assertTrue(timer.isCounting());

        // Test edge case - timer has just finished counting
        long endTime = System.currentTimeMillis();
        long waitTime = timer.durationMillis + (System.currentTimeMillis() - startTime);
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(!timer.isCounting());

        // Test exception handling - timer duration is negative
        Timer timerNegative = new Timer(-1000);
        assertFalse(timerNegative.isCounting());
    }

    @Test
    public void testStart() {
        // Test typical use case - timer starts counting
        timer.start();
        startTime = System.currentTimeMillis();
        assertEquals(startTime, timer.getStartTime());

        // Test edge case - timer has already been started
        timer.start();
        long newStartTime = System.currentTimeMillis();
        assertEquals(startTime, timer.getStartTime());

        // Test exception handling - timer constructor duration is negative
        Timer timerNegative = new Timer(-1000);
        assertFalse(timerNegative.isCounting());
    }

    @After
    public void tearDown() {
        timer = null;
        startTime = 0;
    }
}