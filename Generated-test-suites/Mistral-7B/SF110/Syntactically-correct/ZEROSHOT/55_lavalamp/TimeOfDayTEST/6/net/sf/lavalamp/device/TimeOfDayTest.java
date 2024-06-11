package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(10, 30);
 assertEquals("Expected hours to be 10", 10, timeOfDay.getHours());
 assertEquals("Expected minutes to be 30", 30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructionWithTimeString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:45");
 assertEquals("Expected hours to be 14", 14, timeOfDay.getHours());
 assertEquals("Expected minutes to be 45", 45, timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidTimeString() {
 try {
 new TimeOfDay("14:61");
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 0);
 TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

 assertTrue("Expected timeOfDay1 > timeOfDay2", timeOfDay1.compareTo(timeOfDay2) > 0);
 assertTrue("Expected timeOfDay1 == timeOfDay3", timeOfDay1.compareTo(timeOfDay3) == 0);
 }

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 35);
 assertEquals("Expected toString to return 15:35", "15:35", timeOfDay.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 0);

 assertNotSame("Expected different hash codes for different TimeOfDay instances", timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 0);

 assertTrue("Expected timeOfDay1 == timeOfDay1", timeOfDay1.equals(timeOfDay1));
 assertTrue("Expected timeOfDay1 == timeOfDay2", timeOfDay1.equals(timeOfDay2));
 assertFalse("Expected timeOfDay1 != timeOfDay3", timeOfDay1.equals(timeOfDay3));
 }
}