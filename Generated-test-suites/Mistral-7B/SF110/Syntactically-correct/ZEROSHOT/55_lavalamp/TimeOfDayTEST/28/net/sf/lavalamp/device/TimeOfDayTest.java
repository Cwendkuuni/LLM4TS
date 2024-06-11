package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(10, 30);
 assertEquals("10:30", time.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidHours() {
 try {
 new TimeOfDay(-1, 30);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidMinutes() {
 try {
 new TimeOfDay(10, 61);
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 TimeOfDay time = new TimeOfDay("14:35");
 assertEquals("14:35", time.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidStringFormat() {
 try {
 new TimeOfDay("14:65");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testTimeOfDayCompareTo() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(11, 15);
 TimeOfDay time3 = new TimeOfDay(10, 30);

 assertEquals(0, time1.compareTo(time3));
 assertEquals(-1, time1.compareTo(time2));
 assertEquals(1, time2.compareTo(time1));
 }

 @Test
 public void testTimeOfDayGetters() {
 TimeOfDay time = new TimeOfDay(15, 45);
 assertEquals(15, time.getHours().intValue());
 assertEquals(45, time.getMinutes().intValue());
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay time = new TimeOfDay(15, 45);
 assertEquals("15:45", time.toString());
 }

 @Test
 public void testTimeOfDayHashCode() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(11, 15);
 TimeOfDay time3 = new TimeOfDay(10, 30);

 assertEquals(time1.hashCode(), time3.hashCode());
 assertNotSame(time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testTimeOfDayEquals() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(10, 30);
 TimeOfDay time3 = new TimeOfDay(11, 15);

 assertTrue(time1.equals(time1));
 assertTrue(time1.equals(time3)); // not the same object, but equal in value
 assertFalse(time1.equals(time2));
 }

}