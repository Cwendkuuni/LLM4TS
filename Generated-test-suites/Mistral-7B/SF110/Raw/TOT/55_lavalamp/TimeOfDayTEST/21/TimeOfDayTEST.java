import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 // Test constructor with valid hours and minutes
 @Test
 public void testValidTimeConstruction() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 // Test constructor with valid time string
 @Test
 public void testValidTimeStringConstruction() {
 TimeOfDay time = new TimeOfDay("14:25");
 assertEquals("14:25", time.toString());
 }

 // Test compareTo method with different hours
 @Test
 public void testCompareToDifferentHours() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(14, 0);
 assertTrue(time1.compareTo(time2) < 0);
 }

 // Test compareTo method with same hours and different minutes
 @Test
 public void testCompareToSameHoursDifferentMinutes() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 45);
 assertTrue(time1.compareTo(time2) < 0);
 }

 // Test compareTo method with same hours and minutes
 @Test
 public void testCompareToSameHoursSameMinutes() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertEquals(0, time1.compareTo(time2));
 }

 // Test getHours method
 @Test
 public void testGetHours() {
 TimeOfDay time = new TimeOfDay(13, 30);
 assertEquals(13, time.getHours().intValue());
 }

 // Test getMinutes method
 @Test
 public void testGetMinutes() {
 TimeOfDay time = new TimeOfDay(13, 30);
 assertEquals(30, time.getMinutes().intValue());
 }

 // Test equals method with same object
 @Test
 public void testEqualsSameObject() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = time1;
 assertTrue(time1.equals(time2));
 }

 // Test equals method with different objects
 @Test
 public void testEqualsDifferentObjects() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(14, 0);
 assertFalse(time1.equals(time2));
 }

 // Test equals method with null object
 @Test(expected = NullPointerException.class)
 public void testEqualsNullObject() {
 TimeOfDay time = new TimeOfDay(13, 30);
 time.equals(null);
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(14, 0);
 assertNotSame(time1.hashCode(), time2.hashCode());
 }
}