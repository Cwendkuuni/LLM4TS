package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimerTest {

 private Timer timer;

 @Before
 public void setUp() {
 timer = new Timer(1000);
 }

 @Test
 public void testIsCounting_whenTimerIsRunning_thenReturnsTrue() {
 timer.start();
 assertTrue(timer.isCounting());
 }

 @Test
 public void testIsCounting_whenTimerHasElapsed_thenReturnsFalse() {
 timer.start();
 try {
 Thread.sleep(1500);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertFalse(timer.isCounting());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsCounting_whenTimerHasNotBeenStarted_thenThrowsException() {
 assertTrue(timer.isCounting()); // This should fail with an assertion error
 }

 @Test
 public void testIsCounting_whenTimerHasBeenReset_thenReturnsFalse() {
 timer.start();
 timer.start();
 assertFalse(timer.isCounting());
 }

}