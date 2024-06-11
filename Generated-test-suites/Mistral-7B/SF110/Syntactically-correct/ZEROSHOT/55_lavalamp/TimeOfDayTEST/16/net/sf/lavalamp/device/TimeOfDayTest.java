package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithHoursAndMinutes() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 45);

 assertEquals(timeOfDay1.getHours(), 11);
 assertEquals(timeOfDay1.getMinutes(), 30);
 assertEquals(timeOfDay2.getHours(), 11);
 assertEquals(timeOfDay2.getMinutes(), 45);
 }

 @Test
 public void testTimeOfDayConstructionWithTimeString() {
 TimeOfDay timeOfDay1 = new TimeOfDay("11:30");
 TimeOfDay timeOfDay2 = new TimeOfDay("11:45");

 assertEquals(timeOfDay1.getHours(), 11);
 assertEquals(timeOfDay1.getMinutes(), 30);
 assertEquals(timeOfDay2.getHours(), 11);
 assertEquals(timeOfDay2.getMinutes(), 45);
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructionWithInvalidTimeString() {
 new TimeOfDay("11:60");
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 45);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 0);

 assertEquals(timeOfDay1.compareTo(timeOfDay2), -15);
 assertEquals(timeOfDay2.compareTo(timeOfDay1), 15);
 assertEquals(timeOfDay3.compareTo(timeOfDay1), 60);
 }

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertEquals(timeOfDay.toString(), "11:30");
 }

 @Test
 public void testGetters() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);

 assertEquals(timeOfDay.getHours(), 11);
 assertEquals(timeOfDay.getMinutes(), 30);
 }

 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 0);

 assertEquals(timeOfDay1, timeOfDay1);
 assertEquals(timeOfDay1, timeOfDay2);
 assertNotSame(timeOfDay1, timeOfDay3);
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 0);

 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }

 @Test
 public void testNullObject() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertNull(timeOfDay.equals(null));
 }

 @Test
 public void testDifferentClassObject() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertNotSame(timeOfDay, new String("11:30"));
 }
}