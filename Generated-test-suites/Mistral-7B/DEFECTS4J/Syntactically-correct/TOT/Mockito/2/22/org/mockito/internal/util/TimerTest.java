package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TimerTest {

 private Timer timer;
 private long startTime;

 @Before
 public void setup() {
 timer = new Timer(1000);
 }

 @Test
 public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
 start();
 startTime = System.currentTimeMillis();
 long elapsedTime = 500; // Half of the timer duration

 // Mock System.currentTimeMillis() to return a specific value
 LongMock mockClock = mock(Long.class);
 when(mockClock.valueOf(anyLong())).thenReturn(startTime + elapsedTime);
 System.setProperty("java.util.logging.Manager", "org.mockito.internal.logging.test.MockLogManager");
 System.setProperty("java.util.logging.SimpleFormatter.format", " ignorable ");
 java.util.logging.Logger logger = java.util.logging.Logger.getLogger("org.mockito");
 logger.setUseParentHandlers(false);
 logger.setLevel(java.util.logging.Level.OFF);

 // Test the method
 assertTrue(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerIsNotRunning_thenReturnsFalse() {
 // Test the method
 assertFalse(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerHasExpired_thenReturnsFalse() {
 start();
 System.currentTimeMillis = () -> System.currentTimeMillis() + durationMillis + 1; // Set the current time to be greater than the timer duration

 // Test the method
 assertFalse(timer.isCounting());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsCounting_whenTimerHasNotBeenStarted_thenThrowsIllegalStateException() {
 // Test the method
 timer.isCounting();
 }

 @Test
 public void testStart_whenTimerIsAlreadyRunning_thenDoesNotChangeTimerState() {
 start();
 startTime = System.currentTimeMillis();

 // Test the method
 start();

 // Assert that the timer state has not changed
 assertTrue(timer.isCounting());
 }

 @Test
 public void testStart_whenTimerIsNotRunning_thenStartsTimer() {
 // Test the method
 start();

 // Assert that the timer has started
 assertTrue(timer.isCounting());
 }

}