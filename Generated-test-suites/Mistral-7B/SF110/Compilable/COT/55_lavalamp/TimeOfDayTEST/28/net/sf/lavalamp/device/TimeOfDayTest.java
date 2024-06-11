package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime1;
 private TimeOfDay invalidTime2;

 @Test
 public void testTimeOfDayConstructorWithValidArguments() {
 validTime1 = new TimeOfDay(12, 30);
 }

 @Test
 public void testTimeOfDayConstructorWithInvalidArguments() {
 try {
 invalidTime1 = new TimeOfDay(25, 60); // hours > 23
 } catch (NumberFormatException e) {
 // expected: hours is a number between 0 and 23
 invalidTime1 = null;
 }

 try {
 invalidTime2 = new TimeOfDay("24:61"); // minutes > 59
 } catch (NumberFormatException e) {
 // expected: minutes is a number between 0 and 59
 invalidTime2 = null;
 }
 }

 @Test
 public void testGettersMethodForHours() {
 assertNotNull(validTime1);
 assertEquals(12, validTime1.getHours().intValue());
 }

 @Test
 public void testGettersMethodForMinutes() {
 assertNotNull(validTime1);
 assertEquals(30, validTime1.getMinutes().intValue());
 }

 @Test
 public void testCompareToMethod() {
 TimeOfDay time3 = new TimeOfDay(13, 0);
 TimeOfDay time4 = new TimeOfDay(13, 30);

 assertEquals(1, validTime1.compareTo(validTime2)); // different hours
 assertEquals(0, validTime1.compareTo(validTime1)); // same object
 assertEquals(-1, validTime1.compareTo(time3)); // less than
 assertEquals(0, validTime1.compareTo(time4)); // same minute
 }

 @Test(expected = NullPointerException.class)
 public void testToStringMethodForNullObject() {
 TimeOfDay nullTime = null;
 nullTime.toString();
 }

 @Test
 public void testToStringMethod() {
 assertEquals("12:30", validTime1.toString());
 }

 @Test
 public void testHashCodeMethod() {
 TimeOfDay time5 = new TimeOfDay(12, 45);
 assertEquals(100 * 12 + 45, validTime1.hashCode());
 assertEquals(validTime1.hashCode(), time5.hashCode()); // same time
 }

 @Test
 public void testEqualsMethodForSameObject() {
 assertTrue(validTime1.equals(validTime1));
 }

 @Test
 public void testEqualsMethodForSameTime() {
 TimeOfDay time6 = new TimeOfDay(12, 30);
 assertTrue(validTime1.equals(time6));
 }

 @Test
 public void testEqualsMethodForNullObject() {
 assertFalse(validTime1.equals(null));
 }

 @Test
 public void testEqualsMethodForDifferentClass() {
 assertFalse(validTime1.equals(new Object()));
 }

 @Test
 public void testEqualsMethodForDifferentTime() {
 assertFalse(validTime1.equals(invalidTime1));
 }

 @Test
 public void testEqualsMethodForDifferentTimeWithSameMinsButDifferentHours() {
 TimeOfDay time7 = new TimeOfDay(14, 30);
 assertFalse(validTime1.equals(time7));
 }

 @Test
 public void testEqualsMethodForDifferentTimeWithSameHoursButDifferentMins() {
 TimeOfDay time8 = new TimeOfDay(12, 45);
 assertFalse(validTime1.equals(time8));
 }

 @Test(expected = NullPointerException.class)
 public void testCompareToMethodForNullObject() {
 TimeOfDay nullTime2 = null;
 validTime1.compareTo(nullTime2);
 }

 @Test(expected = ClassCastException.class)
 public void testEqualsMethodForDifferentClassType() {
 TimeOfDay validTime3 = new TimeOfDay(12, 30);
 assertFalse(validTime1.equals("Invalid Time"));
 }
}