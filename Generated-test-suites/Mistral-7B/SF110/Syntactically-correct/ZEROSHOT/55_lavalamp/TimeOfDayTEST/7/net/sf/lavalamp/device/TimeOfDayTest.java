package net.sf.lavalamp.device;

import org.junit.Test;
import org.junit.Assert.*;

public class TimeOfDayTest {

 @Test
 public void testTimeOfDayConstructionWithInts() {
 TimeOfDay timeOfDay = new TimeOfDay(12, 30);
 Assert.assertEquals("12:30", timeOfDay.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidHours() {
 try {
 TimeOfDay timeOfDay = new TimeOfDay(25, 0);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Invalid hours value. Hours should be between 0 and 23", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidMinutes() {
 try {
 TimeOfDay timeOfDay = new TimeOfDay(12, 61);
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Invalid minutes value. Minutes should be between 0 and 59", e.getMessage());
 }
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 TimeOfDay timeOfDay = new TimeOfDay("14:35");
 Assert.assertEquals("14:35", timeOfDay.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTimeOfDayConstructionWithInvalidStringFormat() {
 try {
 TimeOfDay timeOfDay = new TimeOfDay("15:65");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("Invalid time string format. Must be hh:mm", e.getMessage());
 }
 }

 @Test
 public void testCompareTo() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 45);
 TimeOfDay timeOfDay2 = new TimeOfDay(14, 30);
 Assert.assertLessThan(0, timeOfDay1.compareTo(timeOfDay2));

 timeOfDay1 = new TimeOfDay(13, 45);
 timeOfDay2 = new TimeOfDay(13, 45);
 Assert.assertEquals(0, timeOfDay1.compareTo(timeOfDay2));

 timeOfDay1 = new TimeOfDay(12, 30);
 timeOfDay2 = new TimeOfDay(13, 30);
 Assert.assertLessThan(0, timeOfDay1.compareTo(timeOfDay2));
 }

 @Test
 public void testGetters() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 45);
 Assert.assertEquals(13, timeOfDay.getHours().intValue());
 Assert.assertEquals(45, timeOfDay.getMinutes().intValue());
 }

 @Test
 public void testToString() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 45);
 Assert.assertEquals("13:45", timeOfDay.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 45);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);
 Assert.assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 45);
 TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);
 Assert.assertTrue(timeOfDay1.equals(timeOfDay2));
 }

 @Test
 public void testEqualsWithDifferentObjects() {
 TimeOfDay timeOfDay1 = new TimeOfDay(13, 45);
 String string = "test";
 Assert.assertFalse(timeOfDay1.equals(string));
 }

 @Test
 public void testEqualsWithNull() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 45);
 Assert.assertFalse(timeOfDay.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClass() {
 TimeOfDay timeOfDay = new TimeOfDay(13, 45);
 Object object = new Object();
 Assert.assertFalse(timeOfDay.equals(object));
 }
}