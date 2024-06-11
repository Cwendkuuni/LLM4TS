package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest {

 private TimeOfDay validTime1;
 private TimeOfDay validTime2;
 private TimeOfDay invalidTime1;

 @Test
 public void testTimeOfDayConstructionWithInts() {
 validTime1 = new TimeOfDay(10, 30);
 }

 @Test
 public void testTimeOfDayConstructionWithString() {
 validTime2 = new TimeOfDay("14:35");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidTimeOfDayConstructionWithString() {
 String invalidTimeString = "15:65";
 invalidTime1 = new TimeOfDay(invalidTimeString);
 }

 @Test
 public void testGetters() {
 TimeOfDay time = new TimeOfDay(11, 45);
 assertEquals(11, time.getHours().intValue());
 assertEquals(45, time.getMinutes().intValue());
 }

 @Test
 public void testCompareTo() {
 TimeOfDay time1 = new TimeOfDay(15, 25);
 TimeOfDay time2 = new TimeOfDay(14, 55);
 TimeOfDay time3 = new TimeOfDay(15, 25);

 assertEquals(1, time1.compareTo(time2));
 assertEquals(0, time1.compareTo(time3));
 }

 @Test
 public void testToString() {
 TimeOfDay time = new TimeOfDay(23, 59);
 assertEquals("23:59", time.toString());
 }

 @Test
 public void testHashCode() {
 TimeOfDay time1 = new TimeOfDay(15, 25);
 TimeOfDay time2 = new TimeOfDay(15, 25);

 assertEquals(time1.hashCode(), time2.hashCode());
 }

 @Test
 public void testEquals() {
 TimeOfDay time1 = new TimeOfDay(15, 25);
 TimeOfDay time2 = new TimeOfDay(15, 25);
 TimeOfDay time3 = new TimeOfDay(16, 25);

 assertTrue(time1.equals(time1));
 assertTrue(time1.equals(time2));
 assertFalse(time1.equals(time3));
 }

 @Test
 public void testEdgeCases() {
 TimeOfDay midnight = new TimeOfDay(0, 0);
 TimeOfDay noon = new TimeOfDay(12, 0);
 TimeOfDay firstHour = new TimeOfDay(0, 1);
 TimeOfDay lastHour = new TimeOfDay(23, 59);
 TimeOfDay oneMinutePastMidnight = new TimeOfDay(0, 1);
 TimeOfDay fiftyNineMinutes = new TimeOfDay(0, 59);
 TimeOfDay sixtyMinutes = new TimeOfDay(0, 60); // should throw exception

 assertEquals(midnight, new TimeOfDay(0, 0));
 assertEquals(noon, new TimeOfDay(12, 0));
 assertEquals(firstHour, new TimeOfDay(0, 1));
 assertEquals(lastHour, new TimeOfDay(23, 59));
 assertEquals(oneMinutePastMidnight, new TimeOfDay(0, 1));
 assertEquals(fiftyNineMinutes, new TimeOfDay(0, 59));

 try {
 TimeOfDay sixtyMinutesTime = new TimeOfDay(0, 60);
 fail();
 } catch (Exception e) {
 // expected exception
 }
 }
}