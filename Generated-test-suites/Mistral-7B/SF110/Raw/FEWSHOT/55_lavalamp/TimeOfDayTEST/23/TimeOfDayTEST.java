package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);
        assertEquals(10, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay timeOfDay = new TimeOfDay("14:30");
        assertEquals(14, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testCompareToMethod() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(11, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(10, 35);
        TimeOfDay timeOfDay4 = new TimeOfDay(10, 30);

        assertEquals(1, timeOfDay1.compareTo(timeOfDay2));
        assertEquals(-1, timeOfDay2.compareTo(timeOfDay1));
        assertEquals(0, timeOfDay1.compareTo(timeOfDay4));
        assertEquals(-1, timeOfDay3.compareTo(timeOfDay1));
    }

    @Test
    public void testToStringMethod() {
        TimeOfDay timeOfDay = new TimeOfDay(14, 30);
        assertEquals("14:30", timeOfDay.toString());
    }

    @Test
    public void testEqualsMethod() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

        assertTrue(timeOfDay1.equals(timeOfDay1));
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }

    @Test
    public void testHashCodeMethod() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

        int hashCode1 = timeOfDay1.hashCode();
        int hashCode2 = timeOfDay2.hashCode();
        int hashCode3 = timeOfDay3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStringFormatForTimeConstruction() {
        new TimeOfDay("15:60");
    }
}