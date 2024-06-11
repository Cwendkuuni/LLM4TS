package org.junit. test; // Replace this package name with the actual package name of your test file

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {

 private Timer timer;
 private final long DURATION_MILLIS = 1000;

 @Test
 public void testIsCounting_whenTimerIsNotStarted_thenShouldReturnFalse() {
 timer = new Timer(DURATION_MILLIS);
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerIsStarted_thenShouldReturnTrue() {
 // Mock System.currentTimeMillis() to return a fixed value
 TimerMock timerMock = mock(Timer.class);
 when(timerMock.isCounting()).thenCallRealMethod();
 when(timerMock.start()).thenReturn(null);
 when(timerMock.startTime).thenReturn(System.currentTimeMillis() - 500);

 timer = timerMock;
 timer.start();

 // Set System.currentTimeMillis() to a value less than or equal to startTime + durationMillis
 System.setCurrentTimeMillis(System.currentTimeMillis() - 1000);

 verify(timerMock).isCounting();
 assertTrue(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerHasElapsed_thenShouldReturnFalse() {
 timer = new Timer(DURATION_MILLIS);
 timer.start();
 System.setCurrentTimeMillis(System.currentTimeMillis() + DURATION_MILLIS + 1000);

 assertFalse(timer.isCounting());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsCounting_whenStartTimeIsNotSet_thenShouldThrowException() {
 timer = new Timer(DURATION_MILLIS);
 assertFalse(timer.isCounting());
 timer.isCounting();
 }

}