package org.junit.Test;

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
    public void testTimerWithInvalidDuration() {
        Timer timer = new Timer(-1000);
    }

    @Test
    public void testTimerIsCounting() {
        Timer timer = new Timer(2000);
        timer.start();
        assertTrue(timer.isCounting());

        long delay = 3000;
        long currentTime = System.currentTimeMillis();
        while (timer.isCounting() && (currentTime - timer.getStartTime()) < delay) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime = System.currentTimeMillis();
        }
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerIsNotCountingInitially() {
        Timer timer = new Timer(1000);
        assertFalse(timer.isCounting());
    }
}