package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay("14:30");
 TimeOfDay timeOfDay3 = new TimeOfDay(15, 0);
 TimeOfDay timeOfDay4 = new TimeOfDay("15:00");
 TimeOfDay timeOfDay5 = new TimeOfDay(13, 60); // Invalid time

 @Test
 public void testTimeOfDayConstructionWithInts() {
       assertEquals(timeOfDay1.getHours(), 14);
       assertEquals(timeOfDay1.getMinutes(), 30);
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
       assertEquals(timeOfDay2.getHours(), 14);
       assertEquals(timeOfDay2.getMinutes(), 30);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidString() {
       try {
           new TimeOfDay("13:60");
           fail("Expected an IllegalArgumentException");
       } catch (IllegalArgumentException e) {
           // Expected
       }
 }

 @Test
 public void testTimeOfDayToString() {
       assertEquals(timeOfDay1.toString(), "14:30");
       assertEquals(timeOfDay2.toString(), "14:30");
 }

 @Test
 public void testTimeOfDayCompareTo() {
       assertEquals(timeOfDay1.compareTo(timeOfDay2), 0);
       assertEquals(timeOfDay1.compareTo(timeOfDay3), -1);
       assertEquals(timeOfDay3.compareTo(timeOfDay1), 1);
       assertEquals(timeOfDay4.compareTo(timeOfDay3), 0);
 }

 @Test
 public void testTimeOfDayEquals() {
       assertTrue(timeOfDay1.equals(timeOfDay1));
       assertTrue(timeOfDay1.equals(timeOfDay2));
       assertFalse(timeOfDay1.equals(timeOfDay3));
       assertFalse(timeOfDay1.equals(new Object()));
 }

 @Test
 public void testTimeOfDayHashCode() {
       TimeOfDay timeOfDay6 = new TimeOfDay(14, 30);
       assertEquals(timeOfDay1.hashCode(), timeOfDay6.hashCode());
       assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }
}