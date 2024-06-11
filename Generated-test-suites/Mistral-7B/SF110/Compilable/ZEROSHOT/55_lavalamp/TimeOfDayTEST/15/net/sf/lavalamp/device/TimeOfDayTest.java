package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("TimeOfDay should have hours 12 and minutes 30", new TimeOfDay(12, 30), time);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidHours() {
 try {
 new TimeOfDay(-1, 0);
 fail("TimeOfDay should not accept negative hours.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidMinutes() {
 try {
 new TimeOfDay(0, 60);
 fail("TimeOfDay should not accept more than 59 minutes.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 TimeOfDay time = new TimeOfDay("14:35");
 assertEquals("TimeOfDay should have hours 14 and minutes 35", new TimeOfDay(14, 35), time);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidStringFormat() {
 try {
 new TimeOfDay("15:61");
 fail("TimeOfDay should not accept more than 59 minutes.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetters() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("getHours should return hours 12", 12, time.getHours().intValue());
 assertEquals("getMinutes should return minutes 30", 30, time.getMinutes().intValue());
 }

 @Test
 public void testCompareTo() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 0);
 TimeOfDay time3 = new TimeOfDay(12, 30);
 TimeOfDay time4 = new TimeOfDay(12, 45);

 assertEquals("Compare to should return -1 for time1 < time2", -1, time1.compareTo(time2));
 assertEquals("Compare to should return 0 for time1 == time3", 0, time1.compareTo(time3));
 assertEquals("Compare to should return 1 for time1 > time4", 1, time1.compareTo(time4));
 }

 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("toString should return \"12:30\"", "12:30", time.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 0);

 assertEquals("HashCode should be different for different TimeOfDay objects", time1.hashCode(), time2.hashCode());

 TimeOfDay time3 = new TimeOfDay(12, 30);
 assertEquals("HashCode should be the same for equal TimeOfDay objects", time1.hashCode(), time3.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay time1 = new TimeOfDay(12, 30);
 TimeOfDay time2 = new TimeOfDay(13, 0);
 TimeOfDay time3 = new TimeOfDay(12, 30);

 assertFalse("Equals should return false for different TimeOfDay objects", time1.equals(time2));
 assertTrue("Equals should return true for equal TimeOfDay objects", time1.equals(time3));
 }
}