import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 private TimeOfDay timeOfDay1;
 private TimeOfDay timeOfDay2;
 private TimeOfDay timeOfDay3;

 @Test
 public void testTimeOfDayConstructorWithHoursAndMinutes() {
 timeOfDay1 = new TimeOfDay(10, 30);
 assertEquals("10:30", timeOfDay1.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 timeOfDay2 = new TimeOfDay("14:45");
 assertEquals("14:45", timeOfDay2.toString());
 }

 @Test
 public void testCompareTo() {
 timeOfDay1 = new TimeOfDay(11, 30);
 timeOfDay2 = new TimeOfDay(10, 45);
 assertLessThan(timeOfDay1.compareTo(timeOfDay2), 0);
 }

 @Test
 public void testGetHours() {
 timeOfDay3 = new TimeOfDay(15, 30);
 assertEquals(new Integer(15), timeOfDay3.getHours());
 }

 @Test
 public void testGetMinutes() {
 timeOfDay3 = new TimeOfDay(17, 45);
 assertEquals(new Integer(45), timeOfDay3.getMinutes());
 }

 // Edge cases and exception handling tests

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeOfDayHours() {
 timeOfDay1 = new TimeOfDay(-1, 30);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeOfDayMinutes() {
 timeOfDay1 = new TimeOfDay(1, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeOfDayStringHours() {
 timeOfDay1 = new TimeOfDay("12:59ABC", "Invalid hours format");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeOfDayStringMinutes() {
 timeOfDay1 = new TimeOfDay("12:60ABC", "Invalid minutes format");
 }

 @Test
 public void testEquals() {
 timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay4 = new TimeOfDay(12, 30);
 assertTrue(timeOfDay1.equals(timeOfDay4));
 }

 @Test
 public void testNotEquals() {
 timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay4 = new TimeOfDay(13, 30);
 assertFalse(timeOfDay1.equals(timeOfDay4));
 }

 @Test
 public void testHashCode() {
 timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay4 = new TimeOfDay(12, 30);
 assertEquals(timeOfDay1.hashCode(), timeOfDay4.hashCode());
 }
}