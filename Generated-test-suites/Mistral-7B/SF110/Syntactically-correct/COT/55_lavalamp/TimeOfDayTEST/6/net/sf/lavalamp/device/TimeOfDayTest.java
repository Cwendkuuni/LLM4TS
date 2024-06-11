package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 timeOfDay1 = new TimeOfDay(10, 30);
 assertEquals("10:30", timeOfDay1.toString());
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 timeOfDay2 = new TimeOfDay("14:25");
 assertEquals("14:25", timeOfDay2.toString());
 }

 @Test
 public void testCompareTo() {
 timeOfDay1 = new TimeOfDay(11, 30);
 timeOfDay2 = new TimeOfDay(10, 35);
 timeOfDay3 = new TimeOfDay(11, 45);

 assertEquals(0, timeOfDay1.compareTo(timeOfDay1));
 assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(-1, timeOfDay2.compareTo(timeOfDay1));
 assertEquals(1, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testGetHours() {
 timeOfDay1 = new TimeOfDay(15, 45);
 assertEquals(15, timeOfDay1.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 timeOfDay1 = new TimeOfDay(15, 45);
 assertEquals(45, timeOfDay1.getMinutes().intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeFormat() {
 with(new Exception("Invalid time format: Should contain 2 digits for hours and 2 digits for minutes, but was '123:456'"))
 .given(new TimeOfDay("123:456"))
 .expect(IllegalArgumentException.class);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidHourValue() {
 with(new Exception("Invalid hour value: Should be a valid integer, but was '-1'"))
 .given(new TimeOfDay(-1, 30))
 .expect(NumberFormatException.class);
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidMinuteValue() {
 with(new Exception("Invalid minute value: Should be a valid integer, but was '60'"))
 .given(new TimeOfDay(12, 60))
 .expect(NumberFormatException.class);
 }

 @Test
 public void testToString() {
 timeOfDay1 = new TimeOfDay(1, 1);
 assertEquals("01:01", timeOfDay1.toString());
 }

 @Test
 public void testHashCode() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(10, 30);
 timeOfDay3 = new TimeOfDay(11, 35);

 assertEquals(Integer.hashCode(new Integer(10 * 100 + 30)), timeOfDay1.hashCode());
 assertEquals(Integer.hashCode(new Integer(10 * 100 + 30)), timeOfDay1.hashCode());
 assertNotEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 assertNotEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }

 @Test
 public void testEquals() {
 timeOfDay1 = new TimeOfDay(10, 30);
 timeOfDay2 = new TimeOfDay(10, 30);
 timeOfDay3 = new TimeOfDay(11, 35);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }

}