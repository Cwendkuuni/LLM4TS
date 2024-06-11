package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;
 private TimeOfDay timeOfDay4;

 @Test
 public void testTimeOfDayConstructorWithInts() {
 timeOfDay1 = new TimeOfDay(5, 30);
 assertNotNull(timeOfDay1);
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 timeOfDay2 = new TimeOfDay("17:45");
 assertNotNull(timeOfDay2);
 assertEquals("17:45", timeOfDay2.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidStringFormat() {
 assertThrows(NumberFormatException.class, () -> new TimeOfDay("123"));
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidHoursFormat() {
 assertThrows(NumberFormatException.class, () -> new TimeOfDay("01:60"));
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidMinutesFormat() {
 assertThrows(NumberFormatException.class, () -> new TimeOfDay("23:60"));
 }

 @Test
 public void testCompareToSimpleCase() {
 timeOfDay1 = new TimeOfDay(5, 30);
 timeOfDay2 = new TimeOfDay(6, 30);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test
 public void testCompareToSameTime() {
 timeOfDay1 = new TimeOfDay(5, 30);
 timeOfDay2 = new TimeOfDay(5, 30);
 assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test
 public void testCompareToDifferentHoursSameMinutes() {
 timeOfDay1 = new TimeOfDay(5, 30);
 timeOfDay2 = new TimeOfDay(6, 30);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test
 public void testCompareToDifferentMinutesSameHours() {
 timeOfDay1 = new TimeOfDay(5, 30);
 timeOfDay2 = new TimeOfDay(5, 45);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test
 public void testCompareToSameMidnight() {
 timeOfDay1 = new TimeOfDay(0, 0);
 timeOfDay2 = new TimeOfDay(23, 59);
 timeOfDay3 = new TimeOfDay(0, 0);
 assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testCompareToSameMidnightDifferentObject() {
 timeOfDay1 = new TimeOfDay(0, 0);
 timeOfDay2 = new TimeOfDay(23, 59);
 TimeOfDay timeOfDay5 = new TimeOfDay(0, 0);
 assertEquals(0, timeOfDay1.compareTo(timeOfDay5));
 }

 @Test
 public void testGetHours() {
 timeOfDay1 = new TimeOfDay(5, 30);
 assertEquals(5, timeOfDay1.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 timeOfDay1 = new TimeOfDay(5, 30);
 assertEquals(30, timeOfDay1.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 timeOfDay1 = new TimeOfDay(5, 30);
 assertEquals("05:30", timeOfDay1.toString());
 }

 @Test
 public void testEqualsSameObject() {
 timeOfDay1 = new TimeOfDay(5, 30);
 assertEquals(timeOfDay1, timeOfDay1);
 }

 @Test
 public void testEqualsSameValue() {
 timeOfDay1 = new TimeOfDay(5, 30);
 TimeOfDay timeOfDay6 = new TimeOfDay(5, 30);
 assertEquals(timeOfDay1, timeOfDay6);
 }

 @Test
 public void testEqualsDifferentObjects() {
 timeOfDay1 = new TimeOfDay(5, 30);
 TimeOfDay timeOfDay5 = new TimeOfDay(3, 45);
 assertFalse(timeOfDay1.equals(timeOfDay5));
 }

 @Test
 public void testEqualsNull() {
 timeOfDay1 = new TimeOfDay(5, 30);
 assertFalse(timeOfDay1.equals(null));
 }

 @Test
 public void testEqualsDifferentClass() {
 timeOfDay1 = new TimeOfDay(5, 30);
 assertFalse(timeOfDay1.equals("Test"));
 }

 @Test
 public void testHashCodeSameValue() {
 timeOfDay1 = new TimeOfDay(5, 30);
 TimeOfDay timeOfDay6 = new TimeOfDay(5, 30);
 assertEquals(timeOfDay1.hashCode(), timeOfDay6.hashCode());
 }

 @Test
 public void testHashCodeDifferentValues() {
 timeOfDay1 = new TimeOfDay(5, 30);
 TimeOfDay timeOfDay5 = new TimeOfDay(3, 45);
 assertNotEquals(timeOfDay1.hashCode(), timeOfDay5.hashCode());
 }
}