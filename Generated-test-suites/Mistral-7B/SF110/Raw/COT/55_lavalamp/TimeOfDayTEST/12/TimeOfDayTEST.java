package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime;

 @Before
 public void setUp() {
 validTime1 = new TimeOfDay(14, 30);
 validTime2 = new TimeOfDay("15:45");
 invalidTime = new TimeOfDay("25:60"); // this hour and minute combination is invalid
 }

 @Test
 public void testConstructorWithHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(12, 30);
 assertEquals("12:30", time.toString());
 }

 @Test
 public void testConstructorWithString() {
 TimeOfDay time = new TimeOfDay("11:15");
 assertEquals("11:15", time.toString());
 }

 @Test
 public void testGetHours() {
 assertEquals(14, validTime1.getHours().intValue());
 }

 @Test
 public void testGetMinutes() {
 assertEquals(30, validTime1.getMinutes().intValue());
 }

 @Test
 public void testCompareTo() {
 assertLessThan(validTime1.compareTo(validTime2), 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeFormatWithHoursGreaterThan23() {
 new TimeOfDay(24, 30); // should throw an IllegalArgumentException
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeFormatWithInvalidHours() {
 new TimeOfDay("AB:CD"); // should throw a NumberFormatException
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeFormatWithInvalidMinutes() {
 new TimeOfDay("12:60"); // should throw a NumberFormatException
 }

 @Test
 public void testEquals() {
 TimeOfDay validTime3 = new TimeOfDay(14, 30);
 assertTrue(validTime1.equals(validTime1));
 assertTrue(validTime1.equals(validTime3));
 assertFalse(validTime1.equals(invalidTime));
 }

 @Test
 public void testHashCode() {
 TimeOfDay validTime3 = new TimeOfDay(14, 30);
 assertEquals(validTime1.hashCode(), validTime3.hashCode());
 }

}