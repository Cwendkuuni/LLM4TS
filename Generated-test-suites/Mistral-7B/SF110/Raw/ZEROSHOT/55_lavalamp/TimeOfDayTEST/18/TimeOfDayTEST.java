package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest
{
    @Test
    public void testTimeOfDayConstructionWithInts()
    {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals(10, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidHours()
    {
        try
        {
            new TimeOfDay(-1, 0);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        try
        {
            new TimeOfDay(24, 0);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        try
        {
            new TimeOfDay(24, 60);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        try
        {
            new TimeOfDay(0, 61);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testTimeOfDayConstructionWithString()
    {
        TimeOfDay time = new TimeOfDay("14:30");
        assertEquals(14, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidStringFormat()
    {
        try
        {
            new TimeOfDay("14:61");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        try
        {
            new TimeOfDay("25:30");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }

        try
        {
            new TimeOfDay("12:60");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }

    @Test
    public void testCompareTo()
    {
        TimeOfDay timeA = new TimeOfDay(10, 30);
        TimeOfDay timeB = new TimeOfDay(11, 0);
        TimeOfDay timeC = new TimeOfDay(10, 30);
        TimeOfDay timeD = new TimeOfDay(11, 31);

        assertEquals(1, timeB.compareTo(timeA));
        assertEquals(-1, timeA.compareTo(timeB));

        assertEquals(0, timeC.compareTo(timeC));

        assertEquals(-1, timeA.compareTo(timeD));
        assertEquals(1, timeD.compareTo(timeA));
    }

    @Test
    public void testToString()
    {
        TimeOfDay time = new TimeOfDay(10, 30);
        assertEquals("10:30", time.toString());
    }

    @Test
    public void testHashCode()
    {
        TimeOfDay timeA = new TimeOfDay(10, 30);
        TimeOfDay timeB = new TimeOfDay(11, 0);

        assertEquals(137326451, timeA.hashCode());
        assertEquals(137326437, timeB.hashCode()); // different minutes
    }

    @Test
    public void testEquals()
    {
        TimeOfDay timeA = new TimeOfDay(10, 30);
        TimeOfDay timeB = new TimeOfDay(10, 30);
        TimeOfDay timeC = new TimeOfDay(11, 0);

        assertTrue(timeA.equals(timeA));
        assertTrue(timeA.equals(timeB));

        assertFalse(timeA.equals(timeC));
    }
}