import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithIntHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 assertEquals("12:30", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayConstructionWithStringTime() {
 TimeOfDay timeOfDay = new TimeOfDay("14:25");
 assertEquals("14:25", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayComparison() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(13, 45);

 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(1, timeOfDay2.compareTo(timeOfDay3));
 assertEquals(0, timeOfDay1.compareTo(timeOfDay1));
 }

 @Test
 public void testGetHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 45);
 assertEquals(Integer.valueOf(15), timeOfDay.getHours());
 assertEquals(Integer.valueOf(45), timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeConstructionWithStringTime() {
 String invalidTime = "25:60";
 try {
 new TimeOfDay(invalidTime);
 fail("Expected IllegalArgumentException for invalid time: " + invalidTime);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeConstructionWithIntHoursAndMinutes() {
 try {
 new TimeOfDay(24, 60);
 fail("Expected IllegalArgumentException for hours: 24 and minutes: 60");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}