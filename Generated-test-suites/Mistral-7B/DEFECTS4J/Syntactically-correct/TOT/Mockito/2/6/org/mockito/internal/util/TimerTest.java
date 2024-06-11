package org.mockito.internal.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TimerTest {

    private Timer timer;

    @BeforeEach
    public void setup() {
        timer = new Timer(1000);
    }

    @Test
    public void testStart() {
        timer.start();
        Assertions.assertNotEquals(-1, timer.getStartTime());
    }

    @Test
    public void testIsCounting_AfterStarting() {
        timer.start();
        Assertions.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_BeforeStarting() {
        Assertions.assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_ElapsedTimeLessThanOrEqualToDuration() {
        timer.start();
        Assertions.assertTrue(timer.isCounting());
    }

    @Test
    public void testIsCounting_ElapsedTimeGreaterThanDuration() {
        timer.start();
        long sleepTime = 2000;
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(timer.isCounting());
    }
}