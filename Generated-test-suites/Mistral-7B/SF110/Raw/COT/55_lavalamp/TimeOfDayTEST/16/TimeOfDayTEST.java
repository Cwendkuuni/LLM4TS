package net.sf.lavalamp.device;

import org.junit.Assert;
import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime1;
 private TimeOfDay invalidTime2;

 @Test
 public void testConstructorWithHoursAndMinutes() {
 validTime1 = new TimeOfDay(10, 30);
 }

 @Test
 public void testToStringMethod() {
 validTime1 = new TimeOfDay(10, 30);
 Assert.assertEquals("10:30", validTime1.toString());
 }

 @Test
 public void testCompareToMethod() {
 validTime1 = new TimeOfDay(10, 30);
 validTime2 = new TimeOfDay(11, 15);
 Assert.assertEquals(-1, validTime1.compareTo(validTime2));

 validTime1 = new TimeOfDay(11, 15);
 validTime2 = new TimeOfDay(10, 30);
 Assert.assertEquals(1, validTime1.compareTo(validTime2));

 validTime1 = new TimeOfDay(10, 30);
 validTime2 = new TimeOfDay(10, 30);
 Assert.assertEquals(0, validTime1.compareTo(validTime2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidHoursValueInConstructor() {
 invalidTime1 = new TimeOfDay(25, 30);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMinutesValueInConstructor() {
 invalidTime1 = new TimeOfDay(10, 60);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeFormatInConstructor() {
 invalidTime1 = new TimeOfDay("11:60");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeFormatInToStringMethod() {
 validTime1 = new TimeOfDay(10, 30);
 Assert.assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("11:65"));
 }

 @Test
 public void testGetHoursMethod() {
 validTime1 = new TimeOfDay(10, 30);
 Assert.assertEquals(new Integer(10), validTime1.getHours());
 }

 @Test
 public void testGetMinutesMethod() {
 validTime1 = new TimeOfDay(10, 30);
 Assert.assertEquals(new Integer(30), validTime1.getMinutes());
 }

 @Test
 public void testEqualsMethod() {
 validTime1 = new TimeOfDay(10, 30);
 validTime2 = new TimeOfDay(10, 30);
 Assert.assertTrue(validTime1.equals(validTime2));

 validTime1 = new TimeOfDay(10, 30);
 validTime2 = new TimeOfDay(11, 15);
 Assert.assertFalse(validTime1.equals(validTime2));

 validTime1 = null;
 validTime2 = new TimeOfDay(10, 30);
 Assert.assertFalse(validTime1.equals(validTime2));

 validTime1 = new TimeOfDay(10, 30);
 validTime2 = null;
 Assert.assertFalse(validTime1.equals(validTime2));
 }

 @Test
 public void testHashCodeMethod() {
 validTime1 = new TimeOfDay(10, 30);
 validTime2 = new TimeOfDay(11, 15);
 Assert.assertNotSame(validTime1.hashCode(), validTime2.hashCode());

 validTime1 = new TimeOfDay(10, 30);
 validTime2 = new TimeOfDay(10, 30);
 Assert.assertEquals(validTime1.hashCode(), validTime2.hashCode());
 }

 @Test
 public void testTimeWithZeroHoursAndZeroMinutes() {
 validTime1 = new TimeOfDay(0, 0);
 Assert.assertEquals("0:0", validTime1.toString());
 }

 @Test
 public void testTimeWithZeroHoursAndNonZeroMinutes() {
 validTime1 = new TimeOfDay(0, 30);
 Assert.assertEquals("0:30", validTime1.toString());
 }

 @Test
 public void testTimeWithNonZeroHoursAndZeroMinutes() {
 validTime1 = new TimeOfDay(1, 0);
 Assert.assertEquals("1:0", validTime1.toString());
 }

 @Test
 public void testTimeWithNonZeroHoursAndNonZeroMinutes() {
 validTime1 = new TimeOfDay(1, 30);
 Assert.assertEquals("1:30", validTime1.toString());
 }

 @Test
 public void testTimeWithHoursGreaterThan23() {
 validTime1 = new TimeOfDay(24, 0);
 Assert.assertEquals("0:0", validTime1.toString()); // The TimeOfDay class rolls over at 24 hours, so this should be treated as 0:0
 }

 @Test
 public void testTimeWithMinutesGreaterThan59() {
 validTime1 = new TimeOfDay(1, 60);
 Assert.assertEquals("1:0", validTime1.toString()); // The TimeOfDay class rolls over minutes at 59, so this should be treated as 1:0
 }

 @Test
 public void testTimeWithMinutesLessThanZero() {
 invalidTime1 = new TimeOfDay(1, -1);
 Assert.assertThrows(IllegalArgumentException.class, () -> invalidTime1);
 }

 @Test
 public void testTimeWithHoursLessThanZero() {
 invalidTime1 = new TimeOfDay(-1, 30);
 Assert.assertThrows(IllegalArgumentException.class, () -> invalidTime1);
 }

 @Test
 public void testTimeWithInvalidFormatInToStringMethod() {
 validTime1 = new TimeOfDay(10, 30);
 Assert.assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("11:ab"));
 }

 @Test
 public void testTimeWithInvalidFormatInConstructor() {
 Assert.assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("11:ab"));
 }

 @Test
 public void testTimeWithZeroHoursAndZeroMinutesInToStringMethod() {
 validTime1 = new TimeOfDay(0, 0);
 Assert.assertEquals("0:0", validTime1.toString());
 }

 @Test
 public void testTimeWithZeroHoursAndNonZeroMinutesInToStringMethod() {
 validTime1 = new TimeOfDay(0, 30);
 Assert.assertEquals("0:30", validTime1.toString());
 }

 @Test
 public void testTimeWithNonZeroHoursAndZeroMinutesInToStringMethod() {
 validTime1 = new TimeOfDay(1, 0);
 Assert.assertEquals("1:0", validTime1.toString());
 }

 @Test
 public void testTimeWithNonZeroHoursAndNonZeroMinutesInToStringMethod() {
 validTime1 = new TimeOfDay(1, 30);
 Assert.assertEquals("1:30", validTime1.toString());
 }
}