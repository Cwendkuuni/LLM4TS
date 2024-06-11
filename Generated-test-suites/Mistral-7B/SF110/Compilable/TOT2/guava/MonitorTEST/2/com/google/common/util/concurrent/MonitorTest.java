package com.google.common.util.concurrent;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MonitorTest {
   @Test
   public void testEnter() {
      Monitor monitor = new Monitor();
      boolean wasLocked = false;
      try {
         monitor.enter();
         wasLocked = monitor.isOccupiedByCurrentThread();
         assertTrue(wasLocked);
      } finally {
         monitor.leave();
      }
   }
}