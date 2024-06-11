package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1; // instance of TimeOfDay with hours = 13 and minutes = 30
 private TimeOfDay timeOfDay2; // instance of TimeOfDay with hours = 14 and minutes = 45

 @Before
 public void setup() {
 timeOfDay1 = new TimeOfDay(13, 30);
 timeOfDay2 = new TimeOfDay("14:45");
 }

 @Test
 public void testConstructorWithIntHoursAndIntMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(5, 15);
 assertEquals(5, timeOfDay.getHours());
 assertEquals(15, timeOfDay.getMinutes());
 }

 @Test
 public void testConstructorWithStringTime() {
 TimeOfDay timeOfDay = new TimeOfDay("16:30");
 assertEquals(16, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test
 public void testCompareTo() {
 // Test when this time is earlier than another
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));

 // Test when this time is equal to another
 TimeOfDay equalTime = new TimeOfDay(13, 30);
 assertEquals(0, timeOfDay1.compareTo(equalTime));

 // Test when this time is later than another
 TimeOfDay laterTime = new TimeOfDay(14, 0);
 assertEquals(1, laterTime.compareTo(timeOfDay1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompareToWithNullArgument() {
 TimeOfDay.class.getMethod("compareTo", null);
 }

 @Test
 public void testGetHours() {
 assertEquals(13, timeOfDay1.getHours());
 }

 @Test
 public void testGetMinutes() {
 assertEquals(30, timeOfDay1.getMinutes());
 }

 @Test
 public void testToString() {
 assertEquals("13:30", timeOfDay1.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay3 = new TimeOfDay(13, 30);
 assertEquals(100 * 13 + 30, timeOfDay1.hashCode());
 assertEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }

 @Test
 public void testEquals() {
 // Test when this object is equal to itself
 assertTrue(timeOfDay1.equals(timeOfDay1));

 // Test when this object is equal to another instance with same value
 TimeOfDay timeOfDay4 = new TimeOfDay(13, 30);
 assertTrue(timeOfDay1.equals(timeOfDay4));

 // Test when this object is not equal to null
 assertFalse(timeOfDay1.equals(null));

 // Test when this object is not equal to another instance with different value
 TimeOfDay timeOfDay5 = new TimeOfDay(14, 45);
 assertFalse(timeOfDay1.equals(timeOfDay5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEqualsWithNullObject() {
 timeOfDay1.equals(null);
 }

 // Edge cases and exception handling test cases

 @Test
 public void testCompareToWithMinimumValue() {
 TimeOfDay minimumValue = new TimeOfDay(0, 0);
 TimeOfDay timeOfDay6 = new TimeOfDay(23, 59);
 assertEquals(-1, minimumValue.compareTo(timeOfDay6));
 }

 @Test
 public void testCompareToWithMaximumValue() {
 TimeOfDay maximumValue = new TimeOfDay(23, 59);
 TimeOfDay timeOfDay7 = new TimeOfDay(0, 0);
 assertEquals(1, maximumValue.compareTo(timeOfDay7));
 }

 @Test(expected = NumberFormatException.class)
 public void testConstructorWithInvalidTimeFormat() {
 new TimeOfDay("03:60");
 }

 @Test(expected = NumberFormatException.class)
 public void testConstructorWithInvalidHoursFormat() {
 new TimeOfDay("50:30");
 }

 @Test(expected = NumberFormatException.class)
 public void testConstructorWithInvalidMinutesFormat() {
 new TimeOfDay("12:60");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompareToWithNullTimeOfDay() {
 timeOfDay1.compareTo(null);
 }
}