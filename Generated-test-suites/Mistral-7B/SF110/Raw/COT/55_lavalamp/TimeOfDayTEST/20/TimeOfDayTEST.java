package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime1;
 private TimeOfDay invalidTime2;

 @Test
 public void testTimeOfDayConstructorWithInts() {
 validTime1 = new TimeOfDay(12, 30);
 assertEquals("12:30", validTime1.toString());
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 validTime2 = new TimeOfDay("14:22");
 assertEquals("14:22", validTime2.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidStringHours() {
 invalidTime1 = new TimeOfDay("1A:22");
 fail("Expected NumberFormatException but got: " + invalidTime1);
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidStringMinutes() {
 invalidTime2 = new TimeOfDay("12:60");
 fail("Expected NumberFormatException but got: " + invalidTime2);
 }

 @Test
 public void testCompareTo() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 0);
 TimeOfDay time3 = new TimeOfDay(12, 30);

 assertEquals(0, time1.compareTo(time3));
 assertEquals(-1, time1.compareTo(time2));
 assertEquals(1, time2.compareTo(time1));
 }

 @Test
 public void testGetHours() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals(12, time.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals(30, time.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 0);
 TimeOfDay time3 = new TimeOfDay(12, 30);

 assertEquals(time1.hashCode(), time3.hashCode());
 assertNotEquals(time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(12, 30);
 TimeOfDay time3 = new TimeOfDay(12, 31);

 assertTrue(time1.equals(time1));
 assertTrue(time1.equals(time3)); // should not happen in real code
 assertFalse(time1.equals(time2));
 }

}