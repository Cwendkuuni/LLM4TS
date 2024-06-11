package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(10, 30);
 assertEquals("10:30", time.toString());
 }

 @Test
 public void testTimeOfDayConstructionFromString() {
 TimeOfDay time = new TimeOfDay("14:59");
 assertEquals("14:59", time.toString());
 }

 @Test
 public void testCompareToMethod() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(11, 15);
 TimeOfDay time3 = new TimeOfDay(10, 30);
 TimeOfDay time4 = new TimeOfDay(9, 59);
 TimeOfDay time5 = new TimeOfDay(23, 59);

 assertEquals(1, time1.compareTo(time2));
 assertEquals(0, time1.compareTo(time3));
 assertEquals(-1, time2.compareTo(time1));
 assertEquals(-1, time4.compareTo(time1));
 assertEquals(1, time5.compareTo(time1));
 }

 @Test
 public void testGettersMethod() {
 TimeOfDay time = new TimeOfDay(10, 30);
 assertEquals(10, time.getHours().intValue());
 assertEquals(30, time.getMinutes().intValue());
 }

 @Test(expected = NullPointerException.class)
 public void testToStringMethodWithNullHours() {
 new TimeOfDay(null, 30);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringMethodWithNullMinutes() {
 new TimeOfDay(10, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringMethodWithInvalidTimeString() {
 try {
 new TimeOfDay("15:60");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid time: 60 minutes in hour 15", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringMethodWithInvalidTimeStringFormat() {
 try {
 new TimeOfDay("15:30:45");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid time format: too many colon(s)", e.getMessage());
 }
 }
}