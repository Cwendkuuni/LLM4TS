package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 TimeOfDay timeOfDay1;
 TimeOfDay timeOfDay2;
 TimeOfDay timeOfDay3;
 TimeOfDay timeOfDay4;
 TimeOfDay timeOfDay5;
 TimeOfDay timeOfDay6;

 @Test
 public void testTimeOfDayConstructionWithHoursMinutes() {
 timeOfDay1 = new TimeOfDay(11, 30);
 timeOfDay2 = new TimeOfDay(12, 45);
 timeOfDay3 = new TimeOfDay(23, 59);
 timeOfDay4 = new TimeOfDay(0, 0);

 assertEquals("11:30", timeOfDay1.toString());
 assertEquals("12:45", timeOfDay2.toString());
 assertEquals("23:59", timeOfDay3.toString());
 assertEquals("00:00", timeOfDay4.toString());
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 timeOfDay5 = new TimeOfDay("14:22");
 timeOfDay6 = new TimeOfDay("01:30");

 assertEquals("14:22", timeOfDay5.toString());
 assertEquals("01:30", timeOfDay6.toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayStringConstructionWithInvalidFormat() {
 String invalidTime = "14:60";

 try {
 timeOfDay1 = new TimeOfDay(invalidTime);
 } catch (NumberFormatException e) {
 fail("Expected NumberFormatException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testTimeOfDayCompareTo() {
 assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(-1, timeOfDay2.compareTo(timeOfDay1));
 assertEquals(0, timeOfDay1.compareTo(timeOfDay1));
 assertEquals(1, timeOfDay3.compareTo(timeOfDay1));
 assertEquals(1, timeOfDay4.compareTo(timeOfDay1));
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay3));
 }

 @Test
 public void testTimeOfDayGetters() {
 timeOfDay1 = new TimeOfDay(11, 30);
 timeOfDay2 = new TimeOfDay(12, 45);

 assertEquals(11, timeOfDay1.getHours().intValue());
 assertEquals(30, timeOfDay1.getMinutes().intValue());
 assertEquals(12, timeOfDay2.getHours().intValue());
 assertEquals(45, timeOfDay2.getMinutes().intValue());
 }

 @Test
 public void testTimeOfDayToString() {
 timeOfDay1 = new TimeOfDay(11, 30);
 timeOfDay2 = new TimeOfDay(12, 45);
 timeOfDay3 = new TimeOfDay(23, 59);
 timeOfDay4 = new TimeOfDay(0, 0);
 timeOfDay5 = new TimeOfDay("14:22");
 timeOfDay6 = new TimeOfDay("01:30");

 assertEquals("11:30", timeOfDay1.toString());
 assertEquals("12:45", timeOfDay2.toString());
 assertEquals("23:59", timeOfDay3.toString());
 assertEquals("00:00", timeOfDay4.toString());
 assertEquals("14:22", timeOfDay5.toString());
 assertEquals("01:30", timeOfDay6.toString());
 }

 @Test
 public void testTimeOfDayHashCode() {
 timeOfDay1 = new TimeOfDay(11, 30);
 timeOfDay2 = new TimeOfDay(12, 45);
 timeOfDay3 = new TimeOfDay(11, 30);

 assertEquals(221330113, timeOfDay1.hashCode());
 assertEquals(221330124, timeOfDay2.hashCode());
 assertEquals(221330113, timeOfDay3.hashCode());
 }

 @Test
 public void testTimeOfDayEquals() {
 timeOfDay1 = new TimeOfDay(11, 30);
 timeOfDay2 = new TimeOfDay(11, 30);
 timeOfDay3 = new TimeOfDay(12, 45);
 timeOfDay4 = new TimeOfDay(11, 30);
 timeOfDay5 = new TimeOfDay("11:30");
 timeOfDay6 = new TimeOfDay("11:30");

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay4));
 assertTrue(timeOfDay1.equals(timeOfDay5));
 assertFalse(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }
}