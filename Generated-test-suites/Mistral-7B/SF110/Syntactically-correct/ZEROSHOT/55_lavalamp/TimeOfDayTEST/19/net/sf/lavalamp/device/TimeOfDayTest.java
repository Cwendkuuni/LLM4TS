package net.sf.lavalamp.device;

import org.junit.Test;
import org.junit.Assert.*;

public class TimeOfDayTest {

 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);
 TimeOfDay timeOfDay3 = new TimeOfDay("14:56");
 TimeOfDay timeOfDay4 = new TimeOfDay("15:30");
 TimeOfDay timeOfDay5 = new TimeOfDay("00:00");
 TimeOfDay timeOfDay6 = new TimeOfDay("23:59");

 @Test
 public void testConstructorWithHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 30);
 assertEquals(15, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test
 public void testConstructorWithString() {
 TimeOfDay timeOfDay = new TimeOfDay("15:30");
 assertEquals(15, timeOfDay.getHours());
 assertEquals(30, timeOfDay.getMinutes());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeStringFormat() {
 TimeOfDay timeOfDay = null;
 try {
 timeOfDay = new TimeOfDay("15:60");
 } catch (NumberFormatException e) {
 //expected
 }
 Assert.assertNull(timeOfDay);
 }

 @Test
 public void testToStringMethod() {
 assertEquals("15:30", timeOfDay3.toString());
 }

 @Test
 public void testCompareToMethod() {
 // Test comparing same TimeOfDay objects
 int compareResult = timeOfDay1.compareTo(timeOfDay1);
 assertEquals(0, compareResult);

 // Test comparing different hours
 compareResult = timeOfDay1.compareTo(timeOfDay2);
 assertEquals(-1, compareResult);

 // Test comparing different minutes with same hours
 compareResult = timeOfDay3.compareTo(timeOfDay4);
 assertEquals(-1, compareResult);

 // Test comparing earlier time
 compareResult = timeOfDay5.compareTo(timeOfDay1);
 assertEquals(-1, compareResult);

 // Test comparing later time
 compareResult = timeOfDay6.compareTo(timeOfDay1);
 assertEquals(1, compareResult);
 }

 @Test
 public void testEqualsMethod() {
 TimeOfDay timeOfDay7 = new TimeOfDay(15, 30);
 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay7));

 TimeOfDay timeOfDay8 = new TimeOfDay(15, 31);
 assertFalse(timeOfDay1.equals(timeOfDay8));

 TimeOfDay timeOfDay9 = new TimeOfDay(14, 30);
 assertFalse(timeOfDay1.equals(timeOfDay9));
 }

 @Test
 public void testHashCodeMethod() {
 int hashCode1 = timeOfDay1.hashCode();
 int hashCode2 = timeOfDay2.hashCode();
 int hashCode3 = timeOfDay3.hashCode();
 int hashCode4 = timeOfDay4.hashCode();
 int hashCode5 = timeOfDay5.hashCode();
 int hashCode6 = timeOfDay6.hashCode();

 assertNotSame(hashCode1, hashCode2);
 assertNotSame(hashCode1, hashCode3);
 assertNotSame(hashCode1, hashCode4);
 assertNotSame(hashCode1, hashCode5);
 assertNotSame(hashCode1, hashCode6);

 TimeOfDay timeOfDay10 = new TimeOfDay(15, 30);
 assertEquals(hashCode1, timeOfDay10.hashCode());
 }
}