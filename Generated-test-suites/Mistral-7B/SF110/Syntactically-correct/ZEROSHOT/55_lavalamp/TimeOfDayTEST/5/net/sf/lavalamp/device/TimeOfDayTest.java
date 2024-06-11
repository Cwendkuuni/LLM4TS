package net.sf.lavalamp.device;

import org.junit.Test;
import org.junit.Assert.*;

public class TimeOfDayTest
{
    @Test
    public void testConstructorWithHoursAndMinutes()
    {
        TimeOfDay time = new TimeOfDay(11, 30);
        assertEquals(11, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidHours()
    {
        try {
            new TimeOfDay(-1, 0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            new TimeOfDay(24, 0);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            new TimeOfDay(12, 60);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMinutes()
    {
        try {
            new TimeOfDay(11, 60);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            new TimeOfDay(11, -1);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testConstructorWithString()
    {
        TimeOfDay time = new TimeOfDay("11:30");
        assertEquals(11, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStringFormat()
    {
        try {
            new TimeOfDay("11:60");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            new TimeOfDay("12:61");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        try {
            new TimeOfDay("25:30");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCompareTo()
    {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        TimeOfDay time3 = new TimeOfDay(11, 30);

        int comparison = time1.compareTo(time2);
        assertTrue(comparison < 0);

        comparison = time1.compareTo(time3);
        assertEquals(0, comparison);

        comparison = time2.compareTo(time3);
        assertTrue(comparison > 0);
    }

    @Test
    public void testToString()
    {
        TimeOfDay time = new TimeOfDay(11, 30);
        assertEquals("11:30", time.toString());
    }

    @Test
    public void testHashCode()
    {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);

        int hashCode1 = time1.hashCode();
        int hashCode2 = time1.hashCode();
        int hashCode3 = time2.hashCode();
        int hashCode4 = time3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
    }

    @Test
    public void testEquals()
    {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);

        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}