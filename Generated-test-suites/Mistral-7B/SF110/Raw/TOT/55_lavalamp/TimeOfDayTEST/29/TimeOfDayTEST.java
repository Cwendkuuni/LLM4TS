import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeConstructionWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testTimeConstructionWithTimeString() {
 TimeOfDay time = new TimeOfDay("14:25");
 assertEquals("14:25", time.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeConstructionWithInvalidTimeStringHours() {
 try {
 new TimeOfDay("25:30");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeConstructionWithInvalidTimeStringMinutes() {
 try {
 new TimeOfDay("12:60");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Pass
 }
 }

 @Test
 public void testCompareTo() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 0);
 int comparison = time1.compareTo(time2);
 assertTrue(comparison < 0);

 time1 = new TimeOfDay(13, 30);
 comparison = time1.compareTo(time2);
 assertTrue(comparison > 0);

 time1 = new TimeOfDay(13, 30);
 time2 = new TimeOfDay(13, 30);
 comparison = time1.compareTo(time2);
 assertEquals(0, comparison);
 }

 @Test
 public void testGetters() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals(12, time.getHours().intValue());
 assertEquals(30, time.getMinutes().intValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetters() {
 TimeOfDay time = new TimeOfDay(12, 30);
 try {
 time.setHours(13);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Pass
 }

 time.setMinutes(45);
 try {
 time.setMinutes(44);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Pass
 }
 }
}