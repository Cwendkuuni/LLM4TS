package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructorWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 30);
 assertEquals("Expected hours to be 13", 13, timeOfDay.getHours());
 assertEquals("Expected minutes to be 30", 30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructorWithTimeString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:45");
 assertEquals("Expected hours to be 14", 14, timeOfDay.getHours());
 assertEquals("Expected minutes to be 45", 45, timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructorWithInvalidTimeString() {
 try {
 new TimeOfDay("25:60");
 fail("Expected IllegalArgumentException but got none");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(14, 0);
 TimeOfDay timeOfDay3 = new TimeOfDay(13, 30);

 assertEquals("Expected timeOfDay1 > timeOfDay3", 1, timeOfDay1.compareTo(timeOfDay3));
 assertEquals("Expected timeOfDay1 < timeOfDay2", -1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals("Expected timeOfDay1 == timeOfDay3", 0, timeOfDay1.compareTo(timeOfDay3));

 timeOfDay3.setHours(14);
 assertEquals("Expected timeOfDay1 < timeOfDay3", -1, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testGetters() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 30);
 assertEquals("Expected hours to be 13", 13, timeOfDay.getHours());
 assertEquals("Expected minutes to be 30", 30, timeOfDay.getMinutes());
 }

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 30);
 assertEquals("Expected toString to return 13:30", "13:30", timeOfDay.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
 assertEquals("Expected hashCode to be equal for equal objects", timeOfDay1.hashCode(), timeOfDay2.hashCode());

 TimeOfDay timeOfDay3 = new TimeOfDay(14, 45);
 assertNotEquals("Expected different hashCodes for different objects", timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(14, 45);

 assertTrue("Expected timeOfDay1 to be equal to timeOfDay2", timeOfDay1.equals(timeOfDay2));
 assertFalse("Expected timeOfDay1 not to be equal to timeOfDay3", timeOfDay1.equals(timeOfDay3));
 assertFalse("Expected timeOfDay1 not to be equal to null", timeOfDay1.equals(null));
 }
}