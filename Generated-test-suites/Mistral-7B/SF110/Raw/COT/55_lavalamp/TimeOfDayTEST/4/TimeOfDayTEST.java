package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime;

 @Test
 public void testTimeOfDayConstructorWithInt() {
 validTime1 = new TimeOfDay(12, 30);
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 validTime2 = new TimeOfDay("14:45");
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidString() {
 String invalidTimeString = "15:60";
 invalidTime = new TimeOfDay(invalidTimeString);
 }

 @Test
 public void testGetHours() {
 assertEquals(12, validTime1.getHours().intValue());
 assertEquals(14, validTime2.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 assertEquals(30, validTime1.getMinutes().intValue());
 assertEquals(45, validTime2.getMinutes().intValue());
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 45);

 assertEquals(1, validTime1.compareTo(validTime2));
 assertEquals(-1, validTime2.compareTo(validTime1));
 assertEquals(0, validTime1.compareTo(validTime1));
 assertEquals(1, timeOfDay1.compareTo(timeOfDay3));
 assertEquals(-1, timeOfDay3.compareTo(timeOfDay1));
 }

 @Test
 public void testToString() {
 assertEquals("12:30", validTime1.toString());
 assertEquals("14:45", validTime2.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);

 long hashCode1 = timeOfDay1.hashCode();
 long hashCode2 = timeOfDay2.hashCode();

 assertEquals(100 * 13 + 30, hashCode1);
 assertEquals(100 * 13 + 45, hashCode2);
 }

 @Test
 public void testEqualsObject() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(13, 45);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }

 @Test(expected = NullPointerException.class)
 public void testEqualsObjectNull() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 30);
 assertFalse(timeOfDay.equals(null));
 }

 @Test(expected = ClassCastException.class)
 public void testEqualsObjectWrongClass() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 30);
 assertFalse(timeOfDay.equals(new Object()));
 }

}