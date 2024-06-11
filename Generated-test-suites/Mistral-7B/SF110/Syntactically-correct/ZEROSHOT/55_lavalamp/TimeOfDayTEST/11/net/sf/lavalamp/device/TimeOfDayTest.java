package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest
{
    @Test
    public void testTimeOfDayConstructorWithHoursMinutes()
    {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals("Unexpected hours value", 10, timeOfDay.getHours());
        assertEquals("Unexpected minutes value", 30, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructorWithString()
    {
        TimeOfDay timeOfDay = new TimeOfDay("14:45");
        assertEquals("Unexpected hours value", 14, timeOfDay.getHours());
        assertEquals("Unexpected minutes value", 45, timeOfDay.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayStringWithInvalidFormat()
    {
        try
        {
            new TimeOfDay("12:60");
            fail("Expected IllegalArgumentException");
        }
        catch(IllegalArgumentException ex)
        {
            // pass
        }
    }

    @Test
    public void testCompareTo()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 15);
        TimeOfDay timeOfDay3 = new TimeOfDay(10, 30);

        int result1 = timeOfDay1.compareTo(timeOfDay2);
        int result2 = timeOfDay2.compareTo(timeOfDay1);
        int result3 = timeOfDay1.compareTo(timeOfDay3);
        int result4 = timeOfDay3.compareTo(timeOfDay1);

        assertEquals("Unexpected compareTo result between timeOfDay1 and timeOfDay2", 1, result1);
        assertEquals("Unexpected compareTo result between timeOfDay2 and timeOfDay1", -1, result2);
        assertEquals("Unexpected compareTo result between timeOfDay1 and timeOfDay3", 0, result3);
        assertEquals("Unexpected compareTo result between timeOfDay3 and timeOfDay1", 0, result4);
    }

    @Test
    public void testToString()
    {
        TimeOfDay timeOfDay = new TimeOfDay(15, 30);
        assertEquals("Unexpected toString result", "15:30", timeOfDay.toString());
    }

    @Test
    public void testHashcodeAndEquals()
    {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 15);

        int hashcode1 = timeOfDay1.hashCode();
        int hashcode2 = timeOfDay2.hashCode();
        int hashcode3 = timeOfDay3.hashCode();

        assertTrue("Unexpected hashcode equality", timeOfDay1.equals(timeOfDay1));
        assertTrue("Unexpected hashcode equality", timeOfDay1.equals(timeOfDay2));
        assertFalse("Unexpected hashcode inequality", timeOfDay1.equals(timeOfDay3));
        assertNotSame("Unexpected hashcode values", hashcode1, hashcode2);
        assertNotSame("Unexpected hashcode values", hashcode1, hashcode3);
    }
}