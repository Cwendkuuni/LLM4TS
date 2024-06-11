import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testConstructorWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testConstructorWithTimeString() {
 TimeOfDay time = new TimeOfDay("14:25");
 assertEquals("14:25", time.toString());
 }

 @Test
 public void testCompareToSameTime() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 assertEquals(0, time1.compareTo(time2));
 }

 @Test
 public void testCompareToDifferentHours() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertTrue(time1.compareTo(time2) < 0);
 }

 @Test
 public void testCompareToDifferentMinutes() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 35);
 assertTrue(time1.compareTo(time2) < 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidHours() {
 new TimeOfDay(-1, 30);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMinutes() {
 new TimeOfDay(1, 60);
 }

 @Test
 public void testGetHours() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals(12, time.getHours());
 }

 @Test
 public void testGetMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals(30, time.getMinutes());
 }

 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 assertEquals(time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testEqualsSameObject() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = time1;
 assertTrue(time1.equals(time2));
 }

 @Test
 public void testEqualsDifferentObjectsSameTime() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 assertTrue(time1.equals(time2));
 }

 @Test
 public void testEqualsDifferentObjectsDifferentTime() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertFalse(time1.equals(time2));
 }

 @Test
 public void testEqualsNull() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertFalse(time.equals(null));
 }

 @Test
 public void testEqualsDifferentClasses() {
 TimeOfDay time = new TimeOfDay(12, 30);
 String string = "12:30";
 assertFalse(time.equals(string));
 }
}