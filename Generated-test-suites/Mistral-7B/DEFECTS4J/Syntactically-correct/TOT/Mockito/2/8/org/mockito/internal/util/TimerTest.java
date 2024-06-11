package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TimerTest {

 private Timer timer;

 @Before
 public void setUp() {
 timer = new Timer(1000);
 }

 /**
 * Testing if the timer is initially not counting.
 */
 @Test
 public void testIsCountingBeforeStart() {
 assertFalse(timer.isCounting());
 }

 /**
 * Testing if the timer starts counting after the start method is called.
 */
 @Test
 public void testIsCountingAfterStart() {
 timer.start();
 long startTime = System.currentTimeMillis();
 assertTrue(timer.isCounting());
 long elapsedTime = System.currentTimeMillis() - startTime;
 assertTrue(elapsedTime <= timer.durationMillis);
 }

 /**
 * Testing if the timer stops counting after the duration has elapsed.
 */
 @Test
 public void testIsCountingAfterDurationElapsed() {
 timer.start();
 try {
 Thread.sleep(timer.durationMillis + 100);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertFalse(timer.isCounting());
 }

 /**
 * Testing if the start method can be called multiple times.
 */
 @Test
 public void testStartMultipleTimes() {
 timer.start();
 long startTime1 = System.currentTimeMillis();
 timer.start();
 long startTime2 = System.currentTimeMillis();
 assertTrue(timer.isCounting());
 assertTrue(startTime1 < startTime2);
 }

 /**
 * Testing if the timer throws an IllegalArgumentException when the duration is negative.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNegativeDuration() {
 new Timer(-1000);
 }
}