import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeConstruction() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeConstruction() {
 String invalidTime = "12ab";
 try {
 new TimeOfDay(invalidTime);
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test
 public void testTimeComparison() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 45);
 assertNotEquals(time1, time2);
 }

 @Test
 public void testTimeComparisonSameHours() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 35);
 assertNotEquals(time1, time2);
 }

 @Test
 public void testTimeComparisonSameTime() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertEquals(time1, time2);
 }

 @Test
 public void testTimeComparisonBefore() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertNotEquals(time1, time2);
 assertLessThan(time1, time2);
 }

 @Test
 public void testTimeComparisonAfter() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 assertNotEquals(time1, time2);
 assertGreaterThan(time1, time2);
 }

 @Test
 public void testTimeComparisonSameMinute() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertEquals(time1, time2);
 }

 @Test
 public void testTimeGetters() {
 TimeOfDay time = new TimeOfDay(13, 30);
 assertEquals(13, time.getHours().intValue());
 assertEquals(30, time.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(13, 30);
 assertEquals("13:30", time.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertNotEquals(time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertEquals(time1, time2);
 }

 @Test
 public void testEqualsNull() {
 TimeOfDay time = new TimeOfDay(13, 30);
 assertFalse(time.equals(null));
 }

 @Test
 public void testEqualsDifferentClass() {
 TimeOfDay time = new TimeOfDay(13, 30);
 assertFalse(time.equals("test"));
 }

 @Test
 public void testEqualsDifferentTime() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(14, 30);
 assertFalse(time1.equals(time2));
 }

 private boolean assertLessThan(TimeOfDay a, TimeOfDay b) {
 return a.compareTo(b) < 0;
 }

 private boolean assertGreaterThan(TimeOfDay a, TimeOfDay b) {
 return a.compareTo(b) > 0;
}

}