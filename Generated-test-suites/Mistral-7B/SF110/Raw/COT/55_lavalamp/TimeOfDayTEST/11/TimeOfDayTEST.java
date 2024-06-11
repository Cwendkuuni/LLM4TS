package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TimeOfDayTest
{
    @Test
    public void testTimeOfDayConstructorWithInts()
    {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals("1:30", timeOfDay.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidHours()
    {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(-1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidMinutes()
    {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(0, 60));
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(0, -1));
    }

    @Test
    public void testTimeOfDayConstructorWithString()
    {
        TimeOfDay timeOfDay = new TimeOfDay("14:45");
        assertEquals("14:45", timeOfDay.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidStringFormat()
    {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("15:70"));
    }

    @Test
    public void testCompareTo()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(14, 0);
        TimeOfDay timeOfDay3 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay4 = new TimeOfDay(14, 32);

        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
        assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
        assertTrue(timeOfDay1.compareTo(timeOfDay3) == 0);
        assertTrue(timeOfDay3.compareTo(timeOfDay1) == 0);
        assertTrue(timeOfDay2.compareTo(timeOfDay4) < 0);
    }

    @Test
    public void testGetters()
    {
        TimeOfDay timeOfDay = new TimeOfDay(15, 30);
        assertEquals(15, timeOfDay.getHours().intValue());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testToString()
    {
        TimeOfDay timeOfDay = new TimeOfDay(11, 15);
        assertEquals("11:15", timeOfDay.toString());
    }

    @Test
    public void testHashCode()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(11, 15);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 15);
        TimeOfDay timeOfDay3 = new TimeOfDay(12, 15);

        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }

    @Test
    public void testEquals()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(11, 15);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 15);
        TimeOfDay timeOfDay3 = new TimeOfDay(12, 15);

        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
        assertFalse(timeOfDay1.equals(null));
        assertFalse(timeOfDay1.equals("test"));
    }
}