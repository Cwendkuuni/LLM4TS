package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(10, 30);
 assertEquals(10, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:35");
 assertEquals(14, timeOfDay.getHours());
 assertEquals(35, timeOfDay.getMinutes());
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 15);
 TimeOfDay timeOfDay3 = new TimeOfDay(10, 30);

 assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(1, timeOfDay2.compareTo(timeOfDay1));
 }

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(14, 35);
 assertEquals("14:35", timeOfDay.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(14, 35);
 TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);

 assertNotNull(timeOfDay1.hashCode());
 assertNotSame(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(14, 35);
 TimeOfDay timeOfDay2 = new TimeOfDay(14, 35);
 TimeOfDay timeOfDay3 = new TimeOfDay(11, 15);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }
}