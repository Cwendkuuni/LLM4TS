package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructorWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 assertEquals("Unexpected hours value", 12, timeOfDay.getHours());
 assertEquals("Unexpected minutes value", 30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructorWithTimeString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:45");
 assertEquals("Unexpected hours value", 14, timeOfDay.getHours());
 assertEquals("Unexpected minutes value", 45, timeOfDay.getMinutes());
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidTimeString() {
 new TimeOfDay("12:60");
 }

 @Test
 public void testCompareToMethod() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 15);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);

 assertEquals("Unexpected comparison result: 1 > 1", 1, timeOfDay2.compareTo(timeOfDay1));
 assertEquals("Unexpected comparison result: 1 = 1", 0, timeOfDay3.compareTo(timeOfDay1));
 }

 @Test
 public void testToStringMethod() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 assertEquals("Unexpected toString result", "12:30", timeOfDay.toString());
 }

 @Test
 public void testHashCodeMethod() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(14, 23);

 int timeOfDay1HashCode = timeOfDay1.hashCode();
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);
 int timeOfDay3HashCode = timeOfDay3.hashCode();

 assertEquals("Unexpected hashCode value for same TimeOfDay object", timeOfDay1HashCode, timeOfDay3.hashCode());
 assertNotSame("Unexpected hashCode value for different TimeOfDay objects", timeOfDay1HashCode, timeOfDay2.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(13, 15);

 assertTrue("Unexpected equals result for same TimeOfDay objects", timeOfDay1.equals(timeOfDay2));
 assertFalse("Unexpected equals result for different TimeOfDay objects", timeOfDay1.equals(timeOfDay3));
 }
}