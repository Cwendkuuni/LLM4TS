import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 // Test constructor with valid hours and minutes
 @Test
 public void testTimeOfDayConstructionValid() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 // Test constructor with valid time string
 @Test
 public void testTimeOfDayConstructionValidString() {
 TimeOfDay time = new TimeOfDay("14:25");
 assertEquals("14:25", time.toString());
 }

 // Test compareTo method with different hours
 @Test
 public void testCompareToDifferentHours() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(14, 30);
 assertEquals(1, time1.compareTo(time2));
 }

 // Test compareTo method with same hours and different minutes
 @Test
 public void testCompareToSameHoursDifferentMinutes() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 45);
 assertEquals(-1, time1.compareTo(time2));
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
 TimeOfDay time = new TimeOfDay(14, 15);
 assertEquals(14, time.getHours().intValue());
 }

 // Test getMinutes method
 @Test
 public void testGetMinutes() {
 TimeOfDay time = new TimeOfDay(14, 15);
 assertEquals(15, time.getMinutes().intValue());
 }

 // Test toString method
 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(14, 15);
 assertEquals("14:15", time.toString());
 }

 // Test equals method with same object
 @Test
 public void testEqualsSameObject() {
 TimeOfDay time1 = new TimeOfDay(14, 15);
 TimeOfDay time2 = time1;
 assertTrue(time1.equals(time2));
 }

 // Test equals method with different objects but same values
 @Test
 public void testEqualsDifferentObjectsSameValues() {
 TimeOfDay time1 = new TimeOfDay(14, 15);
 TimeOfDay time2 = new TimeOfDay(14, 15);
 assertTrue(time1.equals(time2));
 }

 // Test equals method with different objects and different values
 @Test
 public void testEqualsDifferentObjectsDifferentValues() {
 TimeOfDay time1 = new TimeOfDay(14, 15);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertFalse(time1.equals(time2));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(14, 15);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 assertNotEquals(time1.hashCode(), time2.hashCode());
 }
}