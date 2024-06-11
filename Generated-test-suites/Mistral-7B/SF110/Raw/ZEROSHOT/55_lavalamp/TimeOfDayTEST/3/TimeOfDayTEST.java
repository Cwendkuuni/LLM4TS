package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(10, 30);
 assertEquals(10, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructionWithTimeString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:35");
 assertEquals(14, timeOfDay.getHours());
 assertEquals(35, timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidTimeString() {
 try {
 new TimeOfDay("14:65");
 fail("Expected IllegalArgumentException but no exception was thrown.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testTimeOfDayComparison() {
 TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 15);
 TimeOfDay timeOfDay3 = new TimeOfDay(10, 30);

 assertTrue(timeOfDay1.compareTo(timeOfDay3) == 0);
 assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
 }

 @Test
 public void testToStringMethod() {
 TimeOfDay timeOfDay = new TimeOfDay(14, 45);
 assertEquals("14:45", timeOfDay.toString());
 }

 @Test
 public void testHashCodeMethod() {
 TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 15);
 TimeOfDay timeOfDay3 = new TimeOfDay(10, 30);

 assertEquals(103045, timeOfDay1.hashCode());
 assertEquals(111546, timeOfDay2.hashCode());
 assertEquals(103045, timeOfDay3.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(11, 15);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay3)); // False expected
 assertTrue(timeOfDay1.equals(timeOfDay3)); // False expected
 }
}