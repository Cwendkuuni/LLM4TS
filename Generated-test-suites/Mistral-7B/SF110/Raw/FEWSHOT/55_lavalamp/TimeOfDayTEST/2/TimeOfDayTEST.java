package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(3, 45);
        assertEquals(3, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay time = new TimeOfDay("03:45");
        assertEquals(3, time.getHours().intValue());
        assertEquals(45, time.getMinutes().intValue());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(5, 15);
        TimeOfDay time3 = new TimeOfDay(3, 45);
        TimeOfDay time4 = new TimeOfDay(3, 45);

        assertEquals(1, time1.compareTo(time2));
        assertEquals(0, time1.compareTo(time3));
        assertEquals(0, time1.compareTo(time4));
    }

    @Test
    public void testGetHours() {
        TimeOfDay time = new TimeOfDay(3, 45);
        assertEquals(3, time.getHours().intValue());
    }

    @Test
    public void testGetMinutes() {
        TimeOfDay time = new TimeOfDay(3, 45);
        assertEquals(45, time.getMinutes().intValue());
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(3, 45);
        assertEquals("03:45", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(3, 45);
        TimeOfDay time3 = new TimeOfDay(5, 15);

        assertEquals(time1.hashCode(), time1.hashCode());
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(3, 45);
        TimeOfDay time3 = new TimeOfDay(5, 15);

        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}