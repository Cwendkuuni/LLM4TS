package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(12, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay time = new TimeOfDay("14:35");
        assertEquals(14, time.getHours());
        assertEquals(35, time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(14, 0);
        TimeOfDay time3 = new TimeOfDay(13, 59);
        assertGreaterThan(time1.compareTo(time3), 0);
        assertLessThan(time1.compareTo(time2), 0);
        assertGreaterThan(time2.compareTo(time3), 0);
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(14, 30);
        assertEquals("14:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(13, 59);
        assertNotEquals(time1.hashCode(), time2.hashCode());

        TimeOfDay time3 = new TimeOfDay(13, 30);
        assertEquals(time1.hashCode(), time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(13, 30);
        TimeOfDay time2 = new TimeOfDay(13, 30);
        assertTrue(time1.equals(time2));

        TimeOfDay time3 = new TimeOfDay(13, 31);
        assertFalse(time1.equals(time3));

        Object obj = new Object();
        assertFalse(time1.equals(obj));
    }
}