package net.sf.lavalamp.device;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;
 private TimeOfDay timeOfDay4;

 @Before
 public void setUp() {
 timeOfDay1 = new TimeOfDay(12, 30);
 timeOfDay2 = new TimeOfDay("14:25");
 timeOfDay3 = new TimeOfDay("00:59");
 timeOfDay4 = new TimeOfDay("15:00");
 }

 @Test
 public void testTimeOfDayConstructionWithInts() {
 assertEquals(new TimeOfDay(12, 30), timeOfDay1);
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 assertEquals(new TimeOfDay("14:25"), timeOfDay2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidString() {
 // A TimeOfDay instance cannot be created with a string that does not represent a valid time format
 new TimeOfDay("25:60");
 }

 @Test
 public void testGetHours() {
 assertEquals(12, timeOfDay1.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 assertEquals(30, timeOfDay1.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 assertEquals("12:30", timeOfDay1.toString());
 }

 @Test
 public void testCompareTo() {
 // Compare TimeOfDay objects with different hours
 assertTrue(timeOfDay1.compareTo(new TimeOfDay(11, 30)) > 0);
 assertTrue(timeOfDay1.compareTo(new TimeOfDay(13, 30)) < 0);

 // Compare TimeOfDay objects with the same hours but different minutes
 assertTrue(timeOfDay1.compareTo(new TimeOfDay(12, 45)) < 0);
 assertTrue(timeOfDay1.compareTo(new TimeOfDay(12, 35)) > 0);

 // Compare TimeOfDay objects with the same hours and minutes
 assertEquals(0, timeOfDay1.compareTo(timeOfDay4));
 }

 @Test
 public void testHashCode() {
 final int timeOfDay1HashCode = timeOfDay1.hashCode();
 final int timeOfDay2HashCode = timeOfDay2.hashCode();
 final int timeOfDay3HashCode = timeOfDay3.hashCode();
 final int timeOfDay4HashCode = timeOfDay4.hashCode();

 // Hashcodes should be different for different TimeOfDay objects
 assertFalse(timeOfDay1HashCode == timeOfDay2HashCode);
 assertFalse(timeOfDay1HashCode == timeOfDay3HashCode);
 assertFalse(timeOfDay1HashCode == timeOfDay4HashCode);

 // Hashcodes should be equal for equal TimeOfDay objects
 assertTrue(timeOfDay1.hashCode() == timeOfDay1.hashCode());
 assertTrue(timeOfDay1.hashCode() == timeOfDay1.hashCode());
 assertTrue(timeOfDay1.hashCode() == timeOfDay4.hashCode());
 }

 @Test
 public void testEquals() {
 // Reflexive property
 assertTrue(timeOfDay1.equals(timeOfDay1));

 // Symmetric property
 assertTrue(timeOfDay1.equals(timeOfDay1));

 // Transitive property
 assertTrue(timeOfDay1.equals(timeOfDay4));
 assertTrue(timeOfDay4.equals(timeOfDay1));

 // Equality with null
 assertFalse(timeOfDay1.equals(null));

 // Inequality with different objects
 assertFalse(timeOfDay1.equals(new Object()));
 }
}