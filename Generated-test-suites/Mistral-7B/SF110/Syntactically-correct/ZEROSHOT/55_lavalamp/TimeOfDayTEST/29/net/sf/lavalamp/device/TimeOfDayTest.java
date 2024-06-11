package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(10, 30);
 assertEquals(10, time.getHours());
 assertEquals(30, time.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 TimeOfDay time = new TimeOfDay("14:45");
 assertEquals(14, time.getHours());
 assertEquals(45, time.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayStringInvalidFormat() {
 try {
 new TimeOfDay("14:60");
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTimeOfDayCompareTo() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(9, 30);
 TimeOfDay time3 = new TimeOfDay(10, 35);

 assertTrue(time1.compareTo(time2) > 0);
 assertTrue(time1.compareTo(time3) <= 0);
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay time = new TimeOfDay(14, 45);
 assertEquals("14:45", time.toString());
 }

 @Test
 public void testTimeOfDayHashCode() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(10, 30);
 TimeOfDay time3 = new TimeOfDay(11, 30);

 assertEquals(time1.hashCode(), time1.hashCode());
 assertEquals(time1.hashCode(), time2.hashCode());
 assertNotEquals(time1.hashCode(), time3.hashCode());
 }

 @Test
 public void testTimeOfDayEquals() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(10, 30);
 TimeOfDay time3 = new TimeOfDay(11, 30);

 assertTrue(time1.equals(time1));
 assertTrue(time1.equals(time2));
 assertFalse(time1.equals(time3));
 }

 @Test
 public void testTimeOfDayCompareToSelf() {
 TimeOfDay time = new TimeOfDay(10, 30);
 assertEquals(0, time.compareTo(time));
 }
}