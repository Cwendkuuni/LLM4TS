package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals(new TimeOfDay(12, 30), timeOfDay);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidHours() {
        new TimeOfDay(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidMinutes() {
        new TimeOfDay(0, 60);
    }

    @Test
    public void testTimeOfDayConstructorWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("14:30");
        assertEquals(new TimeOfDay(14, 30), timeOfDay);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidStringFormat() {
        new TimeOfDay("14:60");
    }

    @Test
    public void testGetHours() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals(12, timeOfDay.getHours().intValue());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(12, 45);
        TimeOfDay timeOfDay4 = new TimeOfDay(12, 30);

        assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
        assertEquals(0, timeOfDay1.compareTo(timeOfDay4));
        assertEquals(1, timeOfDay2.compareTo(timeOfDay3));
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals("12:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(13, 30);

        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(13, 30);

        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }

    @Test
    public void testNull() {
        assertNull(new TimeOfDay(null));
    }
}