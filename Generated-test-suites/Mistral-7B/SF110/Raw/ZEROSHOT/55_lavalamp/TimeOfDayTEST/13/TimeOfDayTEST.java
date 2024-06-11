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
 public void testTimeOfDayConstructionWithTimeString() {
 TimeOfDay timeOfDay = new TimeOfDay("10:30");
 assertEquals(10, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidHourString() {
 try {
 new TimeOfDay("25:30");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidMinuteString() {
 try {
 new TimeOfDay("10:60");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testTimeOfDayCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(10, 30);

 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay timeOfDay = new TimeOfDay(10, 30);
 assertEquals("10:30", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testTimeOfDayEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }
}