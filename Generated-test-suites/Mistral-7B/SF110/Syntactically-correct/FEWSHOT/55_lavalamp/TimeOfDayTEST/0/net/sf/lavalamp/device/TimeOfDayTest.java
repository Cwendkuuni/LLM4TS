package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithInts() {
        TimeOfDay time = new TimeOfDay(3, 45);
        assertEquals(3, time.getHours());
        assertEquals(45, time.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay time = new TimeOfDay("03:45");
        assertEquals(3, time.getHours());
        assertEquals(45, time.getMinutes());
    }

    @Test
    public void testTimeOfDayComparison() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(4, 0);
        TimeOfDay time3 = new TimeOfDay(3, 45);

        assertEquals(-1, time1.compareTo(time2));
        assertEquals(0, time1.compareTo(time3));
        assertEquals(1, time2.compareTo(time1));
        assertEquals(0, time3.compareTo(time3));
    }

    @Test
    public void testTimeOfDayToString() {
        TimeOfDay time = new TimeOfDay(3, 45);
        assertEquals("03:45", time.toString());
    }

    @Test
    public void testTimeOfDayHashCodeAndEquals() {
        TimeOfDay time1 = new TimeOfDay(3, 45);
        TimeOfDay time2 = new TimeOfDay(3, 45);
        TimeOfDay time3 = new TimeOfDay(4, 0);

        assertEquals(time1.hashCode(), time1.hashCode());
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotSame(time1.hashCode(), time3.hashCode());

        assertTrue(time1.equals(time1));
        assertTrue(time1.equals(time2));
        assertFalse(time1.equals(time3));
    }

    @Test
    public void testEmptyConstructor() {
        assertNull(new TimeOfDay(null));
    }
}