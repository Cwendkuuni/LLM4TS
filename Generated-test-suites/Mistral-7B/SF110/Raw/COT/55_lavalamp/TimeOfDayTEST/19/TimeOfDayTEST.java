package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.junit.Test;

public class TimeOfDayTest
{
    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes()
    {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals(12, timeOfDay.getHours().intValue());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidHours()
    {
        new TimeOfDay(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidMinutes()
    {
        new TimeOfDay(0, 60);
    }

    @Test
    public void testTimeOfDayConstructorWithString()
    {
        TimeOfDay timeOfDay = new TimeOfDay("14:35");
        assertEquals(14, timeOfDay.getHours().intValue());
        assertEquals(35, timeOfDay.getMinutes().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructorWithInvalidStringFormat()
    {
        new TimeOfDay("14:60");
    }

    @Test
    public void testCompareTo()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);

        assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
        assertEquals(0, timeOfDay1.compareTo(timeOfDay3));
    }

    @Test
    public void testGetHours()
    {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertNotNull(timeOfDay.getHours());
        assertEquals(12, timeOfDay.getHours().intValue());
    }

    @Test
    public void testGetMinutes()
    {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertNotNull(timeOfDay.getMinutes());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testToString()
    {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals("12:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(13, 45);

        assertEquals(timeOfDay1.hashCode(), timeOfDay1.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }

    @Test
    public void testEquals()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(13, 45);

        assertEquals(timeOfDay1, timeOfDay1);
        assertEquals(timeOfDay1, timeOfDay2);
        assertNotEquals(timeOfDay1, timeOfDay3);
    }
}