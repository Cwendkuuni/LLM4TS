package net.sf.lavalamp.device;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TimeOfDayTest {

    @Test
    public void testTimeOfDayConstructionWithIntegerHoursAndMinutes() {
        TimeOfDay timeOfDay = new TimeOfDay(1, 30);
        assertEquals(1, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test
    public void testTimeOfDayConstructionWithStringTime() {
        TimeOfDay timeOfDay = new TimeOfDay("13:30");
        assertEquals(13, timeOfDay.getHours());
        assertEquals(30, timeOfDay.getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTimeOfDayConstructionWithStringTimeInvalidFormat() {
        new TimeOfDay("13:60");
    }

    @Test
    public void testCompareTo() {
        TimeOfDay timeOfDay1 = new TimeOfDay(2, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(1, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(2, 31);

        assertEquals(-1, timeOfDay1.compareTo(timeOfDay2));
        assertEquals(1, timeOfDay2.compareTo(timeOfDay1));
        assertEquals(-1, timeOfDay2.compareTo(timeOfDay3));
        assertEquals(1, timeOfDay3.compareTo(timeOfDay2));
    }

    @Test
    public void testToString() {
        TimeOfDay timeOfDay = new TimeOfDay(13, 30);
        assertEquals("13:30", timeOfDay.toString());
    }

    @Test
    public void testHashCode() {
        TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(14, 30);

        assertEquals(100 * 13 + 30, timeOfDay1.hashCode());
        assertEquals(timeOfDay1.hashCode(), timeOfDay2.hashCode());
        assertNotSame(timeOfDay1.hashCode(), timeOfDay3.hashCode());
    }

    @Test
    public void testEquals() {
        TimeOfDay timeOfDay1 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay2 = new TimeOfDay(13, 30);
        TimeOfDay timeOfDay3 = new TimeOfDay(14, 30);

        assertEquals(timeOfDay1, timeOfDay1);
        assertEquals(timeOfDay1, timeOfDay2);
        assertNotEquals(timeOfDay1, timeOfDay3);
    }

    @Test
    public void testNull() {
        assertNull(new TimeOfDay(null));
    }
}