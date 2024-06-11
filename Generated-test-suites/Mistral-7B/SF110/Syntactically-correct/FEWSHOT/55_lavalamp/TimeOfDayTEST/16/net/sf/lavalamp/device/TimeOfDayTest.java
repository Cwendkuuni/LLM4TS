package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithInts() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 45);
        assertEquals(3, timeOfDay.getHours());
        assertEquals(45, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("15:30");
        assertEquals(15, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithStringInvalidFormat() {
        new TimeOfDay("15:60");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(4, 15);
        assertEquals(0, timeOfDay1.compareTo(timeOfDay1));
        assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
        assertEquals(1, timeOfDay2.compareTo(timeOfDay1));
        assertEquals(1, timeOfDay1.compareTo(timeOfDay3));
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 45);
        assertEquals("03:45", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(4, 15);
        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(4, 15);
        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }
}