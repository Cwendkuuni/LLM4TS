import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testTimeOfDayConstructionWithTimeString() {
 TimeOfDay time = new TimeOfDay("14:25");
 assertEquals("14:25", time.toString());
 }

 @Test
 public void testCompareToMethod() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(14, 20);
 TimeOfDay time3 = new TimeOfDay(13, 30);

 assertTrue(time1.compareTo(time2) > 0);
 assertTrue(time1.compareTo(time3) == 0);
 }

 @Test
 public void testGetters() {
 TimeOfDay time = new TimeOfDay(15, 45);
 assertEquals(15, time.getHours().intValue());
 assertEquals(45, time.getMinutes().intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeConstructionWithInvalidHours() {
 new TimeOfDay(-1, 30);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeConstructionWithInvalidMinutes() {
 new TimeOfDay(11, 61);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeConstructionWithInvalidTimeString() {
 new TimeOfDay("25:30");
 }
}