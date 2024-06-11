package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithInts() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(5, 15);
        TimeOfDay time3 = new TimeOfDay(10, 30);

        assertEquals(3, time1.getHours());
        assertEquals(45, time1.getMinutes());

        assertEquals(5, time2.getHours());
        assertEquals(15, time2.getMinutes());

        assertEquals(10, time3.getHours());
        assertEquals(30, time3.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay time1 = new TimeOfDay("03:45");
        TimeOfDay time2 = new TimeOfDay("05:15");
        TimeOfDay time3 = new TimeOfDay("10:30");

        assertEquals(3, time1.getHours());
        assertEquals(45, time1.getMinutes());

        assertEquals(5, time2.getHours());
        assertEquals(15, time2.getMinutes());

        assertEquals(10, time3.getHours());
        assertEquals(30, time3.getMinutes());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(5, 15);
        TimeOfDay time3 = new TimeOfDay(10, 30);
        TimeOfDay time4 = new TimeOfDay(3, 45);

        assertEquals(-2, time1.compareTo(time3));
        assertEquals(2, time3.compareTo(time1));

        assertEquals(0, time1.compareTo(time4));
        assertEquals(0, time4.compareTo(time1));

        assertEquals(-1, time1.compareTo(time2));
        assertEquals(1, time2.compareTo(time1));
    }

    @Test
    public void testToString() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(5, 15);
        TimeOfDay time3 = new TimeOfDay(10, 30);

        assertEquals("03:45", time1.toString());
        assertEquals("05:15", time2.toString());
        assertEquals("10:30", time3.toString());

        TimeOfDay time5 = new TimeOfDay(23, 59);
        assertEquals("23:59", time5.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(3, 45);
        TimeOfDay time3 = new TimeOfDay(5, 15);

        assertEquals(time1.hashCode(), time1.hashCode());
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotEquals(time1.hashCode(), time3.hashCode());

        assertEquals(time1, time1);
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }

    @Test
    public void testNull() {
        assertNull(new TimeOfDay(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeFormat() {
        new TimeOfDay("03:60");
    }

    @Test
    public void testTimeOfDayComparison() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(5, 15);
        TimeOfDay time3 = new TimeOfDay(10, 30);
        TimeOfDay time4 = new TimeOfDay(3, 45);

        assertEquals(time1, time4);
        assertSame(time1, time1);
        assertSame(time1, time4);

        assertTrue(time1.compareTo(time2) < 0);
        assertTrue(time2.compareTo(time1) > 0);

        assertTrue(time1.compareTo(time3) < 0);
        assertTrue(time3.compareTo(time1) > 0);
    }
}