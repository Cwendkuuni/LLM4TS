import org.junit.Test;
import org.junit.Assert.*;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testConstructorWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(10, 30);
 assertEquals("10:30", time.toString());
 }

 @Test
 public void testConstructorWithTimeString() {
 TimeOfDay time = new TimeOfDay("14:35");
 assertEquals("14:35", time.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidHourInput() {
 try {
 new TimeOfDay("25:30");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidMinuteInput() {
 try {
 new TimeOfDay("10:60");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testComparableFunctionality() {
 TimeOfDay time1 = new TimeOfDay(14, 30);
 TimeOfDay time2 = new TimeOfDay(15, 0);
 assertTrue(time1.compareTo(time2) < 0);
 }

 @Test
 public void testEqualTimes() {
 TimeOfDay time1 = new TimeOfDay(14, 30);
 TimeOfDay time2 = new TimeOfDay(14, 30);
 assertEquals(time1, time2);
 }

 @Test
 public void testDifferentTimes() {
 TimeOfDay time1 = new TimeOfDay(14, 30);
 TimeOfDay time2 = new TimeOfDay(15, 15);
 assertFalse(time1.equals(time2));
 }

 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(14, 30);
 TimeOfDay time2 = new TimeOfDay(14, 30);
 assertEquals(time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(15, 45);
 assertEquals("15:45", time.toString());
 }
}