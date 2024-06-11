package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(3, 45);
 assertEquals("03:45", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 TimeOfDay timeOfDay = new TimeOfDay("03:45");
 assertEquals("03:45", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayComparison() {
 TimeOfDay timeOfDay1 = new TimeOfDay(3, 45);
 TimeOfDay timeOfDay2 = new TimeOfDay(5, 30);
 assertEquals(1, timeOfDay1.compareTo(timeOfDay2));

 timeOfDay1 = new TimeOfDay(5, 30);
 timeOfDay2 = new TimeOfDay(5, 30);
 assertEquals(0, timeOfDay1.compareTo(timeOfDay2));

 timeOfDay1 = new TimeOfDay(3, 45);
 TimeOfDay timeOfDay3 = new TimeOfDay(3, 50);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testTimeOfDayGetters() {
 TimeOfDay timeOfDay = new TimeOfDay(3, 45);
 assertEquals(3, timeOfDay.getHours());
 assertEquals(45, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay timeOfDay = new TimeOfDay(3, 45);
 assertEquals("03:45", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayHashCodeEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(3, 45);
 TimeOfDay timeOfDay2 = new TimeOfDay(3, 45);
 TimeOfDay timeOfDay3 = new TimeOfDay(4, 45);

 assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 assertNotNull(timeOfDay1);
 assertNotNull(timeOfDay2);
 assertFalse(timeOfDay1.equals(timeOfDay3));
 assertFalse(timeOfDay3.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 }
}