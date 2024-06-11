package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructorWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(10, 30);
 assertEquals(10, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:35");
 assertEquals(14, timeOfDay.getHours());
 assertEquals(35, timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayStringWithInvalidFormat() {
 try {
 TimeOfDay timeOfDay = new TimeOfDay("14:60");
 } catch (IllegalArgumentException ex) {
 // expected exception
 }
 fail("Should have thrown an IllegalArgumentException");
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
 TimeOfDay timeOfDay = new TimeOfDay(13, 45);
 assertEquals("13:45", timeOfDay.toString());
 }

 @Test
 public void testHashCodeAndEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(15, 15);
 TimeOfDay timeOfDay2 = new TimeOfDay(15, 15);
 TimeOfDay timeOfDay3 = new TimeOfDay(16, 16);

 assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }

}