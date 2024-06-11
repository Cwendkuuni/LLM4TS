import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testConstructorWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("Unexpected hours value", 12, time.getHours());
 assertEquals("Unexpected minutes value", 30, time.getMinutes());
 }

 @Test
 public void testConstructorWithTimeString() {
 TimeOfDay time = new TimeOfDay("14:45");
 assertEquals("Unexpected hours value", 14, time.getHours());
 assertEquals("Unexpected minutes value", 45, time.getMinutes());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeStringFormat() {
 String invalidTime = "15:62"; // Minutes should be max 59
 TimeOfDay time = new TimeOfDay(invalidTime);
 fail("Should have thrown NumberFormatException");
 }

 @Test
 public void testCompareTo() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 15);
 TimeOfDay time3 = new TimeOfDay(12, 30);

 assertEquals("Unexpected comparison result between time1 and time2", 1, time1.compareTo(time2));
 assertEquals("Unexpected comparison result between time1 and time3", 0, time1.compareTo(time3));
 }

 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(15, 35);
 assertEquals("Unexpected toString result", "15:35", time.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 15);
 TimeOfDay time3 = new TimeOfDay(12, 30);

 assertEquals("Unexpected hashCode result for same object", time1.hashCode(), time1.hashCode());
 assertNotSame("Unexpected hashCode result for different objects", time1.hashCode(), time2.hashCode());
 assertNotSame("Unexpected hashCode result for same object", time1.hashCode(), time3.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 TimeOfDay time3 = new TimeOfDay(13, 15);

 assertTrue("Unexpected equals result for same object", time1.equals(time1));
 assertTrue("Unexpected equals result for same value", time1.equals(time2));
 assertFalse("Unexpected equals result for different objects", time1.equals(time3));
 }
}