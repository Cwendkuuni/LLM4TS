package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructorWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 assertEquals("12:30", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithTimeString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:25");
 assertEquals("14:25", timeOfDay.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructorWithInvalidTimeString() {
 try {
 new TimeOfDay("15:60");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid time format: 60 is not a valid minute value", e.getMessage());
 }
 }

 @Test
 public void testGetters() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 35);
 assertEquals(15, timeOfDay.getHours().intValue());
 assertEquals(35, timeOfDay.getMinutes().intValue());
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 45);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 0);

 assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
 assertTrue(timeOfDay1.compareTo(timeOfDay3) < 0);
 assertTrue(timeOfDay2.compareTo(timeOfDay3) > 0);
 }

 @Test
 public void testEqualsAndHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay4 = null;

 assertEquals(timeOfDay1, timeOfDay1);
 assertEquals(timeOfDay1, timeOfDay2);
 assertNotEquals(timeOfDay1, timeOfDay3);
 assertNull(timeOfDay1.equals(timeOfDay4));
 }
}