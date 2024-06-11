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

 /**
 * Test isCounting() method when timer is not started
 */
 @Test
 public void testIsCountingBeforeStart() {
 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method when timer is counting
 */
 @Test
 public void testIsCountingWhileCounting() {
 timer.start();
 long startTime = System.currentTimeMillis();
 // Simulate some time passing
 Thread.sleep(500);
 assertTrue(timer.isCounting());
 assertTrue(System.currentTimeMillis() - startTime <= timer.durationMillis);
 }

 /**
 * Test isCounting() method when timer has elapsed
 */
 @Test
 public void testIsCountingAfterElapsed() {
 timer.start();
 Thread.sleep(timer.durationMillis + 500);
 assertFalse(timer.isCounting());
 }

 /**
 * Test isCounting() method when start time is set incorrectly
 */
 @Test(expected = AssertionError.class)
 public void testIsCountingWithInvalidStartTime() {
 timer = new Timer(1000);
 timer.startTime = 0;
 timer.isCounting();
 }

 /**
 * Test isCounting() method when durationMillis is negative
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsCountingWithNegativeDurationMillis() {
 timer = new Timer(-1000);
 }

}