package net.sf.lavalamp.device;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;

 @Before
 public void setUp() {
 timeOfDay1 = new TimeOfDay(13, 30);
 timeOfDay2 = new TimeOfDay("14:45");
 timeOfDay3 = new TimeOfDay("00:00");
 }

 @Test
 public void testConstructorWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 35);
 assertEquals(15, timeOfDay.getHours());
 assertEquals(35, timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidHoursValue() {
 try {
 new TimeOfDay(-1, 0);
 fail("Should throw IllegalArgumentException with message 'Invalid hours value'.");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid hours value", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMinutesValue() {
 try {
 new TimeOfDay(0, 60);
 fail("Should throw IllegalArgumentException with message 'Invalid minutes value'.");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid minutes value", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeFormat() {
 try {
 new TimeOfDay("13:60");
 fail("Should throw IllegalArgumentException with message 'Invalid time format'.");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid time format", e.getMessage());
 }
 }

 @Test
 public void testCompareTo() {
 assertTrue(timeOfDay1.compareTo(timeOfDay3) > 0);
 assertTrue(timeOfDay1.compareTo(timeOfDay2) <= 0);
 assertTrue(timeOfDay2.compareTo(timeOfDay3) > 0);
 }

 @Test
 public void testGetHours() {
 assertEquals(13, timeOfDay1.getHours().intValue());
 assertEquals(14, timeOfDay2.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 assertEquals(30, timeOfDay1.getMinutes().intValue());
 assertEquals(45, timeOfDay2.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 assertEquals("13:30", timeOfDay1.toString());
 assertEquals("14:45", timeOfDay2.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay4 = new TimeOfDay(13, 30);
 assertEquals(timeOfDay1.hashCode(), timeOfDay4.hashCode());
 }

 @Test
 public void testEquals() {
 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertFalse(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals("not a TimeOfDay"));
 }
}