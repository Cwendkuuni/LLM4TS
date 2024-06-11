package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayCreationWithInts() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 15);
        assertEquals(3, timeOfDay.getHours());
        assertEquals(15, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayCreationWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("03:15");
        assertEquals(3, timeOfDay.getHours());
        assertEquals(15, timeOfDay.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayCreationWithInvalidString() {
        new TimeOfDay("25:60");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 15);
        TimeOfDay timeOfDay2 = new TimeOfDay(4, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(3, 15);

        assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
        assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(3, 15);
        assertEquals("03:15", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 15);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 15);

        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(3, 15);
        TimeOfDay timeOfDay2 = new TimeOfDay(3, 15);
        TimeOfDay timeOfDay3 = new TimeOfDay(3, 16);

        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }
}