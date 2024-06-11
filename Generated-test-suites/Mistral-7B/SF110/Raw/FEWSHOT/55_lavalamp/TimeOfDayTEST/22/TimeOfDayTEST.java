package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(15, 45);

        assertEquals(new TimeOfDay(10, 30), timeOfDay1);
        assertEquals(new TimeOfDay(15, 45), timeOfDay2);
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay timeOfDay1 = new TimeOfDay("10:30");
        TimeOfDay timeOfDay2 = new TimeOfDay("15:45");

        assertEquals(new TimeOfDay(10, 30), timeOfDay1);
        assertEquals(new TimeOfDay(15, 45), timeOfDay2);
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(15, 45);
        TimeOfDay timeOfDay3 = new TimeOfDay(10, 30);

        assertTrue(timeOfDay1.compareTo(timeOfDay3) == 0);
        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
        assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
    }

    @Test
    public void testGetHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);

        assertEquals(10, timeOfDay.getHours().intValue());
        assertEquals(30, timeOfDay.getMinutes().intValue());
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(10, 30);

        assertEquals("10:30", timeOfDay.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(10, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(11, 30);

        assertTrue(timeOfDay1.hashCode() == timeOfDay2.hashCode());
        assertTrue(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals(timeOfDay3));
    }
}