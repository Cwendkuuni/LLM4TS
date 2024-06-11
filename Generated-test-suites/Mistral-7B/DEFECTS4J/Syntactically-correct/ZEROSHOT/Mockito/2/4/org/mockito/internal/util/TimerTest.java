package org.junit.tests;

import org.junit.Test;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertFalse;
import static org.mockito.Mockito.assertTrue;
import static org.mockito.Mockito.mock;
import org.mockito.internal.util.Timer;

import java.util.concurrent.TimeUnit;

public class TimerTest {

    @Test
    public void testIsCounting_timerNotStarted() {
        Timer timer = new Timer(500);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerStarted() {
        Timer timer = new Timer(500);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - (timer.durationMillis / 2);
        long expectedEndTime = currentTime + timer.durationMillis - elapsedTime;

        System.setCurrentTimeMillis(elapsedTime);
        assertTrue(timer.isCounting());

        System.setCurrentTimeMillis(expectedEndTime);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testIsCounting_timerExpired() {
        Timer timer = new Timer(500);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime + timer.durationMillis;

        System.setCurrentTimeMillis(elapsedTime);
        assertFalse(timer.isCounting());
    }

    @Test(expected = AssertionError.class)
    public void testIsCounting_timerNotInitialized() {
        Timer timer = new Timer(500);
        timer.isCounting();
        timer = null;
    }

    @Test
    public void testStart_timerNotStarted() {
        Timer timer = new Timer(500);
        timer.start();
        verify(timer).start();
    }

    @Test
    public void testStart_timerAlreadyStarted() {
        Timer timer = spy(new Timer(500));
        timer.start();
        timer.start();
        verify(timer, Times.never()).start();
    }

    @Test
    public void testStart_timerExpired() {
        Timer timer = new Timer(500);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime + timer.durationMillis;

        System.setCurrentTimeMillis(elapsedTime);
        timer.start();
        verify(timer, Times.never()).start();
    }

    @Test
    public void testStart_timerInterrupted() {
        Timer timer = new Timer(500);
        timer.start();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(timer.durationMillis + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertTrue(timer.isCounting());
        });
        thread.start();

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime + timer.durationMillis;

        System.setCurrentTimeMillis(elapsedTime);
        timer.interrupt();
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerWithShortDuration() {
        Timer timer = new Timer(10);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime + 1;

        System.setCurrentTimeMillis(elapsedTime);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerWithLongDuration() {
        Timer timer = new Timer(10000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime + 2000;

        System.setCurrentTimeMillis(elapsedTime);
        assertTrue(timer.isCounting());

        elapsedTime += 8000;
        System.setCurrentTimeMillis(elapsedTime);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testTimerWithNegativeDuration() {
        Timer timer = new Timer(-1000);
        timer.start();
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime + 1;

        System.setCurrentTimeMillis(elapsedTime);
        assertFalse(timer.isCounting());
    }

    @Test
    public void testGetRemainingTime_timerNotStarted() {
        Timer timer = new Timer(500);
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        long remainingTime = timer.durationMillis - elapsedTime;

        assertFalse(timer.isCounting());
        assertEquals(remainingTime, timer.getRemainingTime());
    }

    @Test
    public void testGetRemainingTime_timerStarted() {
        Timer timer = new Timer(500);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime;
        long remainingTime = timer.durationMillis - elapsedTime;

        System.setCurrentTimeMillis(System.currentTimeMillis() + timer.durationMillis);
        assertEquals(0, timer.getRemainingTime());
    }

    @Test
    public void testGetRemainingTime_timerExpired() {
        Timer timer = new Timer(500);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime;

        System.setCurrentTimeMillis(elapsedTime + timer.durationMillis);
        assertEquals(-1, timer.getRemainingTime());
    }

    @Test
    public void testGetRemainingTime_timerInterrupted() {
        Timer timer = new Timer(500);
        timer.start();
        long elapsedTime = System.currentTimeMillis() - timer.startTime;

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(timer.durationMillis + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timer.interrupt();
        });
        thread.start();

        System.setCurrentTimeMillis(System.currentTimeMillis() + timer.durationMillis);
        assertEquals(-1, timer.getRemainingTime());
    }
}