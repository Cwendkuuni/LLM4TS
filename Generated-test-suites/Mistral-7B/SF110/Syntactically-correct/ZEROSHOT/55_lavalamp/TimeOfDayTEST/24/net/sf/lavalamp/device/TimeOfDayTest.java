package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(10, 30);
 assertEquals(10, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:45");
 assertEquals(14, timeOfDay.getHours());
 assertEquals(45, timeOfDay.getMinutes());
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayStringConstructionWithInvalidFormat() {
 try {
 new TimeOfDay("14:60");
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(14, 0);
 TimeOfDay timeOfDay3 = new TimeOfDay(13, 30);

 assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
 assertTrue(timeOfDay1.compareTo(timeOfDay3) == 0);
 assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
 }

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 30);
 assertEquals("13:30", timeOfDay.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(14, 45);

 assertNotSame(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(14, 45);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }
}