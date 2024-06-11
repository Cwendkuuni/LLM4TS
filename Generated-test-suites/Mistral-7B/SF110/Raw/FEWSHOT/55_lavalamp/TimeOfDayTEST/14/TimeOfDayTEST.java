package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(2, 45);

        assertEquals("1:30", timeOfDay1.toString());
        assertEquals("2:45", timeOfDay2.toString());

        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay timeOfDay1 = new TimeOfDay("01:30");
        TimeOfDay timeOfDay2 = new TimeOfDay("02:45");

        assertEquals("1:30", timeOfDay1.toString());
        assertEquals("2:45", timeOfDay2.toString());

        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
    }

    @Test(expected = NumberFormatException.class)
    public void testTimeOfDayConstructionWithInvalidString() {
        new TimeOfDay("01:60");
    }

    @Test
    public void testGetters() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);

        assertEquals(1, timeOfDay.getHours().intValue());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(2, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(1, 30);

        assertTrue(timeOfDay1.compareTo(timeOfDay3) == 0);
        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
        assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 45);

        assertTrue(timeOfDay1.equals(timeOfDay3));
        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertFalse(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 45);

        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }
}