package net.sf.lavalamp.device;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithInts() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals(1, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay time = new TimeOfDay("01:30");
        assertEquals(1, time.getHours());
        assertEquals(30, time.getMinutes());

        time = new TimeOfDay("15:45");
        assertEquals(15, time.getHours());
        assertEquals(45, time.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(2, 0);
        TimeOfDay time3 = new TimeOfDay(1, 30);
        TimeOfDay time4 = new TimeOfDay(2, 30);
        TimeOfDay time5 = new TimeOfDay(0, 0);

        assertEquals(1, time1.compareTo(time2));
        assertEquals(0, time1.compareTo(time3));
        assertEquals(-1, time2.compareTo(time1));
        assertEquals(0, time3.compareTo(time3));
        assertEquals(1, time1.compareTo(time4));
        assertEquals(-1, time4.compareTo(time1));
        assertEquals(0, time5.compareTo(time5));
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(1, 30);
        assertEquals("1:30", time.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(1, 30);
        TimeOfDay time3 = new TimeOfDay(2, 0);

        assertEquals(1013045, time1.hashCode());
        assertEquals(1013045, time2.hashCode());
        assertNotEquals(1013045, time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(1, 30);
        TimeOfDay time3 = new TimeOfDay(2, 0);

        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}