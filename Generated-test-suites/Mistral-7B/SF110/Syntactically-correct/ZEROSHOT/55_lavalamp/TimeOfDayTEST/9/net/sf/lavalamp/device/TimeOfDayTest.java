package net.sf.lavalamp.device;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimeOfDayTest
{
    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes()
    {
        TimeOfDay time = new TimeOfDay(5, 30);
        assertEquals(5, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidHours()
    {
        new TimeOfDay(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidMinutes()
    {
        new TimeOfDay(0, 60);
    }

    @Test
    public void testTimeOfDayConstructionWithString()
    {
        TimeOfDay time = new TimeOfDay("14:30");
        assertEquals(14, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testTimeOfDayCompareTo()
    {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(12, 0);
        int comparisonResult = time1.compareTo(time2);
        assertTrue(comparisonResult < 0);
    }

    @Test
    public void testTimeOfDayCompareToEqual()
    {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        int comparisonResult = time1.compareTo(time2);
        assertEquals(0, comparisonResult);
    }

    @Test
    public void testTimeOfDayCompareToGreater()
    {
        TimeOfDay time1 = new TimeOfDay(12, 0);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        int comparisonResult = time1.compareTo(time2);
        assertTrue(comparisonResult > 0);
    }

    @Test
    public void testTimeOfDayToString()
    {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals("12:30", time.toString());
    }

    @Test
    public void testTimeOfDayHashCode()
    {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(12, 45);
        int hashCodeResult1 = time1.hashCode();
        int hashCodeResult2 = time2.hashCode();
        assertNotSame(hashCodeResult1, hashCodeResult2);
    }

    @Test
    public void testTimeOfDayEquals()
    {
        TimeOfDay time1 = new TimeOfDay(11, 30);
        TimeOfDay time2 = new TimeOfDay(11, 30);
        TimeOfDay time3 = new TimeOfDay(12, 0);
        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}