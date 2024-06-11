import org.junit.Test;import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testTimeOfDayConstructionWithTimeString() {
 TimeOfDay time = new TimeOfDay("14:45");
 assertEquals("14:45", time.toString());
 }

 @Test
 public void testCompareToReturnsCorrectComparison() {
 TimeOfDay time1 = new TimeOfDay(13, 30);
 TimeOfDay time2 = new TimeOfDay(13, 45);
 TimeOfDay time3 = new TimeOfDay(12, 30);

 assertTrue(time1.compareTo(time3) > 0);
 assertTrue(time1.compareTo(time2) < 0);
 }

 @Test
 public void testGettersReturnCorrectValues() {
 TimeOfDay time = new TimeOfDay(15, 15);
 assertEquals(15, time.getHours().intValue());
 assertEquals(15, time.getMinutes().intValue());
 }

 @Test
 public void testToStringReturnsCorrectStringRepresentation() {
 TimeOfDay time = new TimeOfDay(23, 59);
 assertEquals("23:59", time.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructionWithInvalidTimeString() {
 String invalidTime = "12:60";
 try {
 new TimeOfDay(invalidTime);
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCompareToThrowsIllegalArgumentExceptionWhenComparingDifferentTypes() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 Object nonTimeOfDay = new Object();
 try {
 timeOfDay.compareTo(nonTimeOfDay);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}