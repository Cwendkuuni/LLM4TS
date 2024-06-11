package org.apache.commons.lang.time;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class StopWatchTest {

 private StopWatch stopWatch;

 @Test
 public void testStartStop() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.stop();
 assertEquals(0, stopWatch.getTime());
 }

 @Test
 public void testSplitUnsplit() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.stop();
 assertEquals(0, stopWatch.getTime());
 }

 @Test
 public void testSplitAndUnsplitMultipleTimes() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.split();
 stopWatch.split();
 stopWatch.unsplit();
 stopWatch.unsplit();
 stopWatch.stop();
 assertEquals(0, stopWatch.getTime());
 }

 @Test
 public void testSuspendResume() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.stop();
 assertEquals(0, stopWatch.getTime());
 }

 @Test
 public void testSuspendResumeMultipleTimes() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.suspend();
 stopWatch.resume();
 stopWatch.stop();
 assertEquals(0, stopWatch.getTime());
 }

 @Test
 public void testIllegalMethodsCallOrder() {
 stopWatch = new StopWatch();
 assertThrows(IllegalStateException.class, stopWatch::stop);
 assertThrows(IllegalStateException.class, stopWatch::split);
 assertThrows(IllegalStateException.class, stopWatch::suspend);
 assertThrows(IllegalStateException.class, stopWatch::start);
 assertThrows(IllegalStateException.class, stopWatch::stop);
 }

 @Test
 public void testIllegalMethodsCallMultipleTimes() {
 stopWatch = new StopWatch();
 stopWatch.start();
 stopWatch.split();
 stopWatch.split();
 assertThrows(IllegalStateException.class, stopWatch::split);
 stopWatch.unsplit();
 stopWatch.unsplit();
 assertThrows(IllegalStateException.class, stopWatch::unsplit);
 stopWatch.stop();
 }

 @Test
 public void testNullPointerException() {
 assertNull(new StopWatch(null));
 }

 @Test
 public void testIllegalArgumentException() {
 assertThrows(IllegalArgumentException.class, () -> new StopWatch(-1));
 }

 @Test
 public void testGetTime() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Thread.yield();
 Thread.yield();
 stopWatch.stop();
 long time = stopWatch.getTime();
 assertTrue(time > 0);
 }

 @Test
 public void testGetSplitTime() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Thread.yield();
 Thread.yield();
 stopWatch.split();
 Thread.yield();
 Thread.yield();
 stopWatch.unsplit();
 Thread.yield();
 Thread.yield();
 stopWatch.stop();
 long time = stopWatch.getSplitTime();
 assertTrue(time > 0);
 }

 @Test
 public void testToStringAndToSplitString() {
 stopWatch = new StopWatch();
 stopWatch.start();
 Thread.yield();
 Thread.yield();
 stopWatch.split();
 Thread.yield();
 Thread.yield();
 stopWatch.unsplit();
 Thread.yield();
 Thread.yield();
 stopWatch.stop();
 String toString = stopWatch.toString();
 String toSplitString = stopWatch.toSplitString();
 assertNotNull(toString);
 assertNotNull(toSplitString);
 }
}