package net.sf.lavalamp.device;
import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.lavalamp.device.TimeOfDay;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructorWithInts() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 TimeOfDay time = new TimeOfDay("14:25");
 assertEquals("14:25", time.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayInvalidStringFormat() {
 try {
 new TimeOfDay("12:60");
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
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
 assertEquals(1, time1.compareTo(time2));
 }

 @Test
 public void testCompareToDifferentMinutes() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 35);
 assertEquals(1, time1.compareTo(time2));
 }

 @Test
 public void testCompareToSameHoursDifferentMinutes() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 29);
 assertEquals(-1, time2.compareTo(time1));
 }

 @Test
 public void testGetters() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals(12, time.getHours().intValue());
 assertEquals(30, time.getMinutes().intValue());
 }

}