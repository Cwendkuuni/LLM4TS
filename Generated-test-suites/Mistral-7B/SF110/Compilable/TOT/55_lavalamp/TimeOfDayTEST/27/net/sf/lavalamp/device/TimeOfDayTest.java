package net.sf.lavalamp.device;
import org.junit.Test;import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithInts() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals(new TimeOfDay("12:30"), time);
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructionWithInvalidString() {
 try {
 new TimeOfDay("12:60");
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testTimeOfDayComparison() {
 TimeOfDay time1 = new TimeOfDay("12:30");
 TimeOfDay time2 = new TimeOfDay("11:45");
 TimeOfDay time3 = new TimeOfDay("12:30");

 assertTrue(time1.compareTo(time2) > 0);
 assertTrue(time1.compareTo(time3) == 0);
 }

 @Test
 public void testTimeOfDayGetters() {
 TimeOfDay time = new TimeOfDay("12:30");
 assertEquals(12, time.getHours().intValue());
 assertEquals(30, time.getMinutes().intValue());
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay time = new TimeOfDay("12:30");
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testTimeOfDayHashcodeAndEquals() {
 TimeOfDay time1 = new TimeOfDay("12:30");
 TimeOfDay time2 = new TimeOfDay("12:30");
 TimeOfDay time3 = new TimeOfDay("13:30");

 assertEquals(time1.hashCode(), time1.hashCode());
 assertEquals(time1.hashCode(), time2.hashCode());
 assertNotSame(time1.hashCode(), time3.hashCode());

 assertTrue(time1.equals(time1));
 assertTrue(time1.equals(time2));
 assertFalse(time1.equals(time3));
 }
}