package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class TimeOfDayTest
{
    @Test
    public void testTimeOfDayConstructorWithInts()
    {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertNotNull(time);
        assertEquals("12:30", time.toString());
    }

    @Test
    public void testTimeOfDayConstructorWithString()
    {
        TimeOfDay time = new TimeOfDay("14:25");
        assertNotNull(time);
        assertEquals("14:25", time.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayInvalidHoursWithInts()
    {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(-1, 30));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayInvalidHoursWithString()
    {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("25:30"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayInvalidMinutesWithInts()
    {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay(12, 61));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayInvalidMinutesWithString()
    {
        assertThrows(IllegalArgumentException.class, () -> new TimeOfDay("12:61"));
    }

    @Test
    public void testTimeOfDayCompareTo()
    {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(13, 45);
        TimeOfDay time3 = new TimeOfDay(14, 30);

        assertEquals(-1, time1.compareTo(time2));
        assertEquals(0, time1.compareTo(time3));
    }

    @Test
    public void testTimeOfDayGetters()
    {
        TimeOfDay time = new TimeOfDay(13, 30);

        assertEquals(13, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testTimeOfDayToString()
    {
        TimeOfDay time = new TimeOfDay(23, 59);
        assertEquals("23:59", time.toString());
    }

    @Test
    public void testTimeOfDayHashCode()
    {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(13, 31);

        assertEquals(time1.hashCode(), time1.hashCode());
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }

    @Test
    public void testTimeOfDayEquals()
    {
        TimeOfDay time1 = new TimeOfDay(12, 30);
        TimeOfDay time2 = new TimeOfDay(12, 30);
        TimeOfDay time3 = new TimeOfDay(13, 31);

        assertEquals(time1, time1);
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }
}