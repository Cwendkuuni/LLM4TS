package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TimeOfDayTest
{
    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes()
    {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertEquals(new Integer(12), timeOfDay.getHours());
        assertEquals(new Integer(30), timeOfDay.getMinutes());
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
        assertEquals(new Integer(14), timeOfDay.getHours());
        assertEquals(new Integer(35), timeOfDay.getMinutes());
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
    public void testToString()
    {
        TimeOfDay timeOfDay = new TimeOfDay(14, 35);
        assertEquals("14:35", timeOfDay.toString());
    }

    @Test
    public void testHashCode()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);
        assertEquals(Integer.hashCode(100 * 12 + 30), timeOfDay1.hashCode());
        assertNotEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }

    @Test
    public void testEquals()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(12, 30);
        TimeOfDay timeOfDay4 = null;
        TimeOfDay timeOfDay5 = new Object();
        assertFalse(timeOfDay1.equals(timeOfDay2));
        assertTrue(timeOfDay1.equals(timeOfDay3));
        assertFalse(timeOfDay1.equals(timeOfDay4));
        assertFalse(timeOfDay1.equals(timeOfDay5));
    }

    @Test
    public void testGetters()
    {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        assertNull(timeOfDay.getHours());
        assertNull(timeOfDay.getMinutes());
        timeOfDay.setHours(14);
        timeOfDay.setMinutes(35);
        assertEquals(new Integer(14), timeOfDay.getHours());
        assertEquals(new Integer(35), timeOfDay.getMinutes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetters()
    {
        TimeOfDay timeOfDay = new TimeOfDay(12, 30);
        timeOfDay.setHours(14);
        timeOfDay.setMinutes(35);
        timeOfDay.setHours(15);
    }
}