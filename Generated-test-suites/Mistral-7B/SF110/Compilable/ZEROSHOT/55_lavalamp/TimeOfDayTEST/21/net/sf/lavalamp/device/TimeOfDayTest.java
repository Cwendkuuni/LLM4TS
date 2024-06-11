package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertEquals("11:30", timeOfDay.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidHours() {
 try {
 new TimeOfDay(-1, 0);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid hour value: -1", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidMinutes() {
 try {
 new TimeOfDay(1, 60);
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid minute value: 60", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidTimeFormat() {
 try {
 new TimeOfDay("123");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid time format: 123", e.getMessage());
 }
 }

 @Test
 public void testTimeOfDayComparison() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 0);
 TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay4 = new TimeOfDay(11, 29);

 assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(1, timeOfDay2.compareTo(timeOfDay4));
 }

 @Test
 public void testTimeOfDayGetters() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertEquals(11, timeOfDay.getHours().intValue());
 assertEquals(30, timeOfDay.getMinutes().intValue());
 }

 @Test
 public void testTimeOfDayToString() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertEquals("11:30", timeOfDay.toString());
 }

 @Test
 public void testTimeOfDayHashcode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(1, 1);
 TimeOfDay timeOfDay2 = new TimeOfDay(1, 1);
 TimeOfDay timeOfDay3 = new TimeOfDay(2, 2);
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 assertNotEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }

 @Test
 public void testTimeOfDayEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(1, 1);
 TimeOfDay timeOfDay2 = new TimeOfDay(1, 1);
 TimeOfDay timeOfDay3 = new TimeOfDay(2, 2);

 assertEquals(timeOfDay1, timeOfDay1);
 assertEquals(timeOfDay1, timeOfDay2);
 assertNotEquals(timeOfDay1, timeOfDay3);
 }
}