package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 // Test case 1: Constructing TimeOfDay object using int hours and int minutes
 @Test
 public void testTimeOfDayConstructionWithIntHoursAndMinutes() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(11, 45);
 TimeOfDay time3 = new TimeOfDay(0, 0);
 TimeOfDay time4 = new TimeOfDay(23, 59);

 assertEquals("10:30", time1.toString());
 assertEquals("11:45", time2.toString());
 assertEquals("0:0", time3.toString());
 assertEquals("23:59", time4.toString());
 }

 // Test case 2: Constructing TimeOfDay object using String time
 @Test
 public void testTimeOfDayConstructionWithStringTime() {
 TimeOfDay time1 = new TimeOfDay("10:30");
 TimeOfDay time2 = new TimeOfDay("11:45");
 TimeOfDay time3 = new TimeOfDay("00:00");
 TimeOfDay time4 = new TimeOfDay("23:59");

 assertEquals("10:30", time1.toString());
 assertEquals("11:45", time2.toString());
 assertEquals("0:0", time3.toString());
 assertEquals("23:59", time4.toString());
 }

 // Test case 3: Comparing TimeOfDay objects using compareTo() method
 @Test
 public void testComparingTimeOfDayObjects() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(11, 45);
 TimeOfDay time3 = new TimeOfDay(10, 30);
 TimeOfDay time4 = new TimeOfDay(9, 59);
 TimeOfDay time5 = new TimeOfDay(11, 45);

 assertTrue(time1.compareTo(time4) > 0);
 assertTrue(time1.compareTo(time3) == 0);
 assertTrue(time2.compareTo(time5) == 0);
 assertTrue(time1.compareTo(time2) < 0);
 }

 // Test case 4: Getting hours and minutes using getHours() and getMinutes() methods
 @Test
 public void testGettingHoursAndMinutes() {
 TimeOfDay time = new TimeOfDay(11, 45);

 assertEquals(11, time.getHours().intValue());
 assertEquals(45, time.getMinutes().intValue());
 }

 // Test case 5: Checking toString() method
 @Test
 public void testToStringMethod() {
 TimeOfDay time = new TimeOfDay(11, 45);

 assertEquals("11:45", time.toString());
 }

 // Test case 6: Checking hashCode() and equals() methods
 @Test
 public void testHashCodeAndEqualsMethods() {
 TimeOfDay time1 = new TimeOfDay(10, 30);
 TimeOfDay time2 = new TimeOfDay(10, 30);
 TimeOfDay time3 = new TimeOfDay(11, 45);

 assertEquals(time1, time1);
 assertEquals(time1, time1);
 assertNotEquals(time1, time2);
 assertNotEquals(time1, time3);
 assertNotEquals(time1, new Object());

 TimeOfDay time4 = new TimeOfDay(10, 30);
 assertEquals(time1, time4);
 }
}