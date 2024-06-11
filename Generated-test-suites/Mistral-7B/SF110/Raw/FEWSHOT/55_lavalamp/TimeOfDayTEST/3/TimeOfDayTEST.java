package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithHoursAndMinutes() {
        TimeOfDay time = new TimeOfDay(12, 30);
        assertEquals(12, time.getHours().intValue());
        assertEquals(30, time.getMinutes().intValue());
    }

    @Test
    public void testTimeOfDayConstructionWithString() {
        TimeOfDay time = new TimeOfDay("14:35");
        assertEquals(14, time.getHours().intValue());
        assertEquals(35, time.getMinutes().intValue());
    }

    @Test
    public void testCompareTo() {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(15, 15);
        TimeOfDay time3 = new TimeOfDay(14, 30);
        TimeOfDay time4 = new TimeOfDay(13, 32);

        assertEquals(1, time1.compareTo(time2));
        assertEquals(0, time1.compareTo(time3));
        assertEquals(-1, time1.compareTo(time4));
    }

    @Test
    public void testToString() {
        TimeOfDay time = new TimeOfDay(14, 30);
        assertEquals("14:30", time.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        TimeOfDay time1 = new TimeOfDay(14, 30);
        TimeOfDay time2 = new TimeOfDay(14, 30);
        TimeOfDay time3 = new TimeOfDay(15, 15);

        assertEquals(time1.hashCode(), time1.hashCode());
        assertEquals(time1.hashCode(), time2.hashCode());
        assertNotNull(time1.hashCode());

        assertEquals(time1, time1);
        assertEquals(time1, time2);
        assertNotEquals(time1, time3);
    }
}