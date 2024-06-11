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

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidHours() {
        new TimeOfDay(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidMinutes() {
        new TimeOfDay(0, 60);
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay time = new TimeOfDay("01:30");
        assertEquals(1, time.getHours());
        assertEquals(30, time.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithInvalidStringFormat() {
        new TimeOfDay("01:61");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(2, 0);
        TimeOfDay time3 = new TimeOfDay(1, 30);

        assertTrue(time1.compareTo(time2) > 0);
        assertEquals(0, time1.compareTo(time3));
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

        assertEquals(1013030, time1.hashCode());
        assertEquals(1013030, time2.hashCode());
        assertNotEquals(1013030, time3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay time1 = new TimeOfDay(1, 30);
        TimeOfDay time2 = new TimeOfDay(1, 30);
        TimeOfDay time3 = new TimeOfDay(2, 0);

        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }
}