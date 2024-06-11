package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;
 private TimeOfDay timeOfDay4;

 @Test
 public void testTimeOfDayConstructorWithHoursAndMinutes() {
 timeOfDay1 = new TimeOfDay(10, 30);
 assertEquals(10, timeOfDay1.getHours());
 assertEquals(30, timeOfDay1.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructorWithTimeString() {
 timeOfDay2 = new TimeOfDay("14:45");
 assertEquals(14, timeOfDay2.getHours());
 assertEquals(45, timeOfDay2.getMinutes());
 }

 @Test
 public void testCompareTo() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(11, 15);
 assertLessThan(timeOfDay1, timeOfDay2);
 }

 @Test
 public void testCompareToEqual() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay3 = new TimeOfDay(10, 30);
 assertEquals(timeOfDay1, timeOfDay3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeFormatHours() {
 timeOfDay3 = new TimeOfDay("15:60");
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeFormatMinutes() {
 timeOfDay4 = new TimeOfDay("10:60");
 fail("Expected IllegalArgumentException not thrown");
 }

 @Test
 public void testToString() {
 timeOfDay1 = new TimeOfDay(10, 30);
 assertEquals("10:30", timeOfDay1.toString());
 }

 @Test
 public void testHashCode() {
 timeOfDay1 = new TimeOfDay(10, 30);
 int hashCode = timeOfDay1.hashCode();
 timeOfDay2 = new TimeOfDay(11, 15);
 assertNotSame(hashCode, timeOfDay2.hashCode());
 }

 @Test
 public void testEquals() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay3 = new TimeOfDay(10, 30);
 assertTrue(timeOfDay1.equals(timeOfDay3));
 }

 @Test
 public void testNotEquals() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(11, 15);
 assertFalse(timeOfDay1.equals(timeOfDay2));
 }

 @Test
 public void testNull() {
 timeOfDay1 = new TimeOfDay(10, 30);
 assertFalse(timeOfDay1.equals(null));
 }

 @Test
 public void testCompareToSameHoursDifferentMinutes() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(10, 45);
 assertLessThan(timeOfDay1, timeOfDay2);
 }

 @Test
 public void testCompareToSameHoursSameMinutes() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(10, 30);
 assertEquals(timeOfDay1, timeOfDay2);
 }

 @Test
 public void testCompareToDifferentHoursSameMinutes() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(11, 30);
 assertLessThan(timeOfDay1, timeOfDay2);
 }

 @Test
 public void testCompareToDifferentHoursDifferentMinutes() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(11, 45);
 assertLessThan(timeOfDay1, timeOfDay2);
 }
}