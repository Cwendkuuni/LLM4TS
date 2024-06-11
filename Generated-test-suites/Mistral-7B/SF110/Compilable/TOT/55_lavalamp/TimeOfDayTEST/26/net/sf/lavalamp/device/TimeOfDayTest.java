package net.sf.lavalamp.device;
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

 // Test constructor with invalid hours
 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructionInvalidHours() {
 new TimeOfDay("25:00");
 }

 // Test constructor with invalid minutes
 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructionInvalidMinutes() {
 new TimeOfDay("12:60");
 }

 // Test constructor with invalid time string
 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructionInvalidString() {
 new TimeOfDay("12:61:00");
 }

 // Test getters
 @Test
 public void testGetters() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals(12, time.getHours().intValue());
 assertEquals(30, time.getMinutes().intValue());
 }

 // Test compareTo method
 @Test
 public void testCompareTo() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 30);
 TimeOfDay time3 = new TimeOfDay(12, 45);
 TimeOfDay time4 = new TimeOfDay(12, 30);

 assertEquals(1, time1.compareTo(time2));
 assertEquals(0, time1.compareTo(time3));
 assertEquals(-1, time2.compareTo(time1));
 assertEquals(0, time1.compareTo(time4));
 }

 // Test equals method
 @Test
 public void testEquals() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 TimeOfDay time3 = new TimeOfDay(13, 30);

 assertTrue(time1.equals(time1));
 assertTrue(time1.equals(time2));
 assertFalse(time1.equals(time3));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 TimeOfDay time3 = new TimeOfDay(13, 30);

 assertEquals(time1.hashCode(), time2.hashCode());
 assertNotSame(time1.hashCode(), time3.hashCode());
 }
}