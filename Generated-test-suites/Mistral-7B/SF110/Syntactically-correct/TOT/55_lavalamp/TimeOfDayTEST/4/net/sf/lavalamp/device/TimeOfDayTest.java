import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeConstructionWithInts() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 assertEquals("Unexpected hours value", 12, time1.getHours());
 assertEquals("Unexpected minutes value", 30, time1.getMinutes());
 }

 @Test
 public void testTimeConstructionWithString() {
 TimeOfDay time2 = new TimeOfDay("14:45");
 assertEquals("Unexpected hours value", 14, time2.getHours());
 assertEquals("Unexpected minutes value", 45, time2.getMinutes());
 }

 @Test
 public void testCompareToSameTime() {
 TimeOfDay time1 = new TimeOfDay(14, 45);
 TimeOfDay time2 = new TimeOfDay(14, 45);
 assertEquals("Unexpected comparison result", 0, time1.compareTo(time2));
 }

 @Test
 public void testCompareToDifferentHours() {
 TimeOfDay time1 = new TimeOfDay(14, 45);
 TimeOfDay time2 = new TimeOfDay(15, 30);
 assertTrue("Unexpected comparison result", time1.compareTo(time2) < 0);
 }

 @Test
 public void testCompareToDifferentMinutes() {
 TimeOfDay time1 = new TimeOfDay(14, 45);
 TimeOfDay time2 = new TimeOfDay(14, 50);
 assertTrue("Unexpected comparison result", time1.compareTo(time2) < 0);
 }

 @Test
 public void testCompareToEqualHoursAndMinutes() {
 TimeOfDay time1 = new TimeOfDay(14, 45);
 TimeOfDay time2 = new TimeOfDay(14, 45);
 assertEquals("Unexpected comparison result", 0, time1.compareTo(time2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeConstructionWithInts() {
 TimeOfDay time = new TimeOfDay(25, 60); // 25 hours is invalid
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeConstructionWithString() {
 TimeOfDay time = new TimeOfDay("15:70"); // 70 minutes is invalid
 }

 @Test
 public void testEqualsSameObject() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = time1;
 assertTrue("Unexpected equals result", time1.equals(time2));
 }

 @Test
 public void testEqualsDifferentObjects() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 45);
 assertFalse("Unexpected equals result", time1.equals(time2));
 }

 @Test
 public void testHashCodeSameObjects() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 assertEquals("Unexpected hashcode result", time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testHashCodeDifferentObjects() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 45);
 assertNotSame("Unexpected hashcode result", time1.hashCode(), time2.hashCode());
 }

}