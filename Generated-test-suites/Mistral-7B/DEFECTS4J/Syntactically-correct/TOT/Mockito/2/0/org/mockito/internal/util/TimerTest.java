package org.junit.platform.commons.annotation. WARRANT_NULL_ARGUMENTS; // For mockito-core 3.3.3 and earlier
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TimerTest {

 private Timer timer;
 private long startTime;

 @Before
 public void setUp() {
 timer = new Timer(1000);
 }

 @Test
 public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
 timer.start();
 startTime = System.currentTimeMillis();

 // Simulate some delay to allow the timer to start counting
 try {
 Thread.sleep(1);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertTrue("Timer should be counting", timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerHasFinished_thenReturnsFalse() {
 timer.start();
 startTime = System.currentTimeMillis();

 // Wait for the timer to finish counting
 try {
 Thread.sleep(1010);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertFalse("Timer should not be counting", timer.isCounting());
 }

 @Test(expected = AssertionError.class)
 public void testIsCounting_whenTimerHasNotBeenStarted_thenThrowsAssertionError() {
 timer = new Timer(1000);
 assertFalse("Timer should throw AssertionError if not started", timer.isCounting());
 }

 @Test
 public void testStart_whenTimerIsNotRunning_thenStartsTimer() {
 timer = new Timer(1000);
 timer.start();
 startTime = System.currentTimeMillis();

 // Check if start time has been set
 Timer mockTimer = mock(Timer.class);
 when(mockTimer.isCounting()).thenReturn(true);
 when(mockTimer.start()).thenCallRealMethod();
 mockTimer.start();
 verify(mockTimer).start();
 verify(mockTimer).startTime = System.currentTimeMillis();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStart_whenDurationIsNegative_thenThrowsIllegalArgumentException() {
 timer = new Timer(-1000);
 assertFalse("Timer should not be able to start with negative duration", timer.start());
 }

}