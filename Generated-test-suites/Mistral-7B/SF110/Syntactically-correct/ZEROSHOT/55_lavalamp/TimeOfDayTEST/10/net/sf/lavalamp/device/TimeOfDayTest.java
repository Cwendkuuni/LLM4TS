package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(1, 30);
 assertEquals(1, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:45");
 assertEquals(14, timeOfDay.getHours());
 assertEquals(45, timeOfDay.getMinutes());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidStringFormat1() {
 try {
 new TimeOfDay("14:60");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid time format: 14:60", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidStringFormat2() {
 try {
 new TimeOfDay("14:59:60");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid time format: 14:59:60", e.getMessage());
 }
 }

 @Test
 public void testTimeOfDayComparison() {
 TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(1, 45);
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));

 TimeOfDay timeOfDay3 = new TimeOfDay(2, 30);
 assertEquals(1, timeOfDay2.compareTo(timeOfDay3));
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay timeOfDay = new TimeOfDay(1, 30);
 assertEquals("1:30", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());

 TimeOfDay timeOfDay3 = new TimeOfDay(2, 30);
 assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }

 @Test
 public void testTimeOfDayEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
 assertEquals(timeOfDay1, timeOfDay2);
 }

 @Test
 public void testTimeOfDayEqualsDifferentObjects() {
 TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
 Object otherObject = new Object();
 assertNotSame(timeOfDay1, otherObject);
 }

 @Test
 public void testTimeOfDayEqualsNull() {
 TimeOfDay timeOfDay = new TimeOfDay(1, 30);
 assertNull(timeOfDay.equals(null));
 }

}