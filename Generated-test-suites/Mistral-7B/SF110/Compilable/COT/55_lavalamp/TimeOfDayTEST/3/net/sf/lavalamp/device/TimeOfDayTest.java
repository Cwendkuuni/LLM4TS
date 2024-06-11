package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;

 @Test
 public void testTimeOfDayConstructorWithHoursAndMinutes() {
 timeOfDay1 = new TimeOfDay(10, 30);
 assertEquals("10:30", timeOfDay1.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 timeOfDay2 = new TimeOfDay("14:45");
 assertEquals("14:45", timeOfDay2.toString());
 }

 @Test
 public void testCompareTo() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(12, 00);
 timeOfDay3 = new TimeOfDay(9, 30);

 assertEquals(1, timeOfDay1.compareTo(timeOfDay2)); // timeOfDay1 is greater
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay3)); // timeOfDay1 is greater than timeOfDay3
 assertEquals(0, timeOfDay2.compareTo(timeOfDay3)); // timeOfDay2 and timeOfDay3 are equal
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeFormatWithLessThanTwoDigitsForHours() {
 new TimeOfDay("5:");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeFormatWithLessThanTwoDigitsForMinutes() {
 new TimeOfDay("12:3");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeFormatWithMoreThanTwoDigitsForHours() {
 new TimeOfDay("123:30");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeFormatWithMoreThanTwoDigitsForMinutes() {
 new TimeOfDay("12:345");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeHours() {
 try {
 new TimeOfDay(-1, 0);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMinutes() {
 try {
 new TimeOfDay(0, -1);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetters() {
 timeOfDay1 = new TimeOfDay(10, 30);
 assertEquals(10, timeOfDay1.getHours().intValue());
 assertEquals(30, timeOfDay1.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 timeOfDay1 = new TimeOfDay(10, 30);
 assertEquals("10:30", timeOfDay1.toString());
 }

 @Test
 public void testHashCode() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(10, 30);
 timeOfDay3 = new TimeOfDay(11, 31);

 assertEquals(100 * 10 + 30, timeOfDay1.hashCode());
 assertEquals(100 * 11 + 31, timeOfDay3.hashCode());
 assertNotEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testEquals() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(10, 30);
 timeOfDay3 = new TimeOfDay(11, 31);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }

}