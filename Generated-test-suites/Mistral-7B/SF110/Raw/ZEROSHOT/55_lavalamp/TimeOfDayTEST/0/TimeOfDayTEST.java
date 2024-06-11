package net.sf.lavalamp.device;

import org.junit.Test;
import org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayWithValidHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertEquals("Incorrect hours value", 11, timeOfDay.getHours());
 assertEquals("Incorrect minutes value", 30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayWithValidStringRepresentation() {
 TimeOfDay timeOfDay = new TimeOfDay("11:30");
 assertEquals("Incorrect hours value", 11, timeOfDay.getHours());
 assertEquals("Incorrect minutes value", 30, timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayWithInvalidHours() {
 try {
 new TimeOfDay(-1, 0);
 fail("Expected an IllegalArgumentException with an invalid hour value");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayWithInvalidMinutes() {
 try {
 new TimeOfDay(1, 60);
 fail("Expected an IllegalArgumentException with an invalid minute value");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testTimeOfDayComparison() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 45);
 TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

 assertEquals("Incorrect comparison result between timeOfDay1 and timeOfDay2", 1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals("Incorrect comparison result between timeOfDay1 and timeOfDay3", 0, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertEquals("Incorrect string representation of timeOfDay", "11:30", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 assertEquals("Incorrect hashcode value for equal TimeOfDay objects", timeOfDay1.hashCode(), timeOfDay2.hashCode());

 TimeOfDay timeOfDay3 = new TimeOfDay(12, 45);
 assertNotSame("Incorrect hashcode value for different TimeOfDay objects", timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }

 @Test
 public void testTimeOfDayEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 45);

 assertTrue("Incorrect equals result for equal TimeOfDay objects", timeOfDay1.equals(timeOfDay2));
 assertFalse("Incorrect equals result for different TimeOfDay objects", timeOfDay1.equals(timeOfDay3));
 }
}