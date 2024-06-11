package net.sf.lavalamp.device;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class TimeOfDayTest {

    TimeOfDay timeOfDay1;
    TimeOfDay timeOfDay2;
    TimeOfDay timeOfDay3;

    @Before
    public void setUp() {
        timeOfDay1 = new TimeOfDay(10, 30);
        timeOfDay2 = new TimeOfDay(11, 45);
        timeOfDay3 = new TimeOfDay("12:30");
    }

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(5, 15);
        assertEquals(5, timeOfDay.getHours());
        assertEquals(15, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        assertEquals(timeOfDay3.getHours(), 12);
        assertEquals(timeOfDay3.getMinutes(), 30);
    }

    @Test
    public void testCompareTo() {
        assertTrue(timeOfDay1.compareTo(timeOfDay2) < 0);
        assertTrue(timeOfDay2.compareTo(timeOfDay1) > 0);
        assertEquals(0, timeOfDay1.compareTo(timeOfDay1));
    }

    @Test
    public void testGetters() {
        assertEquals(timeOfDay1.getHours(), 10);
        assertEquals(timeOfDay1.getMinutes(), 30);

        LocalTime localTime = LocalTime.parse("10:30");
        assertEquals(localTime.getHour(), timeOfDay1.getHours().intValue());
        assertEquals(localTime.getMinute(), timeOfDay1.getMinutes().intValue());
    }

    @Test
    public void testToString() {
        assertEquals(timeOfDay1.toString(), "10:30");
        assertEquals(timeOfDay3.toString(), "12:30");
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay4 = new TimeOfDay(10, 30);
        assertEquals(timeOfDay1.hashCode(), timeOfDay4.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay4 = new TimeOfDay(10, 30);
        assertTrue(timeOfDay1.equals(timeOfDay4));
        assertFalse(timeOfDay1.equals(timeOfDay2));
        assertFalse(timeOfDay1.equals("TimeOfDayTest"));
    }
}