package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithIntHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals(1, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
        assertEquals("1:30", timeOfDay.toString());
    }

    @Test
    public void testTimeOfDayConstructionWithStringTime() {
        TimeOfDay timeOfDay = new TimeOfDay("1:30");
        assertEquals(1, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
        assertEquals("1:30", timeOfDay.toString());
    }

    @Test(expected = NumberFormatException.class)
    public void testTimeOfDayConstructionWithInvalidStringTime() {
        new TimeOfDay("1:61");
    }

    @Test
    public void testCompareToWithSameTimeOfDay() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        assertEquals(0, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareToWithDifferentHours() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(2, 30);
        assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testCompareToWithDifferentMinutes() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 35);
        assertEquals(-3, timeOfDay1.compareTo(timeOfDay2));
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(14, 30);
        assertEquals("14:30", timeOfDay.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 30);
        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }
}