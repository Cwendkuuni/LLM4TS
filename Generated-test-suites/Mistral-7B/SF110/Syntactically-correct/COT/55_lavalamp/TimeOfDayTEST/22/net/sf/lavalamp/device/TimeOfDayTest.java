package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;
 private TimeOfDay timeOfDay4;

 @Test
 public void testTimeOfDayConstructorWithInts() {
 timeOfDay1 = new TimeOfDay(12, 30);
 assertEquals("12:30", timeOfDay1.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 timeOfDay2 = new TimeOfDay("14:15");
 assertEquals("14:15", timeOfDay2.toString());
 }

 @Test
 public void testCompareToMethod() {
 timeOfDay1 = new TimeOfDay(12, 30);
 timeOfDay2 = new TimeOfDay(13, 30);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));

 timeOfDay1 = new TimeOfDay(13, 30);
 timeOfDay2 = new TimeOfDay(13, 45);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));

 timeOfDay1 = new TimeOfDay(13, 45);
 timeOfDay2 = new TimeOfDay(13, 45);
 assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeFormatInStringConstructor() {
 String invalidTime = "15:60";
 try {
 timeOfDay3 = new TimeOfDay(invalidTime);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 //Expected exception
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidHourFormatInStringConstructor() {
 String invalidHour = "25:30";
 try {
 timeOfDay4 = new TimeOfDay(invalidHour);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 //Expected exception
 }
 }

 @Test
 public void testGetters() {
 timeOfDay1 = new TimeOfDay(12, 30);
 assertEquals(12, timeOfDay1.getHours().intValue());
 assertEquals(30, timeOfDay1.getMinutes().intValue());
 }

 @Test
 public void testHashCodeMethod() {
 timeOfDay1 = new TimeOfDay(12, 30);
 timeOfDay2 = new TimeOfDay(12, 30);
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 timeOfDay1 = new TimeOfDay(12, 30);
 timeOfDay2 = new TimeOfDay(12, 30);
 assertTrue(timeOfDay1.equals(timeOfDay2));

 timeOfDay1 = new TimeOfDay(12, 30);
 timeOfDay2 = new TimeOfDay(12, 31);
 assertFalse(timeOfDay1.equals(timeOfDay2));

 timeOfDay1 = new TimeOfDay(12, 30);
 timeOfDay2 = new String("12:30");
 assertFalse(timeOfDay1.equals(timeOfDay2));
 }

}