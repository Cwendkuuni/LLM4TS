package net.sf.lavalamp.device;

import static org.junit.Assert.*;
import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals(12, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("14:45");
        assertEquals(14, timeOfDay.getHours());
        assertEquals(45, timeOfDay.getMinutes());
    }

    @Test(expected = NumberFormatException.class)
    public void testTimeOfDayConstructionWithInvalidString() {
        new TimeOfDay("15:60");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(14, 0);
        int comparisonResult = timeOfDay1.compareTo(timeOfDay2);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(14, 0);
        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(14, 0);
        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(14, 45);
        assertEquals("14:45", timeOfDay.toString());
    }
}