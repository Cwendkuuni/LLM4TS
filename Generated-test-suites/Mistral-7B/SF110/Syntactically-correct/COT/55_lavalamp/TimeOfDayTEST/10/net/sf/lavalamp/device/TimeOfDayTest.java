package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime1;
 private TimeOfDay invalidTime2;

 @Test
 public void testTimeOfDayConstructorWithIntHoursAndIntMinutes() {
 validTime1 = new TimeOfDay(12, 34);
 assertNotNull(validTime1);
 }

 @Test
 public void testTimeOfDayConstructorWithStringTime() {
 validTime2 = new TimeOfDay("14:56");
 assertNotNull(validTime2);
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidStringTimeHours() {
 String invalidTimeString = "13a";
 invalidTime1 = new TimeOfDay(invalidTimeString);
 assertThrows(NumberFormatException.class, () -> new TimeOfDay(invalidTimeString));
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidStringTimeMinutes() {
 String invalidTimeString = "14:60";
 invalidTime2 = new TimeOfDay(invalidTimeString);
 assertThrows(NumberFormatException.class, () -> new TimeOfDay(invalidTimeString));
 }

 @Test
 public void testGetHours() {
 validTime1 = new TimeOfDay(12, 34);
 assertEquals(12, validTime1.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 validTime1 = new TimeOfDay(12, 34);
 assertEquals(34, validTime1.getMinutes().intValue());
 }

 @Test
 public void testCompareTo() {
 validTime1 = new TimeOfDay(12, 34);
 validTime2 = new TimeOfDay(13, 30);
 assertEquals(1, validTime1.compareTo(validTime2));
 }

 @Test
 public void testCompareToSameInstance() {
 validTime1 = new TimeOfDay(12, 34);
 assertEquals(0, validTime1.compareTo(validTime1));
 }

 @Test
 public void testCompareToEqualHoursAndMinutes() {
 validTime1 = new TimeOfDay(12, 34);
 validTime2 = new TimeOfDay(12, 34);
 assertEquals(0, validTime1.compareTo(validTime2));
 }

 @Test
 public void testCompareToDifferentHours() {
 validTime1 = new TimeOfDay(12, 34);
 validTime2 = new TimeOfDay(13, 35);
 assertEquals(-1, validTime1.compareTo(validTime2));
 }

 @Test
 public void testCompareToDifferentMinutesSameHours() {
 validTime1 = new TimeOfDay(12, 34);
 validTime2 = new TimeOfDay(12, 35);
 assertEquals(-1, validTime1.compareTo(validTime2));
 }

 @Test
 public void testCompareToDifferentMinutesDifferentHours() {
 validTime1 = new TimeOfDay(13, 34);
 validTime2 = new TimeOfDay(12, 35);
 assertEquals(1, validTime2.compareTo(validTime1));
 }

 @Test
 public void testToString() {
 validTime1 = new TimeOfDay(12, 34);
 assertEquals("12:34", validTime1.toString());
 }

 @Test
 public void testHashCode() {
 validTime1 = new TimeOfDay(12, 34);
 validTime2 = new TimeOfDay(13, 35);
 assertNotEquals(validTime1.hashCode(), validTime2.hashCode());
 }

 @Test
 public void testEquals() {
 validTime1 = new TimeOfDay(12, 34);
 validTime2 = new TimeOfDay(12, 34);
 assertTrue(validTime1.equals(validTime2));
 }

 @Test
 public void testEqualsDifferentInstances() {
 validTime1 = new TimeOfDay(12, 34);
 validTime2 = new TimeOfDay(13, 35);
 assertFalse(validTime1.equals(validTime2));
 }

 @Test
 public void testEqualsNull() {
 validTime1 = new TimeOfDay(12, 34);
 assertFalse(validTime1.equals(null));
 }

 @Test
 public void testEqualsSameClassDifferentValues() {
 validTime1 = new TimeOfDay(12, 34);
 String anotherObject = new String("12:34");
 assertFalse(validTime1.equals(anotherObject));
 }

}