package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;
 private TimeOfDay timeOfDay4;
 private TimeOfDay timeOfDay5;

 @Test
 public void testTimeOfDayConstructorWithIntHoursIntMinutes() {
 timeOfDay1 = new TimeOfDay(12, 30);
 assertEquals("12:30", timeOfDay1.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithStringTime() {
 timeOfDay2 = new TimeOfDay("14:25");
 assertEquals("14:25", timeOfDay2.toString());
 }

 @Test
 public void testCompareToWithSameTime() {
 timeOfDay1 = new TimeOfDay(13, 45);
 timeOfDay3 = new TimeOfDay(13, 45);
 assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testCompareToWithDifferentHours() {
 timeOfDay1 = new TimeOfDay(13, 30);
 timeOfDay3 = new TimeOfDay(15, 0);
 assertTrue(timeOfDay1.compareTo(timeOfDay3) < 0);
 }

 @Test
 public void testCompareToWithDifferentMinutes() {
 timeOfDay1 = new TimeOfDay(13, 30);
 timeOfDay3 = new TimeOfDay(13, 45);
 assertTrue(timeOfDay1.compareTo(timeOfDay3) < 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidTimeFormatHours() {
 timeOfDay4 = new TimeOfDay("15ab:25");
 fail("Expected IllegalArgumentException, but got: " + timeOfDay4);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidTimeFormatMinutes() {
 timeOfDay4 = new TimeOfDay("12:60");
 fail("Expected IllegalArgumentException, but got: " + timeOfDay4);
 }

 @Test
 public void testGetHours() {
 timeOfDay1 = new TimeOfDay(14, 35);
 assertEquals(14, timeOfDay1.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 timeOfDay1 = new TimeOfDay(14, 35);
 assertEquals(35, timeOfDay1.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 timeOfDay1 = new TimeOfDay(14, 35);
 assertEquals("14:35", timeOfDay1.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsWithNullObject() {
 timeOfDay1 = new TimeOfDay(14, 35);
 assertFalse(timeOfDay1.equals(null));
 }

 @Test
 public void testEqualsWithDifferentTypeObject() {
 timeOfDay1 = new TimeOfDay(14, 35);
 assertFalse(timeOfDay1.equals(new Object()));
 }

 @Test
 public void testEqualsWithSameObject() {
 timeOfDay1 = new TimeOfDay(14, 35);
 timeOfDay5 = timeOfDay1;
 assertTrue(timeOfDay1.equals(timeOfDay5));
 }

 @Test
 public void testEqualsWithDifferentTime() {
 timeOfDay1 = new TimeOfDay(14, 35);
 timeOfDay2 = new TimeOfDay(13, 45);
 assertFalse(timeOfDay1.equals(timeOfDay2));
 }

 @Test
 public void testHashCodeWithSameTime() {
 timeOfDay1 = new TimeOfDay(14, 35);
 timeOfDay3 = new TimeOfDay(14, 35);
 assertEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentTime() {
 timeOfDay1 = new TimeOfDay(14, 35);
 timeOfDay2 = new TimeOfDay(13, 45);
 assertNotSame(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }
}