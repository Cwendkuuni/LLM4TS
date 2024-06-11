package net.sf.lavalamp.device;
import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeConstructionWithValidHoursAndMinutes() {
 TimeOfDay validTime = new TimeOfDay(13, 30);
 assertEquals("Invalid time constructed: expected hours = 13, minutes = 30", "13:30", validTime.toString());
 }

 @Test
 public void testTimeConstructionWithValidTimeString() {
 TimeOfDay validTime = new TimeOfDay("14:45");
 assertEquals("Invalid time constructed: expected hours = 14, minutes = 45", "14:45", validTime.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeConstructionWithInvalidTimeStringHours() {
 new TimeOfDay("13:60");
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeConstructionWithInvalidTimeStringMinutes() {
 new TimeOfDay("13:70");
 }

 @Test
 public void testTimeComparison() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(14, 15);
 TimeOfDay time3 = new TimeOfDay(13, 30);

 assertTrue("Invalid time comparison result: expected time1 > time3", time1.compareTo(time3) > 0);
 assertTrue("Invalid time comparison result: expected time1 < time2", time1.compareTo(time2) < 0);
 assertTrue("Invalid time comparison result: expected time1 == time3", time1.compareTo(time3) == 0);
 }

 @Test
 public void testGetters() {
 TimeOfDay time = new TimeOfDay(13, 30);
 assertEquals("Invalid getter result for hours", 13, time.getHours().intValue());
 assertEquals("Invalid getter result for minutes", 30, time.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(13, 30);
 assertEquals("Invalid toString result", "13:30", time.toString());
 }

}