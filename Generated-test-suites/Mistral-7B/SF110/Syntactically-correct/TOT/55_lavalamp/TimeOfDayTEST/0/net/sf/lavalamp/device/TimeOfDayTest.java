import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructorWithInts() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 assertEquals("12:30", time1.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 TimeOfDay time2 = new TimeOfDay("14:25");
 assertEquals("14:25", time2.toString());
 }

 @Test
 public void testTimeOfDayCompareTo() {
 TimeOfDay time1 = new TimeOfDay(13, 45);
 TimeOfDay time2 = new TimeOfDay(13, 55);
 TimeOfDay time3 = new TimeOfDay(12, 30);

 assertTrue(time1.compareTo(time2) < 0);
 assertTrue(time1.compareTo(time3) > 0);
 assertTrue(time2.compareTo(time3) > 0);
 }

 @Test
 public void testTimeOfDayGetters() {
 TimeOfDay time = new TimeOfDay(13, 59);
 assertEquals(13, time.getHours().intValue());
 assertEquals(59, time.getMinutes().intValue());
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay time = new TimeOfDay(15, 10);
 assertEquals("15:10", time.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeStringFormat() {
 String invalidTime = "12:60";
 try {
 new TimeOfDay(invalidTime);
 fail("Expected IllegalArgumentException");
 } catch(IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeHours() {
 try {
 new TimeOfDay(-2, 30);
 fail("Expected IllegalArgumentException");
 } catch(IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeMinutes() {
 try {
 new TimeOfDay(13, -30);
 fail("Expected IllegalArgumentException");
 } catch(IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullObject() {
 try {
 new TimeOfDay(null, null);
 fail("Expected NullPointerException");
 } catch(NullPointerException e) {
 // pass
 }
 }
}