package net.sf.lavalamp.device;
import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructorWithValidHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(14, 30);
 assertEquals("14:30", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithValidTimeString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:30");
 assertEquals("14:30", timeOfDay.toString());
 }

 @Test
 public void testCompareToWithDifferentTimes() {
 TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(15, 0);
 int comparisonResult = timeOfDay1.compareTo(timeOfDay2);
 assertTrue(comparisonResult < 0);
 }

 @Test
 public void testCompareToWithSameTime() {
 TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(14, 30);
 int comparisonResult = timeOfDay1.compareTo(timeOfDay2);
 assertEquals(0, comparisonResult);
 }

 @Test
 public void testGetHours() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 30);
 assertEquals(15, timeOfDay.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 30);
 assertEquals(30, timeOfDay.getMinutes().intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidHoursValue() {
 new TimeOfDay(-1, 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMinutesValue() {
 new TimeOfDay(0, -1);
 }

 @Test
 public void testInvalidTimeFormat() {
 try {
 new TimeOfDay("14:60");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 30);
 assertEquals("15:30", timeOfDay.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(15, 30);
 assertNotEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(14, 30);
 assertTrue(timeOfDay1.equals(timeOfDay2));
 }

 @Test
 public void testNotEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(14, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(15, 0);
 assertFalse(timeOfDay1.equals(timeOfDay2));
 }
}