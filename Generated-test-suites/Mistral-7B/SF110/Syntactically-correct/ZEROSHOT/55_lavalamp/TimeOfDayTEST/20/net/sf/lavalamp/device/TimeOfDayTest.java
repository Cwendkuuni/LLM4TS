package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructorWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(10, 30);
 assertEquals("10:30", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:59");
 assertEquals("14:59", timeOfDay.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayStringWithInvalidFormat() {
 try {
 new TimeOfDay("15:60");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetters() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 15);
 assertEquals(11, timeOfDay.getHours().intValue());
 assertEquals(15, timeOfDay.getMinutes().intValue());
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 0);
 TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

 assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 45);
 assertEquals("15:45", timeOfDay.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 0);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }
}