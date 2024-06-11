package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TimeOfDayTest {

 // Test constructor with int hours and int minutes
 @Test
 public void testTimeOfDayConstructorIntInt() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 TimeOfDay expectedTimeOfDay = new TimeOfDay(12, 30);
 assertEquals(expectedTimeOfDay, timeOfDay);
 }

 // Test constructor with String time
 @Test
 public void testTimeOfDayConstructorString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:45");
 TimeOfDay expectedTimeOfDay = new TimeOfDay(14, 45);
 assertEquals(expectedTimeOfDay, timeOfDay);
 }

 // Test getters

 @Test
 public void testGetHours() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 30);
 Integer hours = timeOfDay.getHours();
 assertEquals(15, hours.intValue());
 }

 @Test
 public void testGetMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 30);
 Integer minutes = timeOfDay.getMinutes();
 assertEquals(30, minutes.intValue());
 }

 // Test compareTo method

 @Test
 public void testCompareToSameTime() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
 assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test
 public void testCompareToDifferentHoursSameMinutes() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test
 public void testCompareToDifferentHoursDifferentMinutes() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 35);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test
 public void testCompareToNull() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 assertNull(timeOfDay.compareTo(null));
 }

 // Test toString method

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 String expectedToString = "12:30";
 assertEquals(expectedToString, timeOfDay.toString());
 }

 // Test hashCode method

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 // Test equals method

 @Test
 public void testEqualsSameObject() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = timeOfDay1;
 assertTrue(timeOfDay1.equals(timeOfDay2));
 }

 @Test
 public void testEqualsDifferentObjectsSameValues() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
 assertTrue(timeOfDay1.equals(timeOfDay2));
 }

 @Test
 public void testEqualsDifferentObjectsDifferentValues() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 35);
 assertFalse(timeOfDay1.equals(timeOfDay2));
 }

 @Test
 public void testEqualsNull() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 assertFalse(timeOfDay.equals(null));
 }

 // Test exception handling: constructor with int hours and int minutes when hours is < 0
 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructorIntIntNegativeHours() {
 new TimeOfDay(-1, 30);
 }

 // Test exception handling: constructor with String time when length is not 5
 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructorStringInvalidLength() {
 new TimeOfDay("12:3");
 }

 // Test exception handling: constructor with String time when hours is < 0 or minutes is > 59
 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructorStringInvalidHoursOrMinutes() {
 new TimeOfDay("12:-1");
 new TimeOfDay("12:60");
 }
}