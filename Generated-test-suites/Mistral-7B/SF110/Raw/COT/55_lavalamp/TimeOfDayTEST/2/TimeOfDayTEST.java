package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime;

 @Test
 public void testTimeOfDayConstructorWithValidArguments() {
 validTime1 = new TimeOfDay(11, 30);
 }

 @Test
 public void testToStringMethod() {
 assertEquals("11:30", validTime1.toString());
 }

 @Test
 public void testGetHoursMethod() {
 assertEquals(11, validTime1.getHours().intValue());
 }

 @Test
 public void testGetMinutesMethod() {
 assertEquals(30, validTime1.getMinutes().intValue());
 }

 @Test
 public void testTimeOfDayConstructorWithValidStringArgument() {
 validTime2 = new TimeOfDay("14:22");
 }

 @Test
 public void testCompareToMethod() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(11, 45);
 TimeOfDay timeOfDay4 = new TimeOfDay(11, 30);

 assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
 assertEquals(0, timeOfDay1.compareTo(timeOfDay4));
 assertEquals(-1, timeOfDay2.compareTo(timeOfDay1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructorWithInvalidArgumentsHours() {
 assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(25, 30));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructorWithInvalidArgumentsMinutes() {
 assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(11, 61));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructorWithInvalidArgumentsTimeFormat() {
 assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("11:60"));
 }

 @Test
 public void testTimeOfDayInvalidConstructorWithNullStringArgument() {
 assertNull(new TimeOfDay(null));
 }

 @Test
 public void testInvalidTimeWithInvalidHoursAndMinutes() {
 invalidTime = new TimeOfDay(-1, 30);
 assertNull(invalidTime);
 }

 @Test
 public void testInvalidTimeWithValidHoursAndInvalidMinutes() {
 invalidTime = new TimeOfDay(13, -1);
 assertNull(invalidTime);
 }

 @Test
 public void testInvalidTimeWithValidHoursAndValidMinutesOver24Hours() {
 invalidTime = new TimeOfDay(25, 2);
 assertNull(invalidTime);
 }

 @Test
 public void testEqualsMethod() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);

 assertEquals(timeOfDay1, timeOfDay1);
 assertEquals(timeOfDay1, timeOfDay2);
 assertNotEquals(timeOfDay1, timeOfDay3);
 }

 @Test
 public void testHashCodeMethod() {
 TimeOfDay timeOfDay1 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
 TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);

 assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
 assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 assertNotEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
 }
}