package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

/**
* TimeOfDayTest class.
*/
public class TimeOfDayTest {

 /**
 * Test constructor with int hours and int minutes.
 */
 @Test
 public void testTimeOfDayConstructorWithIntHoursAndMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(11, 30);
 assertEquals("11:30", timeOfDay.toString());
 }

 /**
 * Test constructor with String time.
 */
 @Test
 public void testTimeOfDayConstructorWithStringTime() {
 TimeOfDay timeOfDay = new TimeOfDay("14:55");
 assertEquals("14:55", timeOfDay.toString());
 }

 /**
 * Test getHours method.
 */
 @Test
 public void testGetHours() {
 TimeOfDay timeOfDay = new TimeOfDay(15, 30);
 assertEquals(Integer.valueOf(15), timeOfDay.getHours());
 }

 /**
 * Test getMinutes method.
 */
 @Test
 public void testGetMinutes() {
 TimeOfDay timeOfDay = new TimeOfDay(16, 35);
 assertEquals(Integer.valueOf(35), timeOfDay.getMinutes());
 }

 /**
 * Test compareTo method.
 */
 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 45);
 TimeOfDay timeOfDay3 = new TimeOfDay(10, 30);

 assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
 }

 /**
 * Test equals method.
 */
 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(13, 30);

 assertTrue(timeOfDay1.equals(timeOfDay1));
 assertTrue(timeOfDay1.equals(timeOfDay2));
 assertFalse(timeOfDay1.equals(timeOfDay3));
 }

 /**
 * Test hashCode method.
 */
 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);

 assertEquals(Integer.valueOf(101530).hashCode(), timeOfDay1.hashCode());
 assertEquals(Integer.valueOf(101530).hashCode(), timeOfDay2.hashCode());
 assertNotEquals(Integer.valueOf(101540).hashCode(), timeOfDay1.hashCode());
 }

}