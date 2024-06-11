package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals(10, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("14:35");
        assertEquals(14, timeOfDay.getHours());
        assertEquals(35, timeOfDay.getMinutes());
    }

    @Test
    public void testCompareToWithSameTimeOfDay() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareToWithDifferentHours() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
        assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareToWithSameHoursDifferentMinutes() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 45);
        assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals("10:30", timeOfDay.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);
        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        assertEquals(timeOfDay1, timeOfDay1);
        assertEquals(timeOfDay1, timeOfDay1);
        assertNotEquals(timeOfDay1, timeOfDay2);
        assertNotEquals(timeOfDay1, timeOfDay3);
        assertEquals(timeOfDay1, new TimeOfDay(10, 30));
        assertEquals(timeOfDay1, new TimeOfDay("10:30"));
    }
}