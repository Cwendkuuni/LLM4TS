package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime1;
 private TimeOfDay invalidTime2;

 @Test
 public void testTimeOfDayConstructorWithInts() {
 validTime1 = new TimeOfDay(11, 30);
 }

 @Test
 public void testTimeOfDayConstructorWithString() {
 validTime2 = new TimeOfDay("11:30");
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidStringHours() {
 assertThrows(NumberFormatException.class, () -> new TimeOfDay("11a:30"));
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidStringMinutes() {
 assertThrows(NumberFormatException.class, () -> new TimeOfDay("11:60"));
 }

 @Test(expected = NumberFormatException.class)
 public void testTimeOfDayConstructorWithInvalidStringFormat() {
 assertThrows(NumberFormatException.class, () -> new TimeOfDay("11:AB"));
 }

 @Test
 public void testGetters() {
 assertNotNull(validTime1.getHours());
 assertNotNull(validTime1.getMinutes());
 assertEquals(11, validTime1.getHours().intValue());
 assertEquals(30, validTime1.getMinutes().intValue());
 }

 @Test
 public void testEquals() {
 TimeOfDay equalTime = new TimeOfDay(11, 30);
 assertTrue(validTime1.equals(equalTime));
 }

 @Test
 public void testEqualsDifferentObjects() {
 TimeOfDay differentTime = new TimeOfDay(12, 0);
 assertFalse(validTime1.equals(differentTime));
 }

 @Test
 public void testEqualsNull() {
 assertFalse(validTime1.equals(null));
 }

 @Test
 public void testCompareTo() {
 TimeOfDay time1 = new TimeOfDay(11, 30);
 TimeOfDay time2 = new TimeOfDay(12, 0);
 TimeOfDay time3 = new TimeOfDay(11, 31);
 TimeOfDay time4 = new TimeOfDay(11, 30);

 assertEquals(-1, time1.compareTo(time2));
 assertEquals(1, time2.compareTo(time1));
 assertEquals(0, time1.compareTo(time4));
 }

 @Test
 public void testCompareToDifferentHours() {
 assertEquals(-1, new TimeOfDay(10, 30).compareTo(new TimeOfDay(11, 30)));
 }

 @Test
 public void testCompareToSameHoursDifferentMinutes() {
 assertEquals(-1, new TimeOfDay(11, 29).compareTo(new TimeOfDay(11, 30)));
 }

 @Test
 public void testCompareToSameHoursSameMinutes() {
 assertEquals(0, new TimeOfDay(11, 30).compareTo(new TimeOfDay(11, 30)));
 }

 @Test
 public void testCompareToSameTime() {
 TimeOfDay time1 = new TimeOfDay(11, 30);
 assertEquals(0, time1.compareTo(time1));
 }

 @Test
 public void testHashcode() {
 TimeOfDay time1 = new TimeOfDay(11, 30);
 TimeOfDay time2 = new TimeOfDay(11, 30);
 assertEquals(time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("11:30", validTime1.toString());
 }

 // Initialize edge cases
 @Before
 public void setUp() {
 invalidTime1 = new TimeOfDay(-1, 0);
 invalidTime2 = new TimeOfDay(24, 0);
 }

 @Test
 public void testInvalidTimeHoursBelowZero() {
 assertNull(invalidTime1);
 }

 @Test
 public void testInvalidTimeHoursAboveTwentyFour() {
 assertNull(invalidTime2);
 }

 @Test
 public void testToStringInvalidTime() {
 assertNull(invalidTime1.toString());
 assertNull(invalidTime2.toString());
 }

}