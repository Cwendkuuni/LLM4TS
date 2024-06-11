package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TimeOfDayTest
{
    @Test
    public void testTimeOfDayConstructorWithHoursAndMinutes()
    {
        TimeOfDay time = new TimeOfDay(5, 30);
        assertEquals(5, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithString()
    {
        TimeOfDay time = new TimeOfDay("11:35");
        assertEquals(11, time.getHours());
        assertEquals(35, time.getMinutes());
    }

    @Test
    public void testCompareToWhenCurrentTimeIsGreater()
    {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(13, 45);
        int comparisonResult = time1.compareTo(time2);
        assertEquals(1, comparisonResult);
    }

    @Test
    public void testCompareToWhenCurrentTimeIsLess()
    {
        TimeOfDay time1 = new TimeOfDay(13, 45);
        TimeOfDay time2 = new TimeOfDay(14, 30);
        int comparisonResult = time1.compareTo(time2);
        assertEquals(-1, comparisonResult);
    }

    @Test
    public void testCompareToWhenCurrentTimeIsEqual()
    {
        TimeOfDay time1 = new TimeOfDay(13, 45);
        TimeOfDay time2 = new TimeOfDay(13, 45);
        int comparisonResult = time1.compareTo(time2);
        assertEquals(0, comparisonResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructionWithHoursLessThanZero()
    {
        new TimeOfDay(-2, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructionWithMinutesLessThanZero()
    {
        new TimeOfDay(5, -30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructionWithStringLengthMismatch()
    {
        new TimeOfDay("11:35:45");
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidConstructionWithStringContainingNonNumericCharacters()
    {
        new TimeOfDay("11:35A");
    }

    @Test
    public void testToString()
    {
        TimeOfDay time = new TimeOfDay(14, 30);
        assertEquals("14:30", time.toString());
    }

    @Test
    public void testHashCode()
    {
        TimeOfDay time = new TimeOfDay(14, 30);
        int hashCode = time.hashCode();
        TimeOfDay sameTime = new TimeOfDay(14, 30);
        int sameHashCode = sameTime.hashCode();
        assertEquals(hashCode, sameHashCode);

        TimeOfDay differentTime = new TimeOfDay(15, 30);
        int differentHashCode = differentTime.hashCode();
        assertNotEquals(hashCode, differentHashCode);
    }

    @Test
    public void testEqualsWhenEqual()
    {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(14, 30);
        assertTrue(time1.equals(time2));
    }

    @Test
    public void testEqualsWhenNotEqual()
    {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(15, 30);
        assertFalse(time1.equals(time2));

        TimeOfDay time3 = new TimeOfDay(14, 31);
        assertFalse(time1.equals(time3));

        Object notATime = new Object();
        assertFalse(time1.equals(notATime));
    }
}